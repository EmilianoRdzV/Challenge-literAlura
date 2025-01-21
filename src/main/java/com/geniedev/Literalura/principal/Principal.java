package com.geniedev.Literalura.principal;

import com.geniedev.Literalura.model.*;
import com.geniedev.Literalura.service.AutorService;
import com.geniedev.Literalura.service.ConsumoAPI;
import com.geniedev.Literalura.service.ConvierteDatos;
import com.geniedev.Literalura.service.LibroService;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String MENU = """
                --------------
                **Catálogo de libros en Literalura**
                1.- Buscar libro por título
                2.- Listar libros registrados
                3.- Listar autores registrados
                4.- Listar autores vivos en un determinado año
                5.- Listar libros por idioma
                6.- Estadísticas de libros por número de descargas
                7.- Top 10 libros más descargados
                8.- Buscar autor por nombre
                0.- Salir
                --------------
                Elija la opción a través de su número:""";

    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner teclado = new Scanner(System.in);
    private final LibroService libroServicio;
    private final AutorService autorServicio;

    public Principal(LibroService libroService, AutorService autorService) {
        this.libroServicio = libroService;
        this.autorServicio = autorService;
    }

    public void muestraMenu() {
        int opcion;
        do {
            System.out.println(MENU);
            opcion = obtenerEntradaNumerica();
            procesarOpcion(opcion);
        } while (opcion != 0);
        teclado.close();
    }

    private int obtenerEntradaNumerica() {
        try {
            return teclado.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Opción inválida. Favor de introducir un número del menú.");
            return -1;
        } finally {
            teclado.nextLine();
        }
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> buscarLibroPorTitulo();
            case 2 -> listarLibrosRegistrados();
            case 3 -> listarAutoresRegistrados();
            case 4 -> buscarAutoresVivosPorAnio();
            case 5 -> listarLibrosPorIdioma();
            case 6 -> estadisticasLibrosPorNumDescargas();
            case 7 -> top10LibrosMasDescargados();
            case 8 -> buscarAutorPorNombre();
            case 0 -> System.out.println("Cerrando la aplicación...");
            default -> System.out.println("Opción inválida. Favor de introducir un número del menú.");
        }
    }

    private DatosResultados obtenerDatosResultados(String tituloLibro) {
        String url = URL_BASE + "?search=" + tituloLibro.replace(" ", "%20");
        return conversor.obtenerDatos(consumoAPI.obtenerDatos(url), DatosResultados.class);
    }

    private void buscarLibroPorTitulo() {
        System.out.print("Escribe el título del libro que deseas buscar: ");
        String tituloLibro = teclado.nextLine().toUpperCase();

        libroServicio.buscarLibroPorTitulo(tituloLibro).ifPresentOrElse(
                libro -> System.out.println("El libro buscado ya está registrado."),
                () -> registrarNuevoLibro(tituloLibro)
        );
    }

    private void registrarNuevoLibro(String tituloLibro) {
        DatosResultados datos = obtenerDatosResultados(tituloLibro);

        if (datos.listaLibros().isEmpty()) {
            System.out.println("No se encontró el libro buscado en Gutendex API.");
            return;
        }

        DatosLibros datosLibros = datos.listaLibros().get(0);
        Libro libro = new Libro(datosLibros);
        libro.setIdiomas(Idiomas.fromString(datosLibros.idiomas().get(0)));

        Autor autor = autorServicio.buscarAutorRegistrado(datosLibros.autores().get(0).nombre())
                .orElseGet(() -> autorServicio.guardarAutor(new Autor(datosLibros.autores().get(0))));

        libro.setAutor(autor);
        autor.getLibros().add(libro);

        try {
            libroServicio.guardarLibro(libro);
            System.out.println("Libro guardado: " + libro);
        } catch (DataIntegrityViolationException e) {
            System.out.println("El libro ya está registrado.");
        }
    }

    private void listarLibrosRegistrados() {
        libroServicio.listarLibrosRegistrados().stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        autorServicio.listarAutoresRegistrados().forEach(autor -> {
            System.out.println("Autor: " + autor.getNombre());
            autor.getLibros().forEach(libro -> System.out.println("  - " + libro.getTitulo()));
            String librosRegistrados = autor.getLibros().stream().map(Libro::getTitulo).collect(Collectors.joining(", "));
            System.out.println("Libros: ["+librosRegistrados+"]");
            System.out.println("-----------------");
        });
    }

    private void buscarAutoresVivosPorAnio() {
        System.out.print("Ingrese el año: ");
        int anio = teclado.nextInt();
        teclado.nextLine();
        autorServicio.buscarAutoresVivosPorAnio(anio).forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {
        System.out.print("Ingrese el idioma (es, en, fr, pt): ");
        String idioma = teclado.nextLine().toLowerCase();
        libroServicio.buscarLibroPorIdiomas(Idiomas.fromString(idioma)).forEach(System.out::println);
    }

    private void estadisticasLibrosPorNumDescargas() {
        DoubleSummaryStatistics stats = libroServicio.listarLibrosRegistrados().stream()
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();
        System.out.println("Estadísticas de libros por número de descargas: ");
        System.out.println("Promedio: " + stats.getAverage());
        System.out.println("Máximo: " + stats.getMax());
        System.out.println("Mínimo: " + stats.getMin());
    }

    private void top10LibrosMasDescargados() {
        libroServicio.listarLibrosRegistrados().stream()
                .sorted(Comparator.comparingDouble(Libro::getNumeroDescargas).reversed())
                .limit(10)
                .forEach(System.out::println);
    }

    private void buscarAutorPorNombre() {
        System.out.print("Ingrese el nombre del autor: ");
        String nombre = teclado.nextLine();
        autorServicio.buscarAutorPorNombre(nombre).forEach(System.out::println);
    }
}
