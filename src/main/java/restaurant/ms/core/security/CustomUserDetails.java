package restaurant.ms.core.security;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final JwtUser jwtUser;

    public CustomUserDetails(JwtUser jwtUser) {

        this.jwtUser = jwtUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        GrantedAuthority authority = new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return jwtUser.getUserRole();
            }
        };

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return jwtUser.getPassword();
    }

    @Override
    public String getUsername() {
        return jwtUser.getUsername();
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

    public JwtUser getUser() {
        return jwtUser;
    }
}
