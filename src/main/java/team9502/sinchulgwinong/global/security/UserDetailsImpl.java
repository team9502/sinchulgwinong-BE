package team9502.sinchulgwinong.global.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;

import java.util.Collection;

import static team9502.sinchulgwinong.global.exception.ErrorCode.INVALID_USER_TYPE;

public class UserDetailsImpl implements UserDetails {

    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String userType;
    private final Object user;

    public UserDetailsImpl(String email, String password, Collection<? extends GrantedAuthority> authorities, String userType, Object user) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.userType = userType;
        this.user = user;
    }

    public Long getUserId() {
        if (user instanceof User) {
            return ((User) user).getUserId();
        }
        throw new ApiException(INVALID_USER_TYPE);
    }

    public Long getCpUserId() {
        if (user instanceof CompanyUser) {
            return ((CompanyUser) user).getCpUserId();
        }
        throw new ApiException(INVALID_USER_TYPE);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public Object getUser() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
