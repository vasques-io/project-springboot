package br.com.digiboard.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;
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
	
	private UploadedFile uploadedFile;
	
	private Usuario usuario = new Usuario();
	
	private List<Usuario> usuarios;
	
	@PostConstruct
	public void init() {
		usuarios = usuarioRepository.findAll();
	}
	
	//Procura um usuário no banco de dados pelo nome
	public void procurar() {
		usuarios = usuarioRepository.findByNomeContaining(searchText);
	}
	
	//Método chamado quando alguém faz um upload de uma imagem
	public void handleFileUpload(FileUploadEvent event) {
		
		// Obtém o arquivo enviado pelo usuário
	    this.uploadedFile = event.getFile();
	    
	    //Define a url da imagem do perfil no banco de dados
	    getUsuario().setUrlImagem("/img/" + event.getFile().getFileName());
	    
	    // Define o caminho onde o arquivo será salvo
	    String caminhoArquivo = "src/main/webapp/img/" + this.uploadedFile.getFileName();
	    
	    // Cria um objeto do tipo File representando o novo arquivo a ser criado
	    File novoArquivo = new File(caminhoArquivo);
	    
	    try {
	        // Se o arquivo já existir, exclui-o
	        if (novoArquivo.exists()) {
	            novoArquivo.delete();
	        }
	        
	        // Cria o novo arquivo vazio
	        novoArquivo.createNewFile();
	        
	        // Cria um InputStream a partir do arquivo enviado pelo usuário
	        InputStream inputStream = uploadedFile.getInputStream();
	        
	        // Cria um OutputStream para escrever os dados do arquivo enviado no novo arquivo
	        FileOutputStream outputStream = new FileOutputStream(novoArquivo);
	        
	        // Cria um buffer de bytes para ler os dados do arquivo enviado e escrever no novo arquivo
	        byte[] buffer = new byte[4096];
	        
	        // Variável para armazenar a quantidade de bytes lidos do arquivo enviado
	        int bytesRead = -1;
	        
	        // Lê os dados do arquivo enviado e escreve no novo arquivo
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        
	        // Força a escrita dos dados no novo arquivo
	        outputStream.flush();
	        
	        // Fecha o InputStream e o OutputStream para liberar os recursos
	        inputStream.close();
	        outputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
    }
	
	public void salvar() throws IOException {
	    
		//Salva o usuário no banco de dados
	    usuarioRepository.save(getUsuario());
	    
	    //Limpa os campos para inserir um novo usuario
	    usuario = new Usuario();
		
	    //Redireciona para outra página após chamar este método
	    FacesContext.getCurrentInstance().getExternalContext().redirect("adminpage.xhtml");
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

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

}
