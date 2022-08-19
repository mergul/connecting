package com.busra.connecting.controller;

import com.busra.connecting.model.*;
import com.busra.connecting.service.NewsService;
import com.busra.connecting.service.NewsStreams;
import com.busra.connecting.service.Sender;
import com.busra.connecting.service.UserService;
import org.apache.kafka.common.protocol.types.Field;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class NewsHandler {

    @Value("${topics.kafka.listcom-out}")
    private String listcomTopics;
    @Value("${topics.kafka.partitioncom-out}")
    private String partitioncomTopics;
    @Value("${topics.kafka.paymentcom-out}")
    private String paymentcomTopics;
    @Value("${topics.kafka.balancecom-out}")
    private String balancecomTopics;
    @Value("${topics.kafka.checkout-out}")
    private String checkoutTopics;
    @Value("${topics.kafka.usersHistories-out}")
    private String usersHistoriesTopics;

    private final UserService userService;
    private final NewsService newsService;
    private final Sender kafkaSender;

    public NewsHandler(@Qualifier("userService") UserService userService,
                       @Qualifier("newsService") NewsService newsService,
                       Sender kafkaSender) {
        this.userService = userService;
        this.newsService = newsService;
        this.kafkaSender = kafkaSender;
    }

    private Mono<User> getAuthUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> (User) auth.getPrincipal());
    }

    Mono<Boolean> saveNewsMetadata(Mono<NewsFeed> newsFeedMono) {
        ObjectId newsId = new ObjectId();
        return getAuthUser().flatMap(user -> newsFeedMono.map(newsFeed -> News.of(newsId)
                .withOwnerId(user.getId().toHexString())
                .withSummary(newsFeed.getSummary())
                .withTopic(newsFeed.getTopic())
                .withOwner(user.getUsername())
                .withTags(newsFeed.getTags())
                .withMediaParts(newsFeed.getMediaParts())
                .withDate(newsFeed.getDate())
                .withCount(0L)
                .withClean(false)
                .withArrest(false).withOwnerUrl(user.getImage())
                .withMediaReviews(newsFeed.getMediaReviews()).build()))
                .flatMapMany(this::addNews).reduce(true, (aLong, aLong2) -> aLong && aLong2);
    }

    private Flux<Boolean> addNews(News newNews) {
//       return Flux.create(sink -> {
//            Arrays.asList(1, 2)
//                    .forEach(integer -> getSend(integer, newNews).map(sink::next).subscribe());
//            sink.complete();
//        });
        return Flux.range(1, 2).flatMap(integer -> getSend(integer, newNews).subscribeOn(Schedulers.boundedElastic()), 2);
        //  return this.kafkaSender.send(NewsStreams.NEWS_OUT, newNews, newNews.getId().toHexString().getBytes(), true).subscribeOn(Schedulers.boundedElastic()).flatMap(aBoolean -> this.kafkaSender.send(NewsStreams.PAGEVIEWS_OUT, extractNewsPayload(newNews), newNews.getId().toHexString().getBytes(), true).subscribeOn(Schedulers.boundedElastic()));
    }

    private Mono<Boolean> getSend(Integer count, News newNews) {
        return count == 1 ? this.kafkaSender.send(NewsStreams.NEWS_OUT, newNews, newNews.getId().toHexString().getBytes(), true)
                : this.kafkaSender.send(NewsStreams.PAGEVIEWS_OUT, extractNewsPayload(newNews), newNews.getId().toHexString().getBytes(), true);
    }

    private NewsPayload extractNewsPayload(News news) {
        return NewsPayload.of(news.getId())
                .withClean(news.getClean()).withCount(news.getCount())
                .withDate(news.getDate()).withNewsOwner(news.getOwner())
                .withNewsOwnerId(news.getOwnerId()).withOwnerUrl(news.getOwnerUrl())
                .withTags(news.getTags()).withTopic(news.getTopic())
                .withThumb(news.getMediaReviews().get(0).getFile_name())
                .withTopics(news.getTags()).build();
    }

    Mono<News> getNewsById(String id) {
        return this.newsService.findById(id).flatMap(news -> {
            kafkaSender.send(NewsStreams.PAGEVIEWS_OUT, extractNewsPayload(news), news.getId().toHexString().getBytes(), true).subscribeOn(Schedulers.boundedElastic());
            return Mono.just(news);
        });
    }

    Mono<Boolean> setNewsCounts(String id) {
        return this.newsService.findById(id)
                .flatMap(news -> kafkaSender.send(NewsStreams.PAGEVIEWS_OUT, extractNewsPayload(news), news.getId().toHexString().getBytes(), true).subscribeOn(Schedulers.boundedElastic()));
    }
