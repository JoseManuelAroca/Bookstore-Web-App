package com.store.services;


import com.store.dto.RoleDto;
import com.store.entity.Role;
import com.store.repository.RoleRepository;
import com.store.repository.UsuarioRepository;
import com.store.services.mapper.RoleServiceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends AbstractBusinessService<Role, Long, RoleDto, RoleRepository,
        RoleServiceMapper> {

    private final UsuarioRepository usuarioRepository;

    protected RoleService(RoleRepository repository, RoleServiceMapper serviceMapper, UsuarioRepository usuarioRepository) {
        super(repository, serviceMapper);
        this.usuarioRepository = usuarioRepository;
    }

    public List<RoleDto> buscarTodosAlta(){
        return  this.getMapper().toDto(this.getRepo().findAllByShowOnCreate(1));
    }

}
