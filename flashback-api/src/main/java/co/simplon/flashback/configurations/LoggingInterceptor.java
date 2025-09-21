package co.simplon.flashback.configurations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

	private final Logger LOG = LogManager.getLogger(LoggingInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		String endpoint = request.getRequestURI();
		String method = request.getMethod();

		String userId = "anonymous";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if ((auth != null) && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
			userId = auth.getName();
		}

		LOG.info("[{}] request [{} {}]", userId, method, endpoint);
		return true;
	}
}