//    Mono<Boolean> setNewsCounts(Mono<News> newsMono) {
//        return newsMono.flatMap(newsService::sendGreeting);
//    }

    Flux<News> findAllNames() {
        return null;
    }

    Mono<User> getUserById(String id, String random, String fed) {
        return getAuthUser().flatMap(user -> {
            if (fed.equals("0")) kafkaSender.send(NewsStreams.AUTHS_OUT, UserPayload.of(user.getId().toHexString()).withTags(user.getTags()).withUsers(user.getUsers()).withIndex(0).withRandom(random).withIsAdmin(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))).build(), user.getId().toHexString().getBytes(), false).subscribeOn(Schedulers.boundedElastic());
            return Mono.just(user);
        });
    }

    public Mono<Boolean> addTag(Mono<UserTag> body, String random) {
        return getAuthUser().flatMap(user -> body.map(userTag -> Tuples.of(user, userTag)))
                .map(objects -> {
                    String tag = objects.getT2().getTag();
                    List<String> followings = new ArrayList<String>();
                    boolean isTag = tag.charAt(0) == '#';
                    User.Builder builder = User.from(objects.getT1());
                    if (isTag && !objects.getT1().getTags().contains(objects.getT2().getTag().substring(1))) {
                        followings.addAll(objects.getT1().getTags());
                        followings.add(tag.substring(1));
                        builder.withTags(followings);
                    } else if (!objects.getT1().getUsers().contains(objects.getT2().getTag().substring(1))) {
                        followings.addAll(objects.getT1().getUsers());
                        followings.add(objects.getT2().getTag().substring(1));
                        builder.withUsers(followings);
                    }
                    return Tuples.of(builder.build(), objects.getT2(), !isTag);
                }).flatMap(tuple -> Flux.range(1, 3).flatMap(ing -> ing == 1 ? this.userService.save(tuple.getT1())
                        : ing == 2 ? kafkaSender.send(NewsStreams.AUTHS_OUT, UserPayload.of(tuple.getT1().getId().toHexString()).withTags(!tuple.getT3() ? Collections.singletonList(tuple.getT2().getTag()) : Collections.emptyList()).withUsers(!tuple.getT3() ? Collections.emptyList() : Collections.singletonList(tuple.getT2().getTag())).withIndex(!tuple.getT3() ? 1 : 2).withRandom(random).withIsAdmin(tuple.getT1().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))).build(), tuple.getT1().getId().toHexString().getBytes(), false).subscribeOn(Schedulers.boundedElastic())
                        : tuple.getT3() ? manageFollowers(tuple.getT2(), true) : Mono.empty(), 2).collectList()
                ).then(Mono.just(true));
    }

    public Mono<Boolean> removeTag(Mono<UserTag> body) {
        return getAuthUser().flatMap(user -> body.map(userTag -> Tuples.of(user, userTag)))
                .map(objects -> {
                    String tag = objects.getT2().getTag();
                    List<String> followings = new ArrayList<String>();
                    boolean isTag = tag.charAt(0) == '#';
                    User.Builder builder = User.from(objects.getT1());
                    if (isTag) {
                        objects.getT1().getTags().forEach(_tag -> {
                            if (!_tag.equals(tag.substring(1))) followings.add(_tag);
                        });
                        builder.withTags(followings);
                    } else {
                        objects.getT1().getUsers().forEach(_tag -> {
                            if (!_tag.equals(tag.substring(1))) followings.add(_tag);
                        });
                        builder.withUsers(followings);
                    }
                    return Tuples.of(builder.build(), objects.getT2(), !isTag);
                }).flatMap(tuple -> Flux.range(1, 2).flatMap(ing -> ing == 1 ? this.userService.save(tuple.getT1())
                        : tuple.getT3() ? manageFollowers(tuple.getT2(), false) : Mono.empty(), 2).collectList()
                ).then(Mono.just(true));
    }

    private Mono<User> manageFollowers(UserTag userTag, boolean adding) {
        List<String> followers = new ArrayList<String>();
        String follower = userTag.getId();
        return this.userService.findById(userTag.getTag().substring(1))
                .flatMap(user -> {
                    User.Builder builder = User.from(user);
                    followers.addAll(user.getFollowers());
                    if (adding) {
                        if (!followers.contains(follower)) {
                            followers.add(follower);
                        }
                    } else {
                        followers.remove(follower);
                    }
                    builder.withFollowers(followers);
                    return this.userService.save(builder.build());
                });
    }

    //
