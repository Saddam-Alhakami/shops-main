package com.shops.dreamshops.security;

import com.shops.dreamshops.model.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShopUserDetails implements UserDetails {

    private long id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> authorities;

    /**
     * Builds a ShopUserDetails instance from a User entity.
     *
     * @param user The User entity to build the ShopUserDetails from
     * @return The ShopUserDetails instance
     */
    public static ShopUserDetails BuildUserDetails(User user) {
        // Convert the user's roles to granted authorities
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        // Return a new instance of ShopUserDetails with the given user information and roles
        return new ShopUserDetails(user.getId(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Return the roles/authorities associated with the user
    }

    @Override
    public String getPassword() {
        return password; // Return the user's password (hashed)
    }

    @Override
    public String getUsername() {
        return email; // Return the user's email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assume the account is not expired (can be customized)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assume the account is not locked (can be customized)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assume the credentials (password) are not expired (can be customized)
    }

    @Override
    public boolean isEnabled() {
        return true; // Assume the account is enabled (can be customized)
    }
}
