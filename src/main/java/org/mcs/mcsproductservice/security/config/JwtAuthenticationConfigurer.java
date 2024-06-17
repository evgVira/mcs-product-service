package org.mcs.mcsproductservice.security.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mcs.mcsproductservice.security.JwtAuthenticationConverter;
import org.mcs.mcsproductservice.security.RequestJwtTokenFilter;
import org.mcs.mcsproductservice.security.UserEntityService;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private final RequestJwtTokenFilter requestJwtTokenFilter;

    private final UserEntityService userEntityService;

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        CsrfConfigurer csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/token", HttpMethod.POST.name()));
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {

        var jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class), jwtAuthenticationConverter);
        jwtAuthenticationFilter.setSuccessHandler(((request, response, authentication) -> {
            CsrfFilter.skipRequest(request);
        }));
        jwtAuthenticationFilter.setFailureHandler((request, response, exception) -> {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        });

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(userEntityService);

        builder
                .addFilterAfter(requestJwtTokenFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}
