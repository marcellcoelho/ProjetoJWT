package br.com.marcell.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtFilter implements Filter {

	private static String SECRET_KEY = "OUTRA";
	private static String PATH_SERVLET_REST = "/rest";
	private static String PATH_LOGIN = "/login";
	private static String METHOD_POST = "POST";

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		if (!isPathLogin(request)) {
			verificarAutorization(request);
		}
		chain.doFilter(req, res);
	}

	private boolean isPathLogin(final HttpServletRequest request) {
		return request.getServletPath().equalsIgnoreCase(PATH_SERVLET_REST) && request.getPathInfo().equalsIgnoreCase(PATH_LOGIN)
				&& request.getMethod().equalsIgnoreCase(METHOD_POST);
	}

	private void verificarAutorization(HttpServletRequest request) throws ServletException {
		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new ServletException("Autorização ausente ou inválido.");
		}
		verificarToken(request, authHeader.substring(7));
	}

	private void verificarToken(HttpServletRequest request, String token) throws ServletException {
		try {
			final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
			request.setAttribute("claims", claims);
		} catch (SignatureException e) {
			throw new ServletException("Token inválido.");
		} catch (ExpiredJwtException e) {
			throw new ServletException("Token expirou.");
		} catch (UnsupportedJwtException e) {
			throw new ServletException("Unsupported.");
		} catch (MalformedJwtException e) {
			throw new ServletException("Malformed.");
		} catch (IllegalArgumentException e) {
			throw new ServletException("IllegalArgument.");
		}
	}
}
