package com.store.services.mapper;



import com.store.dto.RoleDto;
import com.store.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;



@Service
public class RoleServiceMapper extends AbstractServiceMapper<Role, RoleDto> {

    @Override
    public Role toEntity(RoleDto dto) {
        final Role entidad = new Role();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return entidad;
    }
    @Override
    public RoleDto toDto(Role entidad) {
        final RoleDto dto = new RoleDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);
        return dto;
    }

}
