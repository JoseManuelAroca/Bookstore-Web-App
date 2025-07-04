package com.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MensajesDto {

    private long id;
    private String mensaje;
    private long id_emisor;
    private long id_receptor;
    private byte[] mensajeEncriptado;
    private String mensajeDesencriptado;
}
