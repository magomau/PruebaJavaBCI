package com.nttdata.apiusuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("deprecation")
@Configuration
public class WebSecurityConfig {
/**
 * Crear el usuario y contraseña del Sprint Security
 * @return
 */
	@Bean
	 UserDetailsService userDetailsService() {
		
		var user =User.withUsername("user")
				.password("contrasena")
				.roles("read")
				.build();
		
		return new InMemoryUserDetailsManager(user);
		}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();// solo de prueba
	}
	
/**
 * Metodo de seguridad encargado de adminstrar las seguridad en las rutas de la API
 * @param http
 * @return
 * @throws Exception
 */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/usuarios/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/usuarios/login")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/usuarios/update")).permitAll()
                                .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().disable())
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
//                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/usuarios/**"))
//                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/usuarios/login")));
                .csrf(csrf -> csrf.disable());
         
        return http.build();
    }
    
}
