package com.example.literalura.principal;

import com.example.literalura.modelo.autores;
import com.example.literalura.modelo.ignorepropdatos;
import com.example.literalura.modelo.ignorePropsDatosLibro;
import com.example.literalura.modelo.libros;
import com.example.literalura.repository.AutorRepository;
import com.example.literalura.repository.LibroRepository;
import com.example.literalura.service.ConsumoAPI;
import com.example.literalura.service.ConvierteDatos;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final Scanner lectura = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final LibroRepository repository;
    private final AutorRepository autorRepository;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    public void menuInicial() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Libros por título
                    2 - Lista de libros
                    3 - Lista de autores
                    4 - Autores vivos por año
                    5 - Libros por idioma
                    6 - Estadísticas de descargas
                    7 - Top 10 más descargados
                    8 - Autores por nombre
                    9 - Autores por año de nacimiento
                    0 - Salir
                    """;
            System.out.println(menu);

            try {
                opcion = Integer.parseInt(lectura.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción no válida");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosPorFecha();
                case 5 -> listarLibrosPorIdioma();
                case 6 -> mostrarEstadisticasDeDescargas();
                case 7 -> listarTop10Libros();
                case 8 -> buscarAutorPorNombre();
                case 9 -> buscarAutoresPorFechaNacimiento();
                case 0 -> System.out.println("Gracias por usar la app");
                default -> System.out.println("Opción no válida");
            }
        }
    }

    private ignorepropdatos getDatosLibro() {
        System.out.println("Nombre del libro que a buscar:");
        var nombreLibro = lectura.nextLine();
        String URL_BASE = "https://gutendex.com/books/";
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, ignorepropdatos.class);
    }

    private void buscarLibroPorTitulo() {
        ignorepropdatos ignorepropdatos = getDatosLibro();
        if (!ignorepropdatos.resultados().isEmpty()) {
            ignorePropsDatosLibro primerLibro = ignorepropdatos.resultados().get(0);

            Optional<libros> libroExistente = repository.findByTituloContainsIgnoreCase(primerLibro.titulo());

            if (libroExistente.isPresent()) {
                System.out.println("\nEl libros que buscas es:");
                System.out.println(libroExistente.get());
            } else {
                var datosAutor = primerLibro.autor().get(0);
                autores autores = autorRepository.findByNombreIgnoreCase(datosAutor.nombre())
                        .orElseGet(() -> {
                            autores nuevoAutores = new autores(datosAutor);
                            return autorRepository.save(nuevoAutores);
                        });
                libros libros = new libros(primerLibro, autores);
                repository.save(libros);
                System.out.println(libros);
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        List<libros> libros = repository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<autores> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorFecha() {
        System.out.println("¿Qué año quieres consultar?");
        try {
            var fecha = Integer.parseInt(lectura.nextLine());

            if (fecha < 0 || fecha > 2025) {
                System.out.println("Ingrese un año eentre 0 y 2025.");
                return;
            }

            List<autores> autoresVivos = autorRepository.buscarAutoresVivosEnDeterminadaFecha(fecha);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron resultados.");
            } else {
                System.out.println(" Autores vivos en " + fecha + ".");
                autoresVivos.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un número entero.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Eliga un idioma:
                en - inglés
                es - español
                fr - francés
                ru - ruso
                pt - portugués
                """);
        var idioma = lectura.nextLine();

        Long cantidadLibros = repository.countByIdioma(idioma);

        List<libros> librosPorIdioma = repository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron resultados.");
        } else {
            System.out.println("Existen " + cantidadLibros + " en " + idioma);

            librosPorIdioma.forEach(System.out::println);
        }
    }

    private void mostrarEstadisticasDeDescargas() {
        List<libros> todosLosLibros = repository.findAll();
        DoubleSummaryStatistics est = todosLosLibros.stream()
                .filter(l -> l.getNumeroDeDescargas() > 0)
                .mapToDouble(libros::getNumeroDeDescargas)
                .summaryStatistics();

        System.out.println("Media de descargas: " + est.getAverage());
        System.out.println("Máxima cantidad de descargas: " + est.getMax());
        System.out.println("Mínima cantidad de descargas: " + est.getMin());
        System.out.println("Cantidad de libros evaluados: " + est.getCount());
    }

    private void buscarAutorPorNombre() {
        System.out.println("Nombre del autor:");
        var nombre = lectura.nextLine();
        Optional<autores> autores = autorRepository.findByNombreContainsIgnoreCase(nombre);
        if (autores.isPresent()) {
            System.out.println(autores.get());
        } else {
            System.out.println("Búsqueda sin resultados.");
        }
    }

    private void listarTop10Libros() {
        List<libros> topLibros = repository.findTop10ByOrderByNumeroDeDescargasDesc();
        System.out.println("Libros más descargados");
        topLibros.forEach(l ->
                System.out.println("Libro: " + l.getTitulo() + " | Descargas: " + l.getNumeroDeDescargas()));
    }

    private void buscarAutoresPorFechaNacimiento() {
        System.out.println("Año de nacimiento del autor:");
        try {
            var fecha = Integer.parseInt(lectura.nextLine());
            List<autores> autores = autorRepository.findByFechaDeNacimiento(fecha);
            if (autores.isEmpty()) {
                System.out.println("La búsqueda no tiene resultados para el año " + fecha);
            } else {
                autores.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Fecha no válida.");
        }
    }
}