package com.example.literalura.modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ignorepropdatos(
        @JsonAlias("results") List<ignorePropsDatosLibro> resultados
) {
}