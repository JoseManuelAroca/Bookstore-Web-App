package com.store.services.mapper;


import com.store.dto.UsuarioDto;
import com.store.entity.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UsuarioMapper extends AbstractServiceMapper<Usuario, UsuarioDto> {
    //Convertir de entidad a dtoç
    @Override
    public UsuarioDto toDto(Usuario entidad){
        final UsuarioDto dto = new UsuarioDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public Usuario toEntity(UsuarioDto dto){
        final Usuario entidad = new Usuario();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);
        return entidad;
    }

    public UsuarioMapper() {
    }
}
