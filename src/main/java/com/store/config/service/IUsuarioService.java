package com.store.config.service;

import com.store.entity.Usuario;

public interface IUsuarioService {
    public String getEncodedPassword(Usuario usuario);
}
