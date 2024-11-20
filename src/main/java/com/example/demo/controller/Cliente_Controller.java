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
    Cliente_service cliente_service;

    @Autowired
    public Cliente_Controller(Cliente_service cliente_service) {
        this.cliente_service = cliente_service;
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

        if (cliente != null) {
            return new ResponseEntity<>(cliente, HttpStatus.OK); // Retorna el cliente con estado 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 si no encuentra el cliente
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente_dto> obtenerCliente(@PathVariable Long id) {
        Cliente_dto clienteDto = cliente_service.obtenerClientePorId(id);
        return ResponseEntity.ok(clienteDto);
    }

    // Actualizar un cliente existente por ID
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente_dto> actualizarCliente(@PathVariable("id") Long id, @RequestBody Cliente_dto clienteDto) {
        Cliente_dto clienteActualizado = cliente_service.actualizarCliente(id, clienteDto);

        if (clienteActualizado != null) {
            return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

    // Eliminar un cliente por ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable("id") Long id) {
        boolean eliminado = cliente_service.eliminarCliente(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
