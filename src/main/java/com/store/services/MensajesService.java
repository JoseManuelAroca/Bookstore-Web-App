package com.store.services;


import com.store.config.details.ImprovedUserDetails;
import com.store.dto.MensajesDto;
import com.store.dto.RoleDto;
import com.store.entity.Mensajes;
import com.store.entity.Role;
import com.store.entity.Usuario;
import com.store.repository.MensajesRepository;
import com.store.repository.RoleRepository;
import com.store.repository.UsuarioRepository;
import com.store.services.mapper.MensajesServiceMapper;
import com.store.services.mapper.RoleServiceMapper;
import com.store.util.GetContexHolder;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MensajesService extends AbstractBusinessService<Mensajes, Long, MensajesDto, MensajesRepository,
        MensajesServiceMapper> {

    private final UsuarioRepository usuarioRepository;

    private GetContexHolder getContexHolder;
    protected MensajesService(MensajesRepository repository, MensajesServiceMapper serviceMapper, UsuarioRepository usuarioRepository) {
        super(repository, serviceMapper);
        this.usuarioRepository = usuarioRepository;
    }

    public List<MensajesDto> buscarTodosAlta() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //Leer la lista de mensajes
        List<MensajesDto> mensajesDtos = new ArrayList<>();

        //Obtengo la lista de entidades mensajes
        List<Mensajes> mensajes = this.getRepo().findAll();
        //Leo la cookie de seguridad para obtener la clave privada
        ImprovedUserDetails improvedUserDetails = getContexHolder.GetCookieImproved();


        //Decodificar los mensajes con la clave publica del receptor
        //Recorrer la lista de entidades y sustituir el mensaje por el mensaje decodificado
        Iterator<Mensajes> it = mensajes.iterator();

        // mientras al iterador queda proximo juego
        while(it.hasNext()){
            //Obtenemos la password de a entidad
            //Datos del usuario
            MensajesDto dto = new MensajesDto();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.map(dto,it.next());
            // Sustituyo el mensaje
            dto.setMensajeDesencriptado(desencriptarConClavePrivada(dto.getMensajeEncriptado()));
            //Añado DTO a la lista
            mensajesDtos.add(dto);
        }


        //Devolver el listado
        return  mensajesDtos;
    }

    public MensajesDto enviarMensaje(MensajesDto dto) throws Exception {
        //Necesito la clave publica del receptor


        //Encriptamos el mensaje
        dto.setMensajeEncriptado(encriptarConClavePublica(dto.getMensaje(), dto.getId_receptor()));
        //Guardamos el registro
        MensajesDto mensajesDto = guardar(dto);
        return mensajesDto;

    }

    //Metodo para encriptar con clave publica
    public byte[] encriptarConClavePublica(String textoMensaje,Long id) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("RSA");
        //obtenemos la clave pública
        Usuario usuarioReceptor = usuarioRepository.findById(id);
        PublicKey publicKey =
                KeyFactory.getInstance("RSA").
                        generatePublic(new X509EncodedKeySpec(usuarioReceptor.getPublickey()));
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(textoMensaje.getBytes());
    }

    //Metodo para desencrtptar con clave privada
    public String desencriptarConClavePrivada(byte[] hash) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //Leo cookie de seguridad
        ImprovedUserDetails improvedUserDetails = getContexHolder.GetCookieImproved();
        //Desencriptamos
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,improvedUserDetails.getPrivatekey());
        //Convierto a texo
        String texto = new String(cipher.doFinal(hash), StandardCharsets. UTF_8 );
        return texto;
    }
}
