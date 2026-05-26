package com.sdm.gestion_escolar_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sdm.gestion_escolar_backend.model.Rol;
import com.sdm.gestion_escolar_backend.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    //Buscar por usuario
    Usuario findByUsername(String username);

    // Buscar por email
    Optional<Usuario> findByEmail(String email);
    
    // Buscar por rol
    List<Usuario> findByRol(Rol rol);
    
    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();
    
    // Verificar si existe email
    boolean existsByEmail(String email);
    
    // Buscar usuarios por rol y activos
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    List<Usuario> findActiveUsersByRole(@Param("rol") Rol rol);
}
