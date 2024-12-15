package com.usuarios.usuario;

import com.usuarios.usuario.model.Usuario;
import com.usuarios.usuario.repository.UsuarioRepository;
import com.usuarios.usuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("usuario_prueba");
        usuario.setPassword("password_prueba");
        usuario.setRol("ROLE_USER");
    }

    @Test
    public void testBuscarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(java.util.Collections.singletonList(usuario));

        Iterable<Usuario> usuarios = usuarioService.buscarUsuarios();

        assertNotNull(usuarios);
        assertTrue(usuarios.iterator().hasNext());
        assertEquals(usuario.getUsername(), usuarios.iterator().next().getUsername());
    }

    @Test
    public void testGuardarUsuario() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);

        assertNotNull(nuevoUsuario);
        assertEquals(usuario.getUsername(), nuevoUsuario.getUsername());
    }

    @Test
    public void testBuscarUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.buscarUsuarioPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(usuario.getUsername(), result.get().getUsername());
    }

    @Test
    public void testActualizarUsuario() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuario);

        assertNotNull(usuarioActualizado);
        assertEquals(usuario.getUsername(), usuarioActualizado.getUsername());
    }

    @Test
    public void testEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testValidarCredenciales() {
        when(usuarioRepository.findByUsername("usuario_prueba")).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.validarCredenciales("usuario_prueba", "password_prueba");

        assertTrue(result.isPresent());
        assertEquals(usuario.getUsername(), result.get().getUsername());
    }
}
