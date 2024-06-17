package org.mcs.mcsproductservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mcs.mcsproductservice.security.token.model.Token;
import org.mcs.mcsproductservice.security.token.model.TokensResponse;
import org.mcs.mcsproductservice.security.token.tokenFactory.AccessTokenFactory;
import org.mcs.mcsproductservice.security.token.tokenFactory.RefreshTokenFactory;
import org.mcs.mcsproductservice.security.token.tokenStringSerializer.AccessTokenStringSerializer;
import org.mcs.mcsproductservice.security.token.tokenStringSerializer.RefreshTokenStringSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@Component
public class RequestJwtTokenFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/token", HttpMethod.POST.name());

    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private final RefreshTokenFactory refreshTokenFactory;

    private final AccessTokenFactory accessTokenFactory;

    private final AccessTokenStringSerializer accessTokenStringSerializer;

    private final RefreshTokenStringSerializer refreshTokenStringSerializer;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(requestMatcher.matches(request)){
            if(securityContextRepository.containsContext(request)){

                SecurityContext context = securityContextRepository.loadDeferredContext(request).get();

                if(context != null && !(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)){

                    Authentication authentication = context.getAuthentication();
                    Token refreshToken = refreshTokenFactory.apply(authentication);
                    Token accessToken = accessTokenFactory.apply(refreshToken);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    TokensResponse tokensResponse = TokensResponse.builder()
                            .accessToken(accessTokenStringSerializer.apply(accessToken))
                            .refreshToken(refreshTokenStringSerializer.apply(refreshToken))
                            .build();

                    objectMapper.writeValue(response.getWriter(), tokensResponse);

                    return;
                }
            }
            throw new AccessDeniedException("User must be authenticated");
        }
        filterChain.doFilter(request, response);
    }
}
