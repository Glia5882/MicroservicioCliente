package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Cliente_dto {
    private long id_Cliente;
    private String Nombre;
    private int Edad;
    private String Foto;
    private String Descripcion;
}
