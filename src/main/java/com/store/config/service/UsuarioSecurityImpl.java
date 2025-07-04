package com.store.config.service;

import com.store.config.details.ImprovedUserDetails;
import com.store.entity.Role;
import com.store.entity.Usuario;
import com.store.repository.UsuarioRepository;
import com.store.util.GetBCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Esta clase es una implementación personalizada de UserDetailsService para manejar
 * la autenticación de usuarios en la aplicación. Utiliza el UserRepository para
 * buscar y cargar la información del usuario en la base de datos.
 * <br>
 * Es una implementación de la interfaz UserDetailsService de Spring Security.
 * Su propósito es proporcionar una forma de cargar y devolver los detalles del usuario en un objeto UserDetails,
 * que es necesario para la autenticación y autorización en Spring Security.
 * <br>
 * En resumen, esta clase se utiliza para cargar los detalles de usuario
 * (como el nombre de usuario, la contraseña y los roles)
 * desde el repositorio de usuarios de la aplicación y devolverlos en un objeto
 * UserDetails para ser utilizado por Spring Security en la autenticación y autorización de los usuarios.
 */
@Service
public class UsuarioSecurityImpl  implements IUsuarioService, UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    private final GetBCryptPasswordEncoder encoder;

    public UsuarioSecurityImpl(GetBCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String getEncodedPassword(Usuario usuario) {
        String passwd = usuario.getPassword();
        String encodedPasswod = encoder.bCryptPasswordEncoder().encode(passwd);
        return encodedPasswod;
    }
    @Override
    /*public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername email : " + email);
        Usuario  usuario= usuarioRepository.findUsuarioByEmail(email);
        System.out.println("loadUserByUsername usuario : " + usuario.getNombreUsuario());

        org.springframework.security.core.userdetails.User springUser=null;

        Set<GrantedAuthority> ga = new HashSet<>();
        for (Role item : usuario.getRoles()){
            ga.add(new SimpleGrantedAuthority(item.getRoleName()));
        }
        springUser = new org.springframework.security.core.userdetails.User(
                email,
                usuario.getPassword(),
                ga );
        return springUser;
    }
    */
    public ImprovedUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername email : " + email);
        Optional<Usuario> usuario= usuarioRepository.findUsuarioByEmail(email);
        ImprovedUserDetails improvedUserDetails = new ImprovedUserDetails();
        if(usuario.isPresent()){
            System.out.println("loadUserByUsername usuario : " + usuario.get().getNombreUsuario());

            //Crear nuestra clase
            improvedUserDetails.setUsername(usuario.get().getEmail());
            improvedUserDetails.setPassword(usuario.get().getPassword());
            improvedUserDetails.setUserID((int) usuario.get().getId());
            improvedUserDetails.setNombredemiperro("Duna");
            //falta el grantedauthorities
            improvedUserDetails.setAuthorities(mapRolesToAuthorities(usuario.get().getRoles()));
            //Solo si el usuario está genero sus clave pública y privada
            //creamos la clave pulbico/privada
            KeyPairGenerator keyPairGenerator = null;
            try {
                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            //Almacenamos los datos a nivel de usuario y de cookie de seguridad
            improvedUserDetails.setPublickey(keyPair.getPublic());
            improvedUserDetails.setPrivatekey(keyPair.getPrivate());
            //Actualizamos el usuario
            Usuario usuarioGuardar = usuario.get();
            usuarioGuardar.setPublickey(keyPair.getPublic().getEncoded());
            usuarioGuardar.setPrivatekey(keyPair.getPrivate().getEncoded());
        } else{
            //Busco los roles de anonimo
            Optional<Usuario> usuarioanonimo= usuarioRepository.findUsuarioByEmail("anonimoa@anonimo.com");

            if (usuarioanonimo.isPresent()){
                //Crear nuestra clase
                improvedUserDetails.setUsername(usuario.get().getEmail());
                improvedUserDetails.setPassword(usuario.get().getPassword());
                improvedUserDetails.setUserID((int) usuario.get().getId());
                improvedUserDetails.setNombredemiperro("Duna");
                //falta el grantedauthorities
                improvedUserDetails.setAuthorities(mapRolesToAuthorities(usuario.get().getRoles()));

            } else {
                improvedUserDetails.setUsername("anonimoa@anonimo.com");
                improvedUserDetails.setNombredemiperro("no encontrado");
            }

        }



        System.out.println("cookie creada");
        return improvedUserDetails;
    }
    /**
     * Esta función auxiliar se utiliza para convertir la lista de roles del usuario en una colección de
     * autoridades que pueden ser utilizadas por Spring Security.
     *
     * @param roles Lista de roles del usuario.
     * @return Collection< ? extends GrantedAuthority> Colección de autoridades.
     */
    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        // Utilizar streams de Java para mapear cada rol a una instancia de SimpleGrantedAuthority
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList(); // Convertir el stream en una lista
    }
}
