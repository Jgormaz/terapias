package com.duoc.terapias.repository;

import com.duoc.terapias.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
    // Aquí puedes agregar métodos personalizados si es necesario
}

