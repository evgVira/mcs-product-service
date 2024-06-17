package org.mcs.mcsproductservice.security;

import lombok.RequiredArgsConstructor;
import org.mcs.mcsproductservice.model.UserAuthority;
import org.mcs.mcsproductservice.model.UserEntity;
import org.mcs.mcsproductservice.repository.UserAuthorityRepository;
import org.mcs.mcsproductservice.repository.UserEntityRepository;
import org.mcs.mcsproductservice.security.token.model.Token;
import org.mcs.mcsproductservice.security.token.model.TokenUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEntityService implements UserDetailsService, AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final UserEntityRepository userEntityRepository;

    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if(authenticationToken.getPrincipal() instanceof Token token){
            return new TokenUser(token.getSubject(), "noopassword", true, true, token.getExpiresAt().toInstant().isAfter(Instant.now()), true, token.getAuthorities().stream()
                    .map(SimpleGrantedAuthority::new).toList(), token);
        }
        throw new UsernameNotFoundException("User with name %s not found".formatted(authenticationToken.getName()));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findUserEntityByUsername(username);
        UserAuthority userAuthority = userAuthorityRepository.getUserAuthoritiesByUserId(user.getId());
        return new User(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority(userAuthority.getAuthority())));
    }
}
