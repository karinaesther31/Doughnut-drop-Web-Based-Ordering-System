package finalProject.doughnut_drop.Config;

import finalProject.doughnut_drop.entity.userAccount;
import finalProject.doughnut_drop.repository.userAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(userAccountRepository userRepository) {
        return username -> {
            userAccount account = userRepository.findAll().stream()
                    .filter(u -> u.getUsername() != null && username.equalsIgnoreCase(u.getUsername()))
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            String rawRole = account.getRole() != null ? account.getRole().trim() : "USER";
            String roleName = rawRole.startsWith("ROLE_") ? rawRole.substring(5) : rawRole;

            return User.builder()
                    .username(account.getUsername())
                    .password(resolvePassword(account.getPassword()))
                    .roles(roleName)
                    .build();
        };
    }

    private String resolvePassword(String password) {
        // Customer accounts may have null passwords; prefix "{noop}" when no BCrypt hash exists
        if (password == null || password.isBlank()) {
            return "{noop}";
        }
        if (password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$")
                || password.startsWith("{")) {
            return password;
        }
        return "{noop}" + password;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/auth/**", "/admin/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/doughnuts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/doughnuts/*/restock").hasRole("ADMIN")
                        .requestMatchers("/api/orders/**").permitAll()
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}