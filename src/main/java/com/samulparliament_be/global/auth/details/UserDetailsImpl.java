package com.samulparliament_be.global.auth.details;

import com.samulparliament_be.domain.users.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return null; // OAuth/JWT라서 비밀번호 없음
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 사용 기간이 만료되지 않았음 (기간 제한 정책 없음)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠겨 있지 않음 (정지/차단 기능 미구현)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 인증 정보(비밀번호 등)가 만료되지 않음 (JWT/OAuth라 사용 안 함)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화 상태임 (탈퇴/비활성 계정 구분 없음)
    }
}