package lk.ijse.offbeatceylon.config;

import lk.ijse.offbeatceylon.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/authenticate/**",
                                "/api/v1/user/update/**",
                                "/api/v1/user/delete/**",
                                "/api/v1/user/delete/${email}",
                                "api/v1/user/getAll",
                                "/api/v1/user/register",
                                "/api/v1/auth/refreshToken",
                                "/api/v1/category/getTotalCategories",
                                "/api/v1/addPlace/getPlacesCount",

                                "/api/v1/auth/resend-otp",
                                "/api/v1/auth/forgot-password",
                                "/api/v1/auth/verify-otp",
                                "/api/v1/auth/reset-password",

                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/api/v1/admin/test1",
                                "/api/v1/admin/test2",
                                "/api/v1/addPlace/save",
                                "/api/v1/img/upload/**",
                                "/api/v1/addPlace/**",
                                "/api/v1/addPlace/getAllByName/${placeName}*",
                                "/api/v1/addPlace/update/${placeId}",
                                "/api/v1/addPlace/delete/${placeId}",
                                "/api/v1/category/**",
                                "api/v1/category/getAll",
                                "/api/v1/category/save",
                                "/api/v1/category/getById/${categoryId}",
                                "/api/v1/category/update/${categoryId}",
                                "/api/v1/category/delete/${categoryId}",
                                "/api/v1/category/resources/static/imageFolder/{filename}",
                                "/api/v1/category/resources/static/imageFolder",
                                "/api/v1/upload/img",
                                "/api/v1/uploads/",
                                "/api/v1/uploads/**",
                                "/api/v1/addPlace/getAllByCategory/${categoryName}",
                                "/api/v1/addPlace/getAllByDistrict/**",





                                "/swagger-ui.html").permitAll()
                        .requestMatchers("/uploads/", "/api/v1/uploads/", "/uploads/images/").permitAll()
//                        .requestMatchers("/uploads/"),"/api/v1/uploads/","/uploads/placeImages/").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}