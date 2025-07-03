package com.store.controller;

import com.store.config.service.IUsuarioService;
import com.store.dto.LoginDto;
import com.store.dto.RoleDto;
import com.store.dto.UsuarioDto;
import com.store.entity.Role;
import com.store.entity.Usuario;
import com.store.services.RoleService;
import com.store.services.UsuarioService;
import com.store.util.GetBCryptPasswordEncoder;
import com.store.util.ValidarFormatoPassword;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsuarioController {
    private final UsuarioService service;
    private final RoleService roleService;

    private final IUsuarioService usuarioService;

    private final GetBCryptPasswordEncoder encoder;

    public UsuarioController(UsuarioService service, RoleService roleService, IUsuarioService usuarioService, GetBCryptPasswordEncoder encoder) {
        this.service = service;
        this.roleService = roleService;
        this.usuarioService = usuarioService;
        this.encoder = encoder;
    }
    //Controlador de Login
    @GetMapping("/usuarios/login")
    public String vistaLogin(){
        return "usuarios/login";
    }
    @PostMapping("/usuarios/login")
    public String validarPasswordPst(@ModelAttribute(name = "loginForm" ) LoginDto loginDto) {
        String usr = loginDto.getUsername();
        System.out.println("usr :" + usr);
        String password = loginDto.getPassword();
        System.out.println("pass :" + password);
        //¿es correcta la password?
        if (service.getRepo().repValidarPassword(usr, encoder.bCryptPasswordEncoder().encode(password)  ) > 0)
        {
            System.out.println("login ok" );
            return "home";
        }else {
            System.out.println("login ko" );
            return "usuarios/login";
        }
    }

    @GetMapping("/logout/msg")
    public String logout(Model model) {
        return "usuarios/logout";
    }

    //Para crear un usuario hay dos bloques
    //El que genera la pantalla para pedir los datos de tipo GetMapping
    //Cuando pasamos informacion a la pantalla hay que usar ModelMap
    @GetMapping("/usuarios/registro")
    public String vistaRegistro(Model interfazConPantalla){
        //Instancia en memoria del dto a informar en la pantalla
        final UsuarioDto usuarioDto = new UsuarioDto();
        //Obtengo la lista de roles
        final List<Role> rolelist = roleService.buscarEntidades();
        //Mediante "addAttribute" comparto con la pantalla
        interfazConPantalla.addAttribute("datosUsuario",usuarioDto);
        interfazConPantalla.addAttribute("listaRoles",rolelist);
        System.out.println("Preparando pantalla registro");
        return "usuarios/registro";
    }
    //El que con los datos de la pantalla guarda la informacion de tipo PostMapping
    @PostMapping("/usuarios/registro")
    public String guardarUsuario( @ModelAttribute(name ="datosUsuario") UsuarioDto usuarioDto) throws Exception {
        //Comprobamos el patron
        System.out.println("Guardando usuario antes ");
        System.out.println("Usuario :" + usuarioDto.getNombreUsuario() + ", password : " + usuarioDto.getPassword() );
        if (ValidarFormatoPassword.ValidarFormato(usuarioDto.getPassword())){
            Usuario usuario = service.getMapper().toEntity(usuarioDto);
            System.out.println("Guardando usuario");
            System.out.println("Usuario :" + usuario.getNombreUsuario() + ", password : " + usuario.getPassword() );
            //Codifico la password
            String encodedPasswod = usuarioService.getEncodedPassword(usuario);
            usuarioDto.setPassword(encodedPasswod);
            //El usuario se guarda como no autorizado
            //Guardo la password
            UsuarioDto usuario1 = this.service.guardar(usuarioDto);
            //return "usuarios/detallesusuario";
            return String.format("redirect:/usuarios/%s", usuario1.getId());
        }
        else
        {
            return "usuarios/registro";
        }

    }
}
