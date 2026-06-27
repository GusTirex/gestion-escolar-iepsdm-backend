package com.sdm.gestion_escolar_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdm.gestion_escolar_backend.entity.Rol;
import com.sdm.gestion_escolar_backend.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    //Buscar por usuario
    Usuario findByUsuario(String usuario);

    // Buscar por email
    Optional<Usuario> findByEmail(String email);
    
    // Buscar por rol
    List<Usuario> findByRol(Rol rol);
    
    // Buscar usuarios activos
    List<Usuario> findByEstadoTrue();
    
    // Verificar si existe email
    boolean existsByEmail(String email);
    
    // Buscar usuarios por rol y activos
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.estado = true")
    List<Usuario> findByRolAndEstadoTrue(@Param("rol") Rol rol);
}
