package com.busra.connecting.controller;

import com.busra.connecting.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Arrays;
import java.util.List;

@Component
public class RouterComponent {
   // private static final Logger LOGGER = LoggerFactory.getLogger(RouterComponent.class);

    private final NewsHandler newsHandler;

    @Autowired
    public RouterComponent(NewsHandler newsHandler) {
        this.newsHandler = newsHandler;
    }

    @Bean
    public RouterFunction<?> landingRouterFunction() {
//        SelfSignedCertificate ssc = new SelfSignedCertificate();
//        SslContext sslServer = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
//                .build();
//        HttpServer.create().secure(ssl -> ssl.sslContext(sslServer))
//                .route(routes ->
//                                routes.ws("/ws",
//                                        (wsInbound, wsOutbound) -> wsOutbound.send(wsInbound.receive().retain())))
//                .bindNow();
        return RouterFunctions.nest(RequestPredicates.path("/api"),
                RouterFunctions.route(RequestPredicates.GET("/images"),
                        request -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(newsHandler.findAllNames(), News.class))

//                        .andRoute(RequestPredicates.POST("/image/save").and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)),
//                                request -> ServerResponse.ok()
//                                        .body(imageHandler.saveNews(request.bodyToFlux(Part.class)), Boolean.class))
                        .andRoute(RequestPredicates.POST("/rest/news/save"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.saveNewsMetadata(request.body(BodyExtractors.toMono(NewsFeed.class))), Boolean.class))
                        .andRoute(RequestPredicates.POST("/rest/news/comments"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.saveNewsComments(request.body(BodyExtractors.toMono(CommentsFeed.class))), Boolean.class))

//                        .andRoute(RequestPredicates.POST("/rest/news/sendreport/{user}"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.sendReport(request.body(BodyExtractors.toMono(NewsPayload.class)), request.pathVariable("user")), Boolean.class))

//                        .andRoute(RequestPredicates.PATCH("/rest/user/media/save").and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.saveAveragePhotos(request.bodyToFlux(Part.class)), Boolean.class))
                        .andRoute(RequestPredicates.PATCH("/rest/news/clearNews"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.clearNews(request.bodyToMono(String.class)), Boolean.class))
//                        .andRoute(RequestPredicates.GET("/rest/storage/{fileName}"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.getSignedUrl(request.pathVariable("fileName")), URL.class))
                        .andRoute(RequestPredicates.GET("/rest/user/{id}/{random}/{fed}"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.getUserById(request.pathVariable("id"), request.pathVariable("random"), request.pathVariable("fed")), User.class))
                        .andRoute(RequestPredicates.GET("/rest/start/user/{id}/{random}"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.getUserByStart(request.pathVariable("id"), request.pathVariable("random")), User.class))
                        .andRoute(RequestPredicates.GET("/rest/users/get/{id}/{email}/{random}"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.getUserByEmail(request.pathVariable("id"),request.pathVariable("email"), request.pathVariable("random")), User.class))
//                        .andRoute(RequestPredicates.PATCH("/rest/user/media/save"),//.and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.saveUserMedia(request.bodyToFlux(Part.class)), Boolean.class))
                        .andRoute(RequestPredicates.PUT("/rest/users/addtag/{random}"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.addTag(request.body(BodyExtractors.toMono(UserTag.class)), request.pathVariable("random")), Boolean.class))
                        .andRoute(RequestPredicates.PUT("/rest/users/removetag"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.removeTag(request.body(BodyExtractors.toMono(UserTag.class))), Boolean.class))
                        .andRoute(RequestPredicates.POST("/rest/userregis/save"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.saveUser(request.body(BodyExtractors.toMono(User.class))), Boolean.class))
                        .andRoute(RequestPredicates.POST("/rest/userregis/block"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.blockUser(request.body(BodyExtractors.toMono(UserTag.class))), Boolean.class))
                        //         .andRoute(RequestPredicates.PUT("/rest/description"),
                        //        request -> ServerResponse.ok()
                        //                .body(newsHandler.saveUserDesc(request.body(BodyExtractors.toMono(UserDesc.class))), Boolean.class))
                        // .andRoute(RequestPredicates.PUT("/rest/users/{id}/contentcount"),
                        //         request -> ServerResponse.ok()
                        //                 .body(newsHandler.increaseContent(request.body(BodyExtractors.toMono(String.class))), Boolean.class))
                        .andRoute(RequestPredicates.GET("/rest/users/getAll/{ids}"),
                                request -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(newsHandler.getUsers(Arrays.asList(request.pathVariable("ids").split(","))), User.class))

//                        .andRoute(RequestPredicates.GET("/image/get/{id}"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.fetchImage(request.pathVariable("id")), byte[].class))
//                        .andRoute(RequestPredicates.DELETE("/image/delete/{id}"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.removeImage(request.pathVariable("id")), Boolean.class))
//                        .andRoute(RequestPredicates.DELETE("/rest/news/delete/{id}"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.removeNews(request.pathVariable("id")), Boolean.class))

//                        .andRoute(RequestPredicates.GET("/rest/news/list"),
//                                request -> ServerResponse.ok()
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .body(newsHandler.list(), NewsPayload.class))
/*
                        .andRoute(RequestPredicates.DELETE("/rest/news/removeNewsImages/{id}"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.removeNewsAfter(request.pathVariable("id")), Boolean.class))
                        .andRoute(RequestPredicates.GET("/rest/news/topList/{ids}"),
                                request -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(newsHandler.getTopList(Arrays.asList(request.pathVariable("ids").split(","))), News.class))
                        .andRoute(RequestPredicates.GET("/rest/news/topReportedList/{ids}"),
                                request -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(newsHandler.getReportedTopList(Arrays.asList(request.pathVariable("ids").split(","))), News.class))

                        .andRoute(RequestPredicates.GET("/rest/news/getNewsByOwnerId/{id}"),
                                request -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(newsHandler.getNewsByOwnerId(request.pathVariable("id")), News.class))*/
//                        .andRoute(RequestPredicates.GET("/rest/news/getNewsByOwnerIds/{ids}"),
//                                request -> ServerResponse.ok()
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .body(newsHandler.getNewsByOwnerIds(Arrays.asList(request.pathVariable("ids").split(","))), NewsPayload.class))
                        .andRoute(RequestPredicates.POST("/rest/userregis/delete"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.deleteUser(request.body(BodyExtractors.toMono(User.class))), Boolean.class))
                        .andRoute(RequestPredicates.GET("/rest/news/search/{id}"),
                                request -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(newsHandler.getNewsByTopicsIn(request.pathVariable("id")), NewsPayload.class))
                        .andRoute(RequestPredicates.GET("/rest/users/search/{id}"),
                                request -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(newsHandler.getUsersByUsernameIn(request.pathVariable("id")), User.class))
//                        .andRoute(RequestPredicates.GET("/chat/room/{chatRoom}/subscribeMessages"),
//                                request -> ServerResponse.ok().header("X-Accel-Buffering", "no")
//                                        .contentType(MediaType.TEXT_EVENT_STREAM)
//                                        .body(newsHandler.subscribeChatMessages(request.pathVariable("chatRoom"), request.headers().header("last-event-id")), new ParameterizedTypeReference<ServerSentEvent<RecordSSE>>() {}))
                        .andRoute(RequestPredicates.GET("/rest/news/setNewsCounts/{id}"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.setNewsCounts(request.pathVariable("id")), Boolean.class))
                        .andRoute(RequestPredicates.GET("/rest/news/get/{id}"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.getNewsById(request.pathVariable("id")), News.class))
                        .andRoute(RequestPredicates.POST("/rest/admin/handlePayments"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.handlePayments(request.bodyToMono(new ParameterizedTypeReference<List<String>>() {})), Boolean.class))
                        .andRoute(RequestPredicates.POST("/rest/admin/partitionMoney"),
                                request -> ServerResponse.ok()
                                        .body(newsHandler.partitionMoney(request.bodyToMono(String.class)), Boolean.class))
//                        .andRoute(RequestPredicates.GET("/rest/admin/hotBalanceRecords"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.hotBalanceRecords(), Boolean.class))
//                        .andRoute(RequestPredicates.GET("/rest/balance/total/{id}"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.getBalanceTotal(request.pathVariable("id")), Boolean.class))
//                        .andRoute(RequestPredicates.GET("/rest/balance/history/{id}"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.getUserHistory(request.pathVariable("id")), Boolean.class))
//                        .andRoute(RequestPredicates.POST("/rest/admin/checkout"),
//                                request -> ServerResponse.ok()
//                                        .body(newsHandler.usersCheckout(request.bodyToMono(String.class)), Boolean.class))

        ).andOther(RouterFunctions.resources("/**", new ClassPathResource("static/")));
    }

}
