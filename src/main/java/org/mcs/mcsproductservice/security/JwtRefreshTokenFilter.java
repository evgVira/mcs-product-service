package org.mcs.mcsproductservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mcs.mcsproductservice.security.token.model.Token;
import org.mcs.mcsproductservice.security.token.model.TokenUser;
import org.mcs.mcsproductservice.security.token.model.TokensResponse;
import org.mcs.mcsproductservice.security.token.tokenFactory.AccessTokenFactory;
import org.mcs.mcsproductservice.security.token.tokenStringSerializer.AccessTokenStringSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/token/refresh", HttpMethod.POST.name());

    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private final AccessTokenFactory accessTokenFactory;

    private final AccessTokenStringSerializer accessTokenStringSerializer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            if (securityContextRepository.containsContext(request)) {

                var context = securityContextRepository.loadDeferredContext(request).get();

                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken && context.getAuthentication().getPrincipal() instanceof TokenUser tokenUser &&
                        context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_REFRESH"))) {

                    Token requestToken = tokenUser.getToken();

                    Token accessToken = accessTokenFactory.apply(requestToken);

                    if (accessToken != null) {

                        String accessTokenToString = accessTokenStringSerializer.apply(accessToken);

                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                        TokensResponse tokensResponse = TokensResponse.builder()
                                .accessToken(accessTokenToString)
                                .refreshToken(null)
                                .build();

                        objectMapper.writeValue(response.getWriter(), tokensResponse);
                        return;
                    }
                }
            }
            throw new AccessDeniedException("User must be authenticated");
        }
        filterChain.doFilter(request, response);
    }
}
