package cl.duocuc.userservice.repositorytest;

import cl.duocuc.userservice.model.User;
import cl.duocuc.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Test
    void testCargaInicialDeUsuarios() {
        List<User> usuarios = userRepository.findAll();
        assertEquals(30, usuarios.size(), "Deben cargarse 30 usuarios desde data.sql");
    }

    @Test
    void testBuscarUsuarioPorEmail() {
        Optional<User> usuario = userRepository.findByEmail("benjamín.andrés.aguilar.ledezma@ejemplo.cl");

        assertTrue(usuario.isPresent(), "El usuario debe existir en la base de datos");
        assertEquals("ADMIN", usuario.get().getRol(), "Debe tener rol ADMIN");
    }
}

