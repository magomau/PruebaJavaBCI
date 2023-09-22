package com.nttdata.apiusuario.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nttdata.apiusuario.service.UsuarioService;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;

import com.nttdata.apiusuario.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import static com.nttdata.apiusuario.utilidades.Constantes.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    @Autowired
    private UsuarioService usuarioService;
    
    @Value("${validacionCorreo}")
    private String validacionCorreo;
    
    @Value("${validacionPassword}")
    private String validacionPassword;
    
    /***
     * Metodo de prueba
     * @return
     */
    @GetMapping("/prueba")
    public String hello() {
        return "Hola Mundo Get!!";
    }
    
    /**
     * Meetodo del controlar encargado de crear un usuario a la BD
     * @param usuario
     * @return
     */
    @PostMapping("/registro")
    public ResponseEntity<Object> registrarUsuario(@RequestBody Usuario usuario) {
    	logger.info("Se inicia el registro de Usuario.");
        try {
            // Validaciones para correo y contraseña
            if (!usuario.getCorreo().matches(validacionCorreo)) {
                return ResponseEntity.badRequest().body("{\"mensaje\": \"" + CORREO_INCORRECTO + "\"}");
            }

            if (!usuario.getPassword().matches(validacionPassword)) {
                return ResponseEntity.badRequest().body("{\"mensaje\": \"" + PASSWORD_INCORRECTA + "\"}");
            }

            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);

        } catch (DataIntegrityViolationException e) {
            logger.error("Correo ya registrado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"mensaje\": \"" + CORREO_REGISTRADO + "\"}");

        } catch (PatternSyntaxException e) {
            logger.error("Error en la expresión regular: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"mensaje\": \"" + ERROR_VALIDACION + "\"}");

        } catch (Exception e) {
            logger.error("Error interno del servidor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"mensaje\": \"" + ERROR_INTERNO + "\"}");
        }
    }

    /**
     * Metodo de login valida que la constraseña y el mail esten correctos
     * @param credentials
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        String correo = credentials.get("correo");
        String contrasena = credentials.get("contrasena");

        Optional<Usuario> usuario = usuarioService.login(correo, contrasena);
        if (usuario.isPresent()) {
            Usuario loggedInUser = usuario.get();
            response.put("id", loggedInUser.getId());
            response.put("last_login", loggedInUser.getLast_login());
            response.put("token", loggedInUser.getToken());
            response.put("nombre", loggedInUser.getNombre());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("mensaje", "Credenciales incorrectas");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
    
/**
 * Metodo que se encarga de atualizar los datos del usuario
 * @param usuario
 * @param token
 * @return
 */
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> update(@RequestBody Usuario usuario,
                                                      @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        try {
            
            Usuario updatedUser = usuarioService.updateUsuario(usuario);
            response.put("mensaje", "Operación exitosa");
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad de datos: {}", e.getMessage());
            response.put("mensaje", "Datos duplicados o inválidos");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            
        } catch (EntityNotFoundException e) {
            logger.error("Entidad no encontrada: {}", e.getMessage());
            response.put("mensaje", "Usuario no encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            logger.error("Argumento inválido: {}", e.getMessage());
            response.put("mensaje", "Datos de entrada inválidos");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            
        } catch (Exception e) {
            logger.error("Error interno: {}", e.getMessage());
            response.put("mensaje", "Error interno del servidor");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
