package com.fours.tolevelup.model;

import com.fours.tolevelup.model.entity.ThemeExp;
import com.fours.tolevelup.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserDTO implements UserDetails {
    private String id;
    private String password;
    private String name;
    private String email;
    private int level;
    private String intro;
    private UserRole role;
    private Date registeredAt;
    private int rank;

    public static UserDTO fromEntity(User entity){
        return new UserDTO(
                entity.getId(),
                entity.getPassword(),
                entity.getName(),
                entity.getEmail(),
                entity.getLevel(),
                entity.getIntro(),
                entity.getRole(),
                entity.getRegisteredAt(),
                entity.getRank()
        );
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class publicUserData{
        private String userId;
        private String name;
        private int level;
        private String intro;

        public static publicUserData fromUser(User user) {
            return new publicUserData(
                    user.getId(),
                    user.getName(),
                    user.getLevel(),
                    user.getIntro()
            );
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class feedUserData{
        private String userId;
        private String name;

        public static feedUserData fromUser(User user){
            return new feedUserData(
                    user.getId(),
                    user.getName()
            );
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().name()));
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
