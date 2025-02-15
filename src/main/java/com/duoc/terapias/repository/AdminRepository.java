
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByUserName(String userName); // Buscar un Admin por userName
}

