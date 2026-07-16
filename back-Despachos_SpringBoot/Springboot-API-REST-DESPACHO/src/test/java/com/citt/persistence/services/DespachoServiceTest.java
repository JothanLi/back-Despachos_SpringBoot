package com.citt.persistence.services;

import com.citt.exceptions.DespachoNotFoundException;
import com.citt.persistence.entity.Despacho;
import com.citt.persistence.repository.DespachoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DespachoServiceTest {

    @Mock
    private DespachoRepository despachoRepository;

    @InjectMocks
    private DespachoServiceImpl despachoService;

    private Despacho despacho;

    @BeforeEach
    void setUp() {
        despacho = new Despacho(
                1L,
                LocalDate.of(2026, 7, 20),
                "ABCD12",
                0,
                15L,
                "Av. Siempre Viva 123",
                45000L,
                false
        );
    }

    @Test
    @DisplayName("Actualiza estado e intentos sin borrar los datos del despacho")
    void actualizarEstadoPreservaDatos() throws DespachoNotFoundException {
        when(despachoRepository.findById(1L)).thenReturn(Optional.of(despacho));
        when(despachoRepository.save(any(Despacho.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Despacho actualizado = despachoService.updateEstado(1L, 2, true);

        assertEquals(2, actualizado.getIntento());
        assertTrue(actualizado.getDespachado());
        assertEquals("ABCD12", actualizado.getPatenteCamion());
        assertEquals("Av. Siempre Viva 123", actualizado.getDireccionCompra());
        assertEquals(Long.valueOf(45000L), actualizado.getValorCompra());
    }
}
