package com.busra.connecting.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = User.Builder.class)

public class User implements UserDetails {

    @JsonSerialize(using = ToStringSerializer.class)
    private final ObjectId id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final String email;
    private final Long contentsCount;
    private final List<String> roles;
    private final List<String> tags;
    private final List<String> users;
    private final List<String> followers;
    private final List<String> blocked;
    private final boolean enabled;
    private final Date date;
    private final String image;
    private final String summary;
    private final List<Integer> mediaParts;
    private final List<String> ipAddress;
    private final String iban;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(
            ObjectId id,
            String username,
            String firstname,
            String lastname,
            String email,
            String password,
            Long contentsCount,
            List<String> roles,
            List<String> tags,
            List<String> users,
            List<String> followers,
            List<String> blocked, String image,
            boolean enabled,
            Date date,
            List<Integer> mediaParts,
            String summary,
            List<String> ipAddress, String iban) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.blocked = blocked;
        this.enabled = enabled;
        this.date = date;
        this.tags = tags;
        this.users = users;
        this.contentsCount = contentsCount;
        this.followers = followers;
        this.image = image;
        this.mediaParts = mediaParts;
        this.summary = summary;
        this.ipAddress = ipAddress;
        this.iban = iban;
    }

    public ObjectId getId() {
        return id;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public List<String> getUsers() {
        return this.users;
    }

    public List<Integer> getMediaParts() {
        return this.mediaParts;
    }

    public List<String> getBlocked() {
        return blocked;
    }

    public List<String> getIpAddress() {
        return ipAddress;
    }

    public String getIban() {
        return iban;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roles != null) {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(this.roles.size());
            for (String role : this.roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            return authorities;
        } else return null;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public Date getDate() {
        return date;
    }

    public Long getContentsCount() {
        return contentsCount;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public String getImage() {
        return image;
    }

    public static Builder of() {
        return new Builder();
    }

    public static Builder of(ObjectId id) {
        return new Builder(id);
    }

    public static Builder from(User user) {
        final Builder builder = new Builder();
        builder.id = user.id;
        builder.username = user.username;
        builder.firstname = user.firstname;
        builder.lastname = user.lastname;
        builder.password = user.password;
        builder.date = user.date;
        builder.mediaParts = user.mediaParts;
        builder.roles = user.roles;
        builder.tags = user.tags;
        builder.users = user.users;
        builder.followers = user.followers;
        builder.email = user.email;
        builder.contentsCount = user.contentsCount;
        builder.summary = user.summary;
        builder.enabled = user.enabled;
        builder.iban = user.iban;
        builder.blocked = user.blocked;
        builder.ipAddress = user.ipAddress;
        builder.image= user.image;
        return builder;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", contentsCount=" + contentsCount +
                ", roles=" + roles +
                ", tags=" + tags +
                ", users=" + users +
                ", followers=" + followers +
                ", blocked=" + blocked +
                ", enabled=" + enabled +
                ", date=" + date +
                ", image='" + image + '\'' +
                ", summary='" + summary + '\'' +
                ", mediaParts=" + mediaParts +
                ", ipAddress=" + ipAddress +
                ", iban='" + iban + '\'' +
                '}';
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static final class Builder {

        private ObjectId id;
        private String username;
        private String firstname;
        private String lastname;
        private String password;
        private String email;
        private Long contentsCount;
        private List<String> roles;
        private List<String> tags;
        private List<String> users;
        private List<String> followers;
        private boolean enabled;
        private Date date;
        private String image;
        private List<Integer> mediaParts;
        private String summary;
        private List<String> ipAddress;
        private String iban;
        private List<String> blocked;

        public Builder() {
        }

        public Builder(ObjectId id) {
            this.id = id;
        }

        public Builder withId(ObjectId id) {
            this.id = id;
            return this;
        }

        public Builder withUsername(String ownerId) {
            this.username = ownerId;
            return this;
        }

        public Builder withFirstname(String topic) {
            this.firstname = topic;
            return this;
        }

        public Builder withLastname(String thumbnail) {
            this.lastname = thumbnail;
            return this;
        }

        public Builder withPassword(String summary) {
            this.password = summary;
            return this;
        }

        public Builder withDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder withMediaParts(List<Integer> mediaParts) {
            this.mediaParts = mediaParts;
            return this;
        }

        public Builder withRoles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder withTags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder withUsers(List<String> tags) {
            this.users = tags;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withFollowers(List<String> followers) {
            this.followers = followers;
            return this;
        }

        public Builder withBlocked(List<String> blocked) {
            this.blocked = blocked;
            return this;
        }

        public Builder withIpAddress(List<String> ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder withIban(String iban) {
            this.iban = iban;
            return this;
        }

        public Builder withEmail(String highlights) {
            this.email = highlights;
            return this;
        }

        public Builder withContentsCount(Long count) {
            this.contentsCount = count;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withImage(String image) {
            this.image=image;
            return this;
        }
        public User build() {
            return new User(id,
                    username,
                    firstname,
                    lastname,
                    email,
                    password,
                    contentsCount,
                    roles,
                    tags,
                    users,
                    followers,
                    blocked, image,
                    enabled,
                    date,
                    mediaParts,
                    summary, ipAddress, iban);
        }
    }
}
