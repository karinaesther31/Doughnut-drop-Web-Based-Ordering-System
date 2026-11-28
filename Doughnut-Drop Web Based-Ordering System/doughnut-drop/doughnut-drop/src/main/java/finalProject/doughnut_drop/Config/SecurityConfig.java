package finalProject.doughnut_drop.Config;

import finalProject.doughnut_drop.entity.userAccount;
import finalProject.doughnut_drop.repository.userAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(userAccountRepository userRepository) {
        return username -> {
            userAccount account = userRepository.findAll().stream()
                    .filter(u -> username.trim().equalsIgnoreCase(u.getUsername()))
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            String rawRole = account.getRole() != null ? account.getRole().replace("ROLE_", "") : "USER";
            String encodedPassword = account.getPassword() != null ? account.getPassword() : "";

            return User.withUsername(account.getUsername())
                    .password(encodedPassword)
                    .roles(rawRole)
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        // Static UI files & login page
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/auth/**",
                                "/auth/login.html",
                                "/*.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/assets/**",
                                "/favicon.ico"
                        ).permitAll()

                        // H2 Console & Auth endpoints
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Public catalog, cart, at orders
                        .requestMatchers(HttpMethod.GET, "/api/doughnuts/**").permitAll()
                        .requestMatchers("/api/cart/**").permitAll()
                        .requestMatchers("/api/orders/**").permitAll()

                        // Protected restock para sa ADMIN at STAFF lang
                        .requestMatchers(HttpMethod.POST, "/api/doughnuts/*/restock").hasAnyRole("ADMIN", "STAFF")

                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}