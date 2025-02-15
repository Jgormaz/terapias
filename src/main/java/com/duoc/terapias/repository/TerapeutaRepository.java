
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TerapeutaRepository extends JpaRepository<Terapeuta, Long> {
    Optional<Terapeuta> findByUserName(String userName);
}


