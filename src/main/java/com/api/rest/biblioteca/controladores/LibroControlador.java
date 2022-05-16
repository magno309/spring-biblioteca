package com.api.rest.biblioteca.controladores;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import com.api.rest.biblioteca.entidades.Biblioteca;
import com.api.rest.biblioteca.entidades.Libro;
import com.api.rest.biblioteca.repositorios.BibliotecaRepositorio;
import com.api.rest.biblioteca.repositorios.LibroRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/libros")
public class LibroControlador {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private BibliotecaRepositorio bibliotecaRepositorio;

    /**
     * Guarda un libro en la base de datos
     * 
     * @param biblioteca Libro a guardar
     * @return JSON con los datos del libro guardado
     */
    @PostMapping
    public ResponseEntity<Libro> guardarLibro(@Valid @RequestBody Libro libro) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepositorio.findById(libro.getBiblioteca().getId());
        if (!bibliotecaOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        libro.setBiblioteca(bibliotecaOptional.get());
        Libro libroGuardado = libroRepositorio.save(libro);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(libroGuardado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(libroGuardado);
    }

    /**
     * Edita un libro existente en la base de datos
     * 
     * @param id         Id del libro a editar
     * @param biblioteca Instancia de con los nuevos datos del libro
     * @return Respuesta sin contenido
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libro> editarLibro(@PathVariable Integer id, @Valid @RequestBody Libro libro) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepositorio.findById(id);
        if (!bibliotecaOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Optional<Libro> libroOptional = libroRepositorio.findById(id);
        if (!libroOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        libro.setBiblioteca(bibliotecaOptional.get());
        libro.setId(libroOptional.get().getId());
        libroRepositorio.save(libro);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina un libro de la base de datos
     * 
     * @param id Id del libro a eliminar
     * @return Respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Biblioteca> eliminarLibro(@PathVariable Integer id) {
        Optional<Libro> libroOptional = libroRepositorio.findById(id);
        if (!libroOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        libroRepositorio.delete(libroOptional.get());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene una lista de todos los libros
     * 
     * @param pageable Número de página
     * @return Una lista de todos los libros de acuerdo a un número de página
     */
    @GetMapping
    public ResponseEntity<Page<Libro>> listarLibros(Pageable pageable) {
        return ResponseEntity.ok(libroRepositorio.findAll(pageable));
    }

    /**
     * Obtiene los datos una una biblioteca en base a su ID
     * 
     * @param id Id de la biblioteca que se quiere obtener
     * @return JSON con los datos de la biblioteca
     */
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibro(@PathVariable Integer id) {
        Optional<Libro> libroOptional = libroRepositorio.findById(id);
        if (!libroOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok(libroOptional.get());
    }
}
