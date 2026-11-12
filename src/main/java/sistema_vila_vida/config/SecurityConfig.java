package sistema_vila_vida.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
    .requestMatchers(
        "/",
        "/index.html",
        "/login.html",
        "/imagens/**"
    ).permitAll()
    .anyRequest().authenticated()
)
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
    response.setStatus(303);
    response.setHeader("Location", "/painel.html");
})
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login.html")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        String usuario = System.getenv("VILAVIDA_LOGIN_USUARIO");
        String senha = System.getenv("VILAVIDA_LOGIN_SENHA");

        if (usuario == null || usuario.isBlank()
                || senha == null || senha.isBlank()) {
            throw new IllegalStateException(
                "Configure VILAVIDA_LOGIN_USUARIO e VILAVIDA_LOGIN_SENHA");
        }

        return new InMemoryUserDetailsManager(
            User.withUsername(usuario)
                .password(passwordEncoder.encode(senha))
                .roles("ADMIN")
                .build()
        );
    }
}
