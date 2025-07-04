package com.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mensajes")
public class Mensajes {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column (name="mensaje")
    private String mensaje;
    @Column (name="id_emisor")
    private long id_emisor;
    @Column (name="id_receptor")
    private long id_receptor;
    @Lob
    private byte[] mensajeEncriptado;

}
