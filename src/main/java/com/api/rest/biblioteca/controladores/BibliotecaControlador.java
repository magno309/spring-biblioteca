package com.api.rest.biblioteca.controladores;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import com.api.rest.biblioteca.entidades.Biblioteca;
import com.api.rest.biblioteca.repositorios.BibliotecaRepositorio;

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
@RequestMapping("/api/biblioteca")
public class BibliotecaControlador {

    @Autowired
    private BibliotecaRepositorio bibliotecaRepositorio;

    /**
     * Guarda una biblioteca en la base de datos
     * 
     * @param biblioteca Biblioteca a guardar
     * @return JSON con los datos de la biblioteca guardada
     */
    @PostMapping
    public ResponseEntity<Biblioteca> guardarBiblioteca(@Valid @RequestBody Biblioteca biblioteca) {
        Biblioteca bibliotecaGuardada = bibliotecaRepositorio.save(biblioteca);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(bibliotecaGuardada.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(bibliotecaGuardada);
    }

    /**
     * Edita una biblioteca existente en la base de datos
     * 
     * @param id         Id de la biblioteca a editar
     * @param biblioteca Instancia de con los nuevos datos de la biblioteca
     * @return Respuesta sin contenido
     */
    @PutMapping("/{id}")
    public ResponseEntity<Biblioteca> editarBiblioteca(@PathVariable Integer id,
            @Valid @RequestBody Biblioteca biblioteca) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepositorio.findById(id);
        if (!bibliotecaOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        biblioteca.setId(bibliotecaOptional.get().getId());
        bibliotecaRepositorio.save(biblioteca);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina una biblioteca de la base de datos
     * 
     * @param id Id de la biblioteca a eliminar
     * @return Respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Biblioteca> eliminarBiblioteca(@PathVariable Integer id) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepositorio.findById(id);
        if (!bibliotecaOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        bibliotecaRepositorio.delete(bibliotecaOptional.get());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene una lista de todas las bibliotecas
     * 
     * @param pageable Número de página
     * @return Una lista de todas las bibliotecas de acuerdo a un número de página
     */
    @GetMapping
    public ResponseEntity<Page<Biblioteca>> listarBibliotecas(Pageable pageable) {
        return ResponseEntity.ok(bibliotecaRepositorio.findAll(pageable));
    }

    /**
     * Obtiene los datos una una biblioteca en base a su ID
     * 
     * @param id Id de la biblioteca que se quiere obtener
     * @return JSON con los datos de la biblioteca
     */
    @GetMapping("/{id}")
    public ResponseEntity<Biblioteca> obtenerBiblioteca(@PathVariable Integer id) {
        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepositorio.findById(id);
        if (!bibliotecaOptional.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok(bibliotecaOptional.get());
    }
}
