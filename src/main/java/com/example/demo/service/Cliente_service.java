package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Cliente_dto;
import com.example.demo.entities.Cliente;
import com.example.demo.repositories.Cliente_Repository;

@Service
public class Cliente_service {

    Cliente_Repository cliente_Repository;
    ModelMapper modelMapper;

    @Autowired
    public Cliente_service(Cliente_Repository cliente_repository, ModelMapper modelMapper) {
        this.cliente_Repository = cliente_repository;
        this.modelMapper = modelMapper;
    }

    // Crear un nuevo cliente
    public Cliente_dto crearCliente(Cliente_dto clienteDto) {
        validarCliente(clienteDto);
        Cliente cliente = modelMapper.map(clienteDto, Cliente.class); // Convertir DTO a entidad
        Cliente nuevoCliente = cliente_Repository.save(cliente); // Guardar en la base de datos
        return modelMapper.map(nuevoCliente, Cliente_dto.class); // Convertir de nuevo a DTO
    }

    // Obtener todos los clientes
    public List<Cliente_dto> obtenerTodosLosClientes() {
        List<Cliente> clientes = (List<Cliente>) cliente_Repository.findAll();
        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, Cliente_dto.class))
                .collect(Collectors.toList());
    }

    // Obtener cliente por ID
    public Cliente_dto obtenerClientePorId(Long id) {
        Cliente cliente = cliente_Repository.findById(id).orElse(null); // Retorna null si no encuentra el cliente
        if (cliente != null) {
            return modelMapper.map(cliente, Cliente_dto.class); // Mapea la entidad al DTO
        }
        return null; // Retorna null si no se encontró
    }
    
    // Actualizar un cliente existente por ID
    public Cliente_dto actualizarCliente(Long id, Cliente_dto clienteDto) {
    Optional<Cliente> clienteExistente = cliente_Repository.findById(id);

    if (clienteExistente.isPresent()) {
        Cliente cliente = clienteExistente.get();
        cliente.setNombre(clienteDto.getNombre());
        cliente.setEdad(clienteDto.getEdad());
        cliente.setFoto(clienteDto.getFoto());
        cliente.setDescripcion(clienteDto.getDescripcion());
        Cliente clienteActualizado = cliente_Repository.save(cliente);
        return modelMapper.map(clienteActualizado, Cliente_dto.class);
    }

    return null; // Devuelve null si no se encuentra el cliente
    }

    // Eliminar un cliente por nombre (ID)
    public boolean eliminarCliente(Long id) {
        if (cliente_Repository.existsById(id)) {
            cliente_Repository.deleteById(id);
            return true;
        }
        return false;
    }

    // Buscar clientes por nombre
    public List<Cliente_dto> buscarClientesPorNombre(String nombre) {
        return cliente_Repository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(cliente -> modelMapper.map(cliente, Cliente_dto.class))
                .collect(Collectors.toList());
    }

    // Autenticación mejorada
    public boolean autenticarCliente(Long id, String foto) {
        return cliente_Repository.findById(id)
                .map(cliente -> cliente.getFoto().equals(foto))
                .orElse(false);
    }

    // Método de autenticación básico 
    public boolean autenticarCliente(Long id, String foto) {
        Optional<Cliente> cliente = cliente_Repository.findById(id);
        if (cliente.isPresent()) {
            return cliente.get().getFoto().equals(foto); 
        }
        return false;
    }

        // Método privado para validar cliente
    private void validarCliente(Cliente_dto clienteDto) {
        if (clienteDto.getNombre() == null || clienteDto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (clienteDto.getEdad() <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor a 0.");
        }
        if (clienteDto.getFoto() != null && clienteDto.getFoto().length() > 255) {
            throw new IllegalArgumentException("La URL de la foto es demasiado larga.");
        }
        if (clienteDto.getDescripcion() != null && clienteDto.getDescripcion().length() > 500) {
            throw new IllegalArgumentException("La descripción es demasiado larga.");
        }
    }

    // Verificar si un cliente existe por su ID
    public boolean verificarCliente(Long clienteId) {
        return cliente_Repository.existsById(clienteId);
    }

    public Calificacion crearCalificacion(Calificacion calificacion, Calificacion_service calificacionService) {
        // Verifica si el cliente asociado a la calificación existe
        if (!verificarCliente(calificacion.getClienteId())) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        return calificacionService.crearCalificacion(calificacion);
    }

}