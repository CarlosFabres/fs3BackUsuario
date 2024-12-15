package com.usuarios.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuarios.usuario.model.Usuario;
import com.usuarios.usuario.service.UsuarioService;
import com.usuarios.usuario.controller.UsuarioController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

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
    public void testObtenerUsuarios() throws Exception {
        when(usuarioService.buscarUsuarios()).thenReturn(List.of(usuario)); // Cambia a `List.of()` si solo hay un usuario

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testCrearUsuario() throws Exception {
        when(usuarioService.guardarUsuario(usuario)).thenReturn(usuario);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usuario)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    public void testObtenerUsuarioPorId() throws Exception {
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/1"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        when(usuarioService.actualizarUsuario(usuario)).thenReturn(usuario);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(usuario)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminarUsuario(1L);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/1"))
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    public void testIniciarSesion() throws Exception {
        when(usuarioService.validarCredenciales("usuario_prueba", "password_prueba")).thenReturn(Optional.of(usuario));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"usuario_prueba\",\"password\":\"password_prueba\"}"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
