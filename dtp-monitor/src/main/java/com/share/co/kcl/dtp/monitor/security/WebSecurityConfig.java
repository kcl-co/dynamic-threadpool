package com.share.co.kcl.dtp.monitor.security;

import com.share.co.kcl.dtp.monitor.security.authentication.DtpMonitorProcessingFilter;
import com.share.co.kcl.dtp.monitor.security.authentication.DtpConsoleProcessingFilter;
import com.share.co.kcl.dtp.monitor.security.authentication.entity.DtpConsoleAuthenticationToken;
import com.share.co.kcl.dtp.monitor.security.authentication.entity.DtpMonitorAuthenticationToken;
import com.share.co.kcl.dtp.monitor.security.authentication.manager.DtpConsoleAuthenticationProvider;
import com.share.co.kcl.dtp.monitor.security.authentication.manager.DtpMonitorAuthenticationProvider;
import com.share.co.kcl.dtp.monitor.security.authentication.service.DtpConsoleAuthenticatedTokenUserService;
import com.share.co.kcl.dtp.monitor.security.authentication.service.DtpMonitorAuthenticatedTokenUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author kcl.co
 * @since 2022/02/19
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationUserDetailsService<DtpConsoleAuthenticationToken> dtpConsoleAuthenticationUserDetailsService;
    private final AuthenticationUserDetailsService<DtpMonitorAuthenticationToken> dtpMonitorAuthenticationUserDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public WebSecurityConfig(
            DtpConsoleAuthenticatedTokenUserService dtpConsoleAuthenticationUserDetailsService,
            DtpMonitorAuthenticatedTokenUserService dtpMonitorAuthenticationUserDetailsService,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler) {
        this.dtpConsoleAuthenticationUserDetailsService = dtpConsoleAuthenticationUserDetailsService;
        this.dtpMonitorAuthenticationUserDetailsService = dtpMonitorAuthenticationUserDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        DtpConsoleAuthenticationProvider dtpConsoleAuthenticationProvider = new DtpConsoleAuthenticationProvider();
        dtpConsoleAuthenticationProvider.setPreAuthenticatedUserDetailsService(this.dtpConsoleAuthenticationUserDetailsService);
        auth.authenticationProvider(dtpConsoleAuthenticationProvider);

        DtpMonitorAuthenticationProvider dtpMonitorAuthenticationProvider = new DtpMonitorAuthenticationProvider();
        dtpMonitorAuthenticationProvider.setPreAuthenticatedUserDetailsService(this.dtpMonitorAuthenticationUserDetailsService);
        auth.authenticationProvider(dtpMonitorAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // ??????
                .cors()
                .and()
                // CSRF
                .csrf().disable()
                // header
                .headers()
                .httpStrictTransportSecurity().disable()
                .frameOptions().disable()
                .and()
                // ?????? anonymous
                .anonymous()
                .principal(0)
                .and()
                .addFilterAt(new DtpConsoleProcessingFilter(authenticationManager()), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAt(new DtpMonitorProcessingFilter(authenticationManager()), AbstractPreAuthenticatedProcessingFilter.class)
                // ????????????
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)
                .accessDeniedHandler(this.accessDeniedHandler)
                // ???????????????
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // ?????????????????????????????????????????????????????????????????????
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
        ;
    }

    /**
     * ????????????
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //???????????????*????????????????????????????????????????????????ip???????????????????????????localhost???8080?????????????????????????????????
        corsConfiguration.addAllowedOrigin("*");
        //header???????????????header????????????????????????token???????????????*?????????token???
        corsConfiguration.addAllowedHeader("*");
        //????????????????????????POST???GET???
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        //???????????????????????????url
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
