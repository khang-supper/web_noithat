// package project.spring.SecurityConfig;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfigconten extends WebSecurityConfiguration {

//     @Bean
//      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//          http.authorizeRequests().requestMatchers("/admin/**").hasRole("1")
//                  .requestMatchers("/**").hasRole("0").and().formLogin();
//          return http.build();
//      }

//     @Bean
//     public UserDetailsService userDetailsService() {
//         UserDetails user = User.withDefaultPasswordEncoder()
//             .username("user")
//             .password("password")
//             .roles("0")
//             .build();
//         UserDetails admin = User.withDefaultPasswordEncoder()
//             .username("admin")
//             .password("password")
//             .roles("1", "0")
//             .build();
//         return new InMemoryUserDetailsManager(user, admin);
//     }
// }
