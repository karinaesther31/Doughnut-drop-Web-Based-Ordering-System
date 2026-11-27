package finalProject.doughnut_drop.Config;

import finalProject.doughnut_drop.entity.userAccount;
import finalProject.doughnut_drop.repository.userAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                    .filter(u -> username.equalsIgnoreCase(u.getUsername()))
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            String roleName = account.getRole().startsWith("ROLE_")
                    ? account.getRole().substring(5)
                    : account.getRole();

            String encodedPassword = account.getPassword() != null
                    ? account.getPassword()
                    : "{noop}";

            return User.builder()
                    .username(account.getUsername())
                    .password(encodedPassword)
                    .roles(roleName)
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/doughnuts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/doughnuts/*/restock").hasRole("ADMIN")
                        .requestMatchers("/api/orders/**").permitAll()
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}