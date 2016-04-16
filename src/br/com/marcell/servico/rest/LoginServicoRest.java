package br.com.marcell.servico.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.projeto.entidade.Cargo;
import br.com.projeto.entidade.Permissao;
import br.com.projeto.entidade.Usuario;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/login")
public class LoginServicoRest {
	
	private static String SECRET_KEY = "MARCELL";

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse login(Usuario usuario) throws ServletException {
		validarUsuario(usuario);
		Usuario usuarioBD = usuarioMock(); //TODO: VALIDA NA BASE;
		
		return new LoginResponse(gerarToken(usuarioBD));
	}
	
	@SuppressWarnings("unused")
    private static class LoginResponse implements Serializable {
		private static final long serialVersionUID = 1L;
		public String token;
        public LoginResponse(final String token) {
            this.token = token;
        }
    }

	private Usuario usuarioMock() {
		Usuario usuarioBD = new Usuario();
		usuarioBD.setNome("Marcell Araújo Coelho Ribeiro Gomes");
		List<Cargo> cargoList =  new ArrayList<>();
		Cargo cargo = new Cargo();
		cargo.setNome("Admin");
		
		List<Permissao> permissaoList = new ArrayList<>();
		Permissao permissao1 = new Permissao();
		permissao1.setNome("UC001");
		permissaoList.add(permissao1);
		
		Permissao permissao2 = new Permissao();
		permissao2.setNome("UC002");
		permissaoList.add(permissao2);
		
		
		cargo.setPermissaoList(permissaoList);
		cargoList.add(cargo);
		usuarioBD.setCargoList(cargoList);
		return usuarioBD;
	}

    private String gerarToken(Usuario usuario) {
		List<String> cargos = new ArrayList<>();
		List<String> permissoes = new ArrayList<>();
		for (Cargo cargo : usuario.getCargoList()) {
			cargos.add(cargo.getNome());
			for (Permissao permissao : cargo.getPermissaoList()) {
				if (!permissoes.contains(permissao.getNome())) {
					permissoes.add(permissao.getNome());
				}
			}}
		JwtBuilder jwtBuilder = Jwts.builder();
		jwtBuilder.setSubject(usuario.getNome());
		jwtBuilder.claim("cargos", cargos);
		jwtBuilder.claim("permissoes", permissoes);
		jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + 3600000));
		jwtBuilder.setIssuedAt(new Date());
		jwtBuilder.signWith(SignatureAlgorithm.HS256, SECRET_KEY);
		return jwtBuilder.compact();
	}

	private void validarUsuario(Usuario usuario) throws ServletException {
		if (usuario.getUsuario() == null) {
			throw new ServletException("Falta usuario.");
		}
		if (usuario.getSenha() == null) {
			throw new ServletException("Falta senha.");
		}
	}

}
