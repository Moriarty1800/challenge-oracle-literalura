package com.example.literalura.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private autores autores;
    private String idioma;
    private Double numeroDeDescargas;

    public libros() {}

    public libros(ignorePropsDatosLibro ignorePropsDatosLibro, autores autores) {
        this.titulo = ignorePropsDatosLibro.titulo();
        this.idioma = ignorePropsDatosLibro.idiomas().isEmpty() ? "Desconocido" : ignorePropsDatosLibro.idiomas().get(0);
        this.numeroDeDescargas = ignorePropsDatosLibro.numeroDeDescargas();
        this.autores = autores;
    }

    @Override
    public String toString() {
        return "LIBRO" + "\n" +
                "Título: " + titulo + "\n" +
                "Autor: " + autores + "\n" +
                "Idioma: " + idioma + "\n" +
                "Descargas: " + numeroDeDescargas + "\n" +
                ;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public autores getAutor() {
        return autores;
    }

    public void setAutor(autores autores) {
        this.autores = autores;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
}