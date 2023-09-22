package com.nttdata.apiusuario.service;

import com.nttdata.apiusuario.model.Usuario;
import com.nttdata.apiusuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    /***
     * Metodo que se encarga de la creación del usuario llamado desde controlador
     * @param usuario
     * @return
     * @throws Exception
     */
    @Transactional
    public Usuario crearUsuario(Usuario usuario) throws Exception {
        // Verificar si el correo ya existe en la base de datos
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new Exception("El correo ya registrado");
        }

        // Establecer valores iniciales para el usuario
        usuario.setId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        usuario.setCreated(now);
        usuario.setModified(now);
        usuario.setLast_login(now);
        usuario.setToken(generarToken());
        usuario.setIsactive(true);
        

        return usuarioRepository.save(usuario);
    }

    /***
     * Se encarga de generar un token UUID
     * @return
     */
    private String generarToken() {
        return UUID.randomUUID().toString();
    }
    
    /***
     * Metodo que actualizar los datos que se envian desde controlador
     * @param usuario
     * @return
     * @throws Exception
     */
    public Usuario updateUsuario(Usuario usuario) throws Exception {
        Optional<Usuario> existeUsuario = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (existeUsuario.isEmpty()) {
            throw new Exception("Usuario no encontrado.");
        }

        Usuario fetchedUsuario = existeUsuario.get();

        // Validaciones (si son necesarias, similares a las de arriba)

        // Actualizar campos y guardar
        fetchedUsuario.setNombre(usuario.getNombre());
        fetchedUsuario.setPassword(usuario.getPassword());
        fetchedUsuario.setTelefonos(usuario.getTelefonos());
        LocalDateTime now = LocalDateTime.now();
        fetchedUsuario.setModified(now);

        return usuarioRepository.save(fetchedUsuario);
    }

    /***
     * Metodo llamado por el contralador para realizar el login
     * @param correo
     * @param contrasena
     * @return
     */
    public Optional<Usuario> login(String correo, String contrasena) {
        Optional<Usuario> existingUsuario = usuarioRepository.findByCorreo(correo);
        if (existingUsuario.isEmpty() || !existingUsuario.get().getPassword().equals(contrasena)) {
            return Optional.empty();
        }

        Usuario fetchedUsuario = existingUsuario.get();
        LocalDateTime now = LocalDateTime.now();
        fetchedUsuario.setLast_login(now);
        String newToken = generarToken();
        fetchedUsuario.setToken(newToken);

        usuarioRepository.save(fetchedUsuario);

        return Optional.of(fetchedUsuario);
    }
    
}

