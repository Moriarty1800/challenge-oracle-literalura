package com.example.literalura.repository;

import com.example.literalura.modelo.libros;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<libros, Long> {
    List<libros> findByIdioma(String idioma);
    Optional<libros> findByTituloContainsIgnoreCase(String nombreLibro);
    Long countByIdioma(String idioma);
    List<libros> findTop10ByOrderByNumeroDeDescargasDesc();
}