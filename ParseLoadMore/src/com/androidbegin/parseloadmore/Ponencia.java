package com.androidbegin.parseloadmore;

public class Ponencia {
	private String titulo;

	private String description;
	
	private String autores[];
	
	public String[] getAutores() {
		return autores;
	}

	public void setAutores(String[] autores) {
		this.autores = autores;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}