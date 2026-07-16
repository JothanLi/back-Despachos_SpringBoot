package com.citt.controller;

import com.citt.dto.ActualizarEstadoDespachoRequest;
import com.citt.exceptions.DespachoNotFoundException;
import com.citt.persistence.entity.Despacho;
import com.citt.persistence.services.DespachoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/despachos")
@Tag(name = "Despacho", description = "Controlador para gestionar despachos")
public class DespachoController {

    private final DespachoService despachoService;

    public DespachoController(DespachoService despachoService) {
        this.despachoService = despachoService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo despacho")
    public ResponseEntity<Despacho> crearDespacho(@Valid @RequestBody Despacho despacho) {
        Despacho despachoGuardado = despachoService.saveDespacho(despacho);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idDespacho}")
                .buildAndExpand(despachoGuardado.getIdDespacho())
                .toUri();
        return ResponseEntity.created(location).body(despachoGuardado);
    }

    @PutMapping("/{idDespacho}")
    @Operation(summary = "Actualizar completamente un despacho")
    public ResponseEntity<Despacho> actualizarDespacho(
            @PathVariable Long idDespacho,
            @Valid @RequestBody Despacho despacho) throws DespachoNotFoundException {
        return ResponseEntity.ok(despachoService.updateDespacho(idDespacho, despacho));
    }

    @PatchMapping("/{idDespacho}/estado")
    @Operation(summary = "Actualizar únicamente intentos y estado de entrega")
    public ResponseEntity<Despacho> actualizarEstado(
            @PathVariable Long idDespacho,
            @Valid @RequestBody ActualizarEstadoDespachoRequest request) throws DespachoNotFoundException {
        return ResponseEntity.ok(
                despachoService.updateEstado(idDespacho, request.intento(), request.despachado())
        );
    }

    @GetMapping
    @Operation(summary = "Obtener todos los despachos")
    public ResponseEntity<List<Despacho>> getAllDespachos() {
        return ResponseEntity.ok(despachoService.findAllDespachos());
    }

    @GetMapping("/{idDespacho}")
    @Operation(summary = "Obtener un despacho por ID")
    public ResponseEntity<Despacho> obtenerDespacho(@PathVariable Long idDespacho) throws DespachoNotFoundException {
        return ResponseEntity.ok(despachoService.findById(idDespacho));
    }

    @DeleteMapping("/{idDespacho}")
    @Operation(summary = "Eliminar un despacho por ID")
    public ResponseEntity<Void> eliminarDespacho(@PathVariable Long idDespacho) throws DespachoNotFoundException {
        despachoService.deleteDespacho(idDespacho);
        return ResponseEntity.noContent().build();
    }
}
