package com.example.demo.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomJwtConverter implements Converter<Jwt, CustomJwt> {

    @Override
    public CustomJwt convert(@NonNull Jwt jwt) {
        // Extract claims and authorities as needed
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        // You can also map other information from the Jwt to the custom token
        var customJwt = new CustomJwt(jwt, authorities);
        customJwt.setFirstname(jwt.getClaimAsString("given_name"));
        customJwt.setLastname(jwt.getClaimAsString("family_name"));
        return customJwt;
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        var authorities = new ArrayList<GrantedAuthority>();

        // ... your logic to extract and map the claims to GrantedAuthority ...
        var realm_access = jwt.getClaimAsMap("realm_access");
        if (realm_access != null && realm_access.get("roles") != null) {
            var roles = realm_access.get("roles");
            if (roles instanceof List l) {
                l.forEach(role ->
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
                );
            }
        }

        return authorities;
    }
}