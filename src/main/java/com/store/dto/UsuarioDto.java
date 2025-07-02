package com.store.dto;

import com.store.entity.Role;
import lombok.Getter;
import lombok.Setter;


import java.util.Set;
@Getter
@Setter
public class UsuarioDto {
    private long id;
    private String email;
    private String nombreUsuario;
    private String password;

    private Set<Role> roles;
}
