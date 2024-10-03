package longrun.springsecuritysessionlogin.config;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import longrun.springsecuritysessionlogin.handler.CustomAuthenticationFailureHandler;
import longrun.springsecuritysessionlogin.handler.CustomAuthenticationSuccessHandler;
import longrun.springsecuritysessionlogin.repository.UserRepository;
import longrun.springsecuritysessionlogin.security.CustomAuthenticationFilter;
import longrun.springsecuritysessionlogin.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        CustomAuthenticationFilter authenticationFilter = customAuthenticationFilter(authenticationManager);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                        .configurationSource(CorsConfig.corsConfigurationSource()))
                .authorizeHttpRequests((authorize)-> authorize
                        .requestMatchers("/login","/member/sign-up").permitAll()
                        .anyRequest().authenticated())
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/member/login")
//                        .usernameParameter("userId")
//                        .passwordParameter("password")
//                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1) //최대 동시 접속 세션 1개
                        .maxSessionsPreventsLogin(true)) //동시 로그인 차단
                .logout(logout->logout
                        .logoutUrl("/member/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // 상태 코드 200
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
//                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterAfter(authenticationFilter, LogoutFilter.class);


        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public CustomAuthenticationFailureHandler authenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception{
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager);
        customAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        customAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        SecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
        customAuthenticationFilter.setSecurityContextRepository(contextRepository);
        return customAuthenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
