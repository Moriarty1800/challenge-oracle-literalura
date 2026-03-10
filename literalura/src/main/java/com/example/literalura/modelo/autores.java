package com.example.literalura.modelo;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class autores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<libros> libros;

    public autores() {}

    public autores(ignorePropDatosAutor ignorePropDatosAutor) {
        this.nombre = ignorePropDatosAutor.nombre();
        this.fechaDeNacimiento = ignorePropDatosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = ignorePropDatosAutor.fechaDeFallecimiento();
    }

    @Override
    public String toString() {
        return "Autor: " + nombre +
                " (" + fechaDeNacimiento + " - " + (fechaDeFallecimiento == null ? "Presente" : fechaDeFallecimiento) + ")";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<libros> getLibros() {
        return libros;
    }

    public void setLibros(List<libros> libros) {
        this.libros = libros;
    }
}