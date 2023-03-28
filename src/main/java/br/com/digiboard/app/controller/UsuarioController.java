package br.com.digiboard.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.digiboard.app.model.Usuario;

import br.com.digiboard.app.repository.UsuarioRepository;

@Component
@ViewScoped
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private String searchText;
	
	private Usuario usuario = new Usuario();
	
	private List<Usuario> usuarios;
	
	@PostConstruct
	public void init() {
		usuarios = usuarioRepository.findAll();
	}
	
	public void procurar() {
		usuarios = usuarioRepository.findByNomeContaining(searchText);
	}
	
	public void salvar() {
		usuarioRepository.save(getUsuario());
		usuario = new Usuario();
		try {
	        FacesContext.getCurrentInstance().getExternalContext().redirect("adminpage.xhtml");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void editar(Usuario usuarioedit) {
		setUsuario(usuarioedit);

	}
	
	public void excluir(Usuario usuario) {
		usuarios.remove(usuario);
		usuarioRepository.delete(usuario);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	

}
