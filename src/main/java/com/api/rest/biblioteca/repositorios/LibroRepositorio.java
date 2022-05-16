package com.api.rest.biblioteca.repositorios;

import com.api.rest.biblioteca.entidades.Libro;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro, Integer>{
    
}
