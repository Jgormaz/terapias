package com.duoc.terapias.repository;

import com.duoc.terapias.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, String> {
    // Puedes agregar métodos personalizados si los necesitas, por ejemplo:
    // Optional<Categoria> findByNombreCategoria(String nombreCategoria);
}
