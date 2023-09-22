package com.nttdata.apiusuario.repository;

import java.util.Optional;
import java.util.UUID;

import com.nttdata.apiusuario.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByCorreo(String correo);
}

