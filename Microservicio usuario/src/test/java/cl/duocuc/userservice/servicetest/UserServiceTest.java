package cl.duocuc.userservice.servicetest;

import cl.duocuc.userservice.model.User;
import cl.duocuc.userservice.repository.UserRepository;
import cl.duocuc.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@DataJpaTest
@EntityScan(basePackages = "cl.duocuc.userservice.model")

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(
                User.builder().id("1").name("Test").build()
        ));

        List<User> users = userService.findAll();
        assertFalse(users.isEmpty(), "La lista no debe estar vacía");
    }

    @Test
    void testAddUserSuccess() {
        User newUser = User.builder().id("999").name("Nuevo Usuario").email("nuevo@ejemplo.cl").password("clave999").rol("CLIENTE").active(true).build();

        when(userRepository.existsById("999")).thenReturn(false);

        boolean result = userService.addUser(newUser);

        assertTrue(result, "Debe permitir agregar un nuevo usuario");
        verify(userRepository).save(newUser);
    }

    @Test
    void testAddUserDuplicateId() {
        User duplicate = User.builder().id("1").name("Duplicado").email("duplicado@ejemplo.cl").password("clave").rol("CLIENTE").active(true).build();

        when(userRepository.existsById("1")).thenReturn(true);

        boolean result = userService.addUser(duplicate);

        assertFalse(result, "No debe permitir agregar un usuario con ID repetido");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRemoveUser() {
        when(userRepository.existsById("1")).thenReturn(true);

        boolean removed = userService.removeUser("1");

        assertTrue(removed, "Debe eliminar el usuario con ID 1");
        verify(userRepository).deleteById("1");
    }

    @Test
    void testUpdateUser() {
        User updated = User.builder().id("2").name("Actualizado").email("nuevo@correo.cl").password("cambio").rol("CLIENTE").active(true).build();

        when(userRepository.existsById("2")).thenReturn(true);

        boolean result = userService.updateUser("2", updated);

        assertTrue(result, "Debe actualizar el usuario");
        verify(userRepository).save(updated);
    }

    @Test
    void testDesactivateUser() {
        User user = User.builder().id("3").active(true).build();

        when(userRepository.findById("3")).thenReturn(Optional.of(user));

        boolean result = userService.desactivateUser("3");

        assertTrue(result, "Debe desactivar el usuario");
        assertFalse(user.isActive(), "El usuario debe estar inactivo");
        verify(userRepository).save(user);
    }
}
