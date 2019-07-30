package com.github.pallocchi.positions.config;

import com.github.pallocchi.positions.auth.JwtAuthenticationFilter;
import com.github.pallocchi.positions.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtConfig config;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
            .and()
            .addFilterAfter(new JwtAuthenticationFilter(config), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/clients*").authenticated()
            .antMatchers("/providers*").authenticated()
            .antMatchers("/positions*").authenticated()
            .anyRequest().permitAll();
    }

}
