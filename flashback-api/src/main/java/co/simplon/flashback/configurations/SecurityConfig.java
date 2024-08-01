package co.simplon.flashback.configurations;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.auth0.jwt.algorithms.Algorithm;

import co.simplon.flashback.services.AuthHelper;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

	@Value("${flashback-api.cors.enabled}")
	private boolean corsEnabled;

	@Value("${flashback-api.auth.rounds}")
	private int rounds;

	@Value("${flashback-api.auth.issuer}")
	private String issuer;

	@Value("${flashback-api.auth.secret}")
	private String secret;

	@Value("${flashback-api.auth.tokenAccessExp}")
	private long tokenAccessExpiration;

	@Bean
	public AuthHelper authHelper() {
		Algorithm algorithm = Algorithm.HMAC256(secret);
		PasswordEncoder encoder = new BCryptPasswordEncoder(rounds);

		return new AuthHelper.Builder().algorithm(algorithm)
				.passwordEncoder(encoder).issuer(issuer)
				.expiration(tokenAccessExpiration).build();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(corsCustomizer()).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers("/sign-up", "/sign-in", "/")
						.permitAll()
						.requestMatchers("/movies/for-search",
								"/movies/by-title", "/movies/by-director",
								"/movies/by-genre", "/favorites/**",
								"/retrospectives/**")
						.hasRole("USER")
						.requestMatchers("/admin/**", "/movies",
								"/movies/for-edit", "/movies/labels",
								"/movies/{id}")
						.hasRole("ADMIN").anyRequest().authenticated())
				.oauth2ResourceServer(
						(oauth2ResourceServer) -> oauth2ResourceServer
								.jwt(Customizer.withDefaults()));
		return http.build();
	}

	private Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer() {
		return corsEnabled ? Customizer.withDefaults() : cors -> cors.disable();
	}

	@Bean
	JwtAuthenticationConverter authenticationConverter() {
		JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
		authoritiesConverter.setAuthoritiesClaimName("role");
		authoritiesConverter.setAuthorityPrefix("ROLE_");
		JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
		authenticationConverter
				.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
		return authenticationConverter;
	}

	@Bean
	JwtDecoder jwtDecoder() throws Exception {
		SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(key).build();
	}

}
