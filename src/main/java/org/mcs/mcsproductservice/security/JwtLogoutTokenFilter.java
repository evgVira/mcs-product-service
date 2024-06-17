package org.mcs.mcsproductservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mcs.mcsproductservice.security.token.model.TokenUser;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class JwtLogoutTokenFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/token/logout", HttpMethod.POST.name());

    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_SCRIPT_FOR_TOKEN_DEACTIVATE = "insert into t_deactivated_token(id, keep_until) values (?, ?)";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(requestMatcher.matches(request)){
            if(securityContextRepository.containsContext(request)){

                var context = securityContextRepository.loadDeferredContext(request).get();

                if(context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken && context.getAuthentication().getPrincipal() instanceof TokenUser tokenUser &&
                context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_LOGOUT"))){
                    jdbcTemplate.update(SQL_SCRIPT_FOR_TOKEN_DEACTIVATE, tokenUser.getToken().getId(), tokenUser.getToken().getExpiresAt());
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
            }
            throw new AccessDeniedException("User must be authenticated");
        }
        filterChain.doFilter(request, response);
    }
}
