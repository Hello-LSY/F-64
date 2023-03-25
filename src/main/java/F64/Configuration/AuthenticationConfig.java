//package F64.Configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
//
//@Configuration
//@EnableWebSecurity
//public class AuthenticationConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        return httpSecurity
//                .httpBasic().disable()
//                .csrf().disable()
//                .cors()
//                .and()
//                .headers()
//                .frameOptions().sameOrigin()
//                .and()
//                .authorizeRequests()
//                //인증필요한거
//                .antMatchers(HttpMethod.POST, "/board/**").authenticated()
//                //안필요한거
//                .antMatchers("/login", "/").permitAll()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                //.addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
//                .build()
//                ;
//    }
//}
