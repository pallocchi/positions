package com.github.pallocchi.positions.config;

import com.github.pallocchi.positions.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
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
            .antMatchers("/clients/**").hasRole("ADMIN")
            .antMatchers("/providers/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/positions").hasRole("CLIENT")
            .antMatchers(HttpMethod.POST, "/positions").hasRole("ADMIN")
            .anyRequest().permitAll();
    }

    @Bean
    public JwtConfig config() {
        return new JwtConfig();
    }

}
