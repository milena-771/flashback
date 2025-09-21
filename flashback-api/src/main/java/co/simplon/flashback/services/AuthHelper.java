package co.simplon.flashback.services;

import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class AuthHelper {

	private final Logger LOG = LogManager.getLogger(AuthHelper.class);

	private final String issuer;

	private final long expiration;

	private final Algorithm algorithm;

	private final PasswordEncoder encoder;

	public AuthHelper(Builder builder) {
		this.issuer = builder.issuer;
		this.algorithm = builder.algorithm;
		this.encoder = builder.passwordEncoder;
		this.expiration = builder.expiration;
	}

	public String encode(String password) {
		try {
			LOG.info("START >>> encode");
			return encoder.encode(password);
		} finally {
			LOG.info("END <<< encode");
		}
	}

	public Boolean matches(String candidate, String hash) {
		try {
			LOG.info("START >>> matches");
			return encoder.matches(candidate, hash);
		} finally {
			LOG.info("END <<< matches");
		}
	}

	public String createJWT(String role, String id) {
		try {
			LOG.info("START >>> createJWT");
			Instant now = Instant.now();
			Instant expirationTime = now.plusSeconds(expiration);
			var jwt = JWT.create().withIssuer(issuer).withSubject(id).withIssuedAt(now);
			if (expiration != -1) {
				jwt = jwt.withExpiresAt(expirationTime);
			}
			String createJwt = jwt.withClaim("role", role).sign(algorithm);
			return createJwt;
		} finally {
			LOG.info("END <<< createJWT");
		}
	}

	public String refreshJWT(String role, String id, Long refreshExpirationTime) {
		try {
			LOG.info("START >>> refreshJWT");
			Instant now = Instant.now();
			Instant expirationTime = now.plusSeconds(refreshExpirationTime);
			return JWT.create().withIssuer(issuer).withSubject(id).withIssuedAt(now)
					.withExpiresAt(expirationTime).withClaim("role", role).sign(algorithm);
		} finally {
			LOG.info("END <<< refreshJWT");
		}
	}

	public static class Builder {
		private String issuer;
		private long expiration;
		private Algorithm algorithm;
		private PasswordEncoder passwordEncoder;

		public Builder() {
		}

		public Builder issuer(String issuer) {
			this.issuer = issuer;
			return this;
		}

		public Builder expiration(long expiration) {
			this.expiration = expiration;
			return this;
		}

		public Builder algorithm(Algorithm algorithm) {
			this.algorithm = algorithm;
			return this;
		}

		public Builder passwordEncoder(PasswordEncoder passwordEncoder) {
			this.passwordEncoder = passwordEncoder;
			return this;
		}

		public AuthHelper build() {
			return new AuthHelper(this);
		}
	}

}
