
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
    
    Optional<Paciente> findByCorreo(String correo);
}

