package com.duoc.terapias.repository;

import com.duoc.terapias.model.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, String> {
    // Aquí puedes agregar métodos personalizados si es necesario
}