//    public Mono<Boolean> removesdTag(Mono<UserTag> body) {
//        return body.flatMap(userTag -> getAuthUser().flatMap(user -> {
//            String tag = userTag.getTag();
//            List<String> followings = new ArrayList<String>();
//            boolean isTag = tag.charAt(0) == '#';
//            User.Builder builder = User.from(user);
//            if (isTag) {
//                user.getTags().forEach(_tag -> {
//                    if (!_tag.equals(tag.substring(1))) followings.add(_tag);
//                });
//                builder.withTags(followings);
//            } else {
//                user.getUsers().forEach(_tag -> {
//                    if (!_tag.equals(tag.substring(1))) followings.add(_tag);
//                });
//                builder.withUsers(followings);
//            }
//            return this.userService.save(builder.build()).then(Mono.just(userTag));
//        })).flatMap(userTag -> managesdFollowers(userTag, false));
//    }
//
//    private Mono<Boolean> managesdFollowers(UserTag userTag, boolean adding) {
//        if (userTag.getTag().charAt(0) == '@') {
//            List<String> followers = new ArrayList<String>();
//            String follower = userTag.getId();
//            return this.userService.findById(userTag.getTag().substring(1))
//                    .flatMap(user -> {
//                        User.Builder builder = User.from(user);
//                        followers.addAll(user.getFollowers());
//                        if (adding) {
//                            if (!followers.contains(follower)) {
//                                followers.add(follower);
//                            }
//                        } else {
//                            followers.remove(follower);
//                        }
//                        builder.withFollowers(followers);
//                        return this.userService.save(builder.build()).then(Mono.just(true));
//                    });
//        } else
//            return Mono.just(true);
//    }
//
//    public Mono<Boolean> addsdTag(Mono<UserTag> body, String random) {
//        return body.flatMap(userTag -> this.getAuthUser()
//                .flatMap(user -> {
//                    if (!user.getTags().contains(userTag.getTag().substring(1)) && !user.getUsers().contains(userTag.getTag().substring(1))) {
//                        String tag = userTag.getTag();
//                        List<String> followings = new ArrayList<String>();
//                        boolean isTag = tag.charAt(0) == '#';
//                        User.Builder builder = User.from(user);
//                        if (isTag) {
//                            followings.addAll(user.getTags());
//                            followings.add(tag.substring(1));
//                            builder.withTags(followings);
//                        } else {
//                            followings.addAll(user.getUsers());
//                            followings.add(userTag.getTag().substring(1));
//                            builder.withUsers(followings);
//                        }
//                        return this.userService.save(builder.build())
//                                .flatMap(user1 -> {
//                                    kafkaSender.send(NewsStreams.AUTHS_OUT, UserPayload.of(user1.getId().toHexString()).withTags(isTag ? Collections.singletonList(tag) : Collections.emptyList()).withUsers(isTag ? Collections.emptyList() : Collections.singletonList(tag)).withIndex(isTag ? 1 : 2).withRandom(random).withIsAdmin(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))).build(), user1.getId().toHexString().getBytes(), false).subscribeOn(Schedulers.boundedElastic());
//                                    return Mono.just(userTag);
//                                });
//                    }
//                    return Mono.empty();
//                })).flatMap(userTag -> managesdFollowers(userTag, true));
//    }
    public Mono<User> getUserByStart(String id, String random) {
        return this.userService.findById(id).flatMap(user -> {
            kafkaSender.send(NewsStreams.AUTHS_OUT, UserPayload.of(user.getId().toHexString()).withTags(user.getTags()).withUsers(user.getUsers()).withIndex(0).withRandom(random).withIsAdmin(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))).build(), user.getId().toHexString().getBytes(), false).subscribeOn(Schedulers.boundedElastic());
            return Mono.just(user);
        });
    }

    public Mono<User> getUserByEmail(String mid, String email, String random) {
        if (!mid.contains("@") && !mid.contains("#")) {
            String id = mid;
            if (mid.length() == 12) id = (new ObjectId(mid.getBytes())).toHexString();
            return this.userService.findById(id).flatMap(user -> {
                        if (!email.equals("a") && user.getBlocked() != null && user.getBlocked().size() > 0 && user.getBlocked().contains(email)) {
                            return Mono.just(User.of(user.getId()).build());
                        } else {
                            if (!email.equals("a"))
                                kafkaSender.send(NewsStreams.AUTHS_OUT, UserPayload.of(user.getId().toHexString()).withTags(user.getTags()).withUsers(user.getUsers()).withIndex(0).withRandom(random).withIsAdmin(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))).build(), user.getId().toHexString().getBytes(), false).subscribeOn(Schedulers.boundedElastic());
                            return Mono.just(user);
                        }
                    }
            );
        } else if (!mid.contains("@")) { //username
            return this.userService.findByUsernameId(mid.substring(1)).flatMap(user -> {
                        if (!email.equals("a") && user.getBlocked().contains((new ObjectId(email.getBytes())).toHexString()))
                            return Mono.just(User.of().build());
                        else return Mono.just(user);
//                                    kafkaSender.send(NewsStreams.AUTHS_OUT, UserPayload.of(user.getId().toHexString()).withTags(user.getTags()).withUsers(user.getUsers()).withIndex(0).withRandom(random).build(), user.getId().toHexString().getBytes(), false).subscribeOn(Schedulers.boundedElastic())
//                                    .map(vv -> user);
                    }
            ).subscribeOn(Schedulers.boundedElastic());
        } else return this.userService.findById(mid.substring(1)); //email
    }

    public Flux<User> getUsers(List<String> ids) {
        return this.userService.findAllByIds(ids);
    }

    public Mono<Boolean> saveUser(Mono<User> body) {
        return body.flatMap(this.userService::save).subscribeOn(Schedulers.boundedElastic())
                .then(Mono.just(true));
    }

    public Mono<Boolean> deleteUser(Mono<User> body) {
        return body.flatMap(user -> this.userService.save(User.from(user).withEnabled(false).build())).subscribeOn(Schedulers.boundedElastic())
                .then(Mono.just(true));
    }

    public Mono<Boolean> blockUser(Mono<UserTag> body) {
        return body.flatMap(userTag -> this.userService.findById(userTag.getId()).map(user -> Tuples.of(user, userTag))).flatMap(tuple -> {
            List<String> list = new ArrayList<String>(tuple.getT1().getBlocked());
            if (tuple.getT2().getTag().equals("0")) {
                list.add(tuple.getT2().getEmail());
            } else {
                list.remove(tuple.getT2().getEmail());
            }
            return this.userService.save(User.from(tuple.getT1()).withBlocked(list).build()).subscribeOn(Schedulers.boundedElastic());
        }).then(Mono.just(true));
    }

    public Mono<Boolean> clearNews(Mono<String> id) {
        return id.flatMap(this.newsService::findById)
                .flatMap(news -> {
                    this.kafkaSender.send(NewsStreams.NEWS_OUT, News.from(news).withClean(true).withArrest(false).build(), news.getId().toHexString().getBytes(), true).subscribeOn(Schedulers.boundedElastic());
                    return Mono.just(true);
                });
    }

    public Flux<NewsPayload> getNewsByTopicsIn(String id) {
        return this.newsService.findAllByTopicsPart(id);
    }

    public Flux<User> getUsersByUsernameIn(String id) {
        return this.userService.findAllByUsernamePart(id);
    }

    public Mono<Boolean> saveNewsComments(Mono<CommentsFeed> body) {
        return body.flatMap(commentsFeed -> this.kafkaSender.send("comments-topics", commentsFeed, commentsFeed.getNewsId().getBytes(), false).subscribeOn(Schedulers.boundedElastic()));
    }

    public Mono<Boolean> partitionMoney(Mono<String> para) {
        // String key = String.valueOf(new Date().getTime());
        return this.getAuthUser().flatMap(user -> para.map(pa -> Tuples.of(user, pa)))
                .flatMap(tuple -> this.kafkaSender.send(partitioncomTopics, new PartitionCommand(tuple.getT1().getId().toHexString(), tuple.getT2()), tuple.getT1().getId().toByteArray(), false).subscribeOn(Schedulers.boundedElastic()));
    }

    //    public Mono<Boolean> getBalanceTotal(String id) {
//        String key = String.valueOf(new Date().getTime());
//        return this.kafkaSender.send(balancecomTopics, new BalanceCommand(key, id), key.getBytes(), false).subscribeOn(Schedulers.boundedElastic());}
//    public Mono<Boolean> getUserHistory(String id) {
//        String key = String.valueOf(new Date().getTime());
//        return this.kafkaSender.send(usersHistoriesTopics, new HistoryCommand(key, id), key.getBytes(), false).subscribeOn(Schedulers.boundedElastic());}
//    public Mono<Boolean> usersCheckout(Mono<String> keyMono) {
//        String key = String.valueOf(new Date().getTime());
//        return keyMono.flatMap(val -> this.kafkaSender.send(checkoutTopics, new CheckoutCommand(key, val), key.getBytes(), false).subscribeOn(Schedulers.boundedElastic()));}
    public Mono<Boolean> handlePayments(Mono<List<String>> pa) {
        String key = String.valueOf(new Date().getTime());
        return pa.flatMap(val -> this.kafkaSender.send(paymentcomTopics, new PaymentCommand(key, val), key.getBytes(), false).subscribeOn(Schedulers.boundedElastic()));
    }
}
