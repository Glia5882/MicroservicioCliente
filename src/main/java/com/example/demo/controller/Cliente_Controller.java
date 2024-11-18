package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Cliente_dto;
import com.example.demo.service.Cliente_service;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/cliente")
public class Cliente_Controller {
    private final Cliente_service cliente_service;
    private final Calificacion_service calificacion_service;

    @Autowired
    public Cliente_Controller(Cliente_service cliente_service, Calificacion_service calificacion_service) {
        this.cliente_service = cliente_service;
        this.calificacion_service = calificacion_service;
    }

    // Crear un nuevo cliente
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente_dto> crearCliente(@RequestBody Cliente_dto clienteDto) {
        Cliente_dto nuevoCliente = cliente_service.crearCliente(clienteDto);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    // Obtener todos los clientes
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cliente_dto>> obtenerTodosLosClientes() {
        List<Cliente_dto> clientes = cliente_service.obtenerTodosLosClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    // Obtener cliente por ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente_dto> obtenerClientePorId(@PathVariable("id") Long id) {
        Cliente_dto cliente = cliente_service.obtenerClientePorId(id);
        return cliente != null ? new ResponseEntity<>(cliente, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Actualizar un cliente existente por ID
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente_dto> actualizarCliente(@PathVariable("id") Long id, @RequestBody Cliente_dto clienteDto) {
        Cliente_dto clienteActualizado = cliente_service.actualizarCliente(id, clienteDto);
        return clienteActualizado != null ? new ResponseEntity<>(clienteActualizado, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Eliminar un cliente por ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable("id") Long id) {
        boolean eliminado = cliente_service.eliminarCliente(id);
        return eliminado ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para que un cliente califique un servicio
    @PostMapping(value = "/{clienteId}/calificaciones", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calificarServicio(@PathVariable("clienteId") Long clienteId, @RequestBody Califiacion_dto calificacionDto) {
        // Verifica si el cliente existe
        if (cliente_service.obtenerClientePorId(clienteId) == null) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }

        // Asigna el clienteId a la calificación y crea la calificación
        calificacionDto.setClienteId(clienteId);
        Califiacion_dto nuevaCalificacion = calificacion_service.crearCalificacion(calificacionDto);
        return new ResponseEntity<>(nuevaCalificacion, HttpStatus.CREATED);
    }

    // Endpoint para obtener las calificaciones de un cliente
    @GetMapping(value = "/{clienteId}/calificaciones", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Califiacion_dto>> obtenerCalificacionesDelCliente(@PathVariable("clienteId") Long clienteId) {
        // Verifica si el cliente existe
        if (cliente_service.obtenerClientePorId(clienteId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Obtiene las calificaciones del cliente
        List<Califiacion_dto> calificaciones = calificacion_service.obtenerCalificacionesPorCliente(clienteId);
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }
}