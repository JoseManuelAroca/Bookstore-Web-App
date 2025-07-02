package com.store.services;



import com.store.dto.UsuarioDto;
import com.store.entity.Usuario;
import com.store.repository.UsuarioRepository;
import com.store.services.mapper.UsuarioMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


@Service
public class UsuarioService   extends AbstractBusinessService<Usuario,Integer, UsuarioDto,
        UsuarioRepository, UsuarioMapper>   {
    //


    //Acceso a los datos de la bbdd
    public UsuarioService(UsuarioRepository repo, UsuarioMapper serviceMapper) {

        super(repo, serviceMapper);
    }
    public UsuarioDto guardar(UsuarioDto usuarioDtoPsw){
        System.out.println("usuarioDto:" +usuarioDtoPsw.getNombreUsuario() );
        //Traduzco del dto con datos de entrada a la entidad
        final Usuario entidad = getMapper().toEntity(usuarioDtoPsw);
        System.out.println("Entidad:" +entidad.getNombreUsuario() );
        //Guardo el la base de datos
        Usuario entidadGuardada =  getRepo().save(entidad);
        //Traducir la entidad a DTO para devolver el DTO
        return getMapper().toDto(entidadGuardada);
    }


}
