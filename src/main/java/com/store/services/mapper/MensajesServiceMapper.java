package com.store.services.mapper;



import com.store.dto.MensajesDto;
import com.store.dto.RoleDto;
import com.store.entity.Mensajes;
import com.store.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class MensajesServiceMapper extends AbstractServiceMapper<Mensajes, MensajesDto> {

    @Override
    public Mensajes toEntity(MensajesDto dto) {
        final Mensajes entidad = new Mensajes();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return entidad;
    }
    @Override
    public MensajesDto toDto(Mensajes entidad) {
        final MensajesDto dto = new MensajesDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);
        return dto;
    }

}
