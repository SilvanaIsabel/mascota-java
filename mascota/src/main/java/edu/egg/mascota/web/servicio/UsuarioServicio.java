
package edu.egg.mascota.web.servicio;


import edu.egg.mascota.web.entidades.Foto;
import edu.egg.mascota.web.entidades.Usuario;
import edu.egg.mascota.web.entidades.Zona;
import edu.egg.mascota.web.errores.ErrorServicio;
import edu.egg.mascota.web.repositorio.UsuarioRepositorio;
import edu.egg.mascota.web.repositorio.ZonaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;





@Service
public class UsuarioServicio implements UserDetailsService{
    
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    // VAMOS A LLAMAR ESTE SERVICIO CUANDO YO ME REGISTRO
//    @Autowired
//    private NotificacionServicio notificacionServicio;
    
     @Autowired
  private ZonaRepositorio zonaRepositorio;
    
      @Autowired
      private FotoServicio fotoServicio;
    
    // METODO REGISTRAR
       @Transactional
    public void registrar( MultipartFile archivo,String nombre, String apellido, String mail, String clave,String clave2, String idZona) throws ErrorServicio{
        Zona zona = zonaRepositorio.getOne(idZona); 
        validar(nombre,apellido,mail,clave,clave2,zona);
       
         
       
       
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        
        usuario.setZona(zona);
        // clave incriptada
        String enciptada=new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(enciptada);
        
        usuario.setAlta(new Date());
        
        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);
        
        usuarioRepositorio.save(usuario);
        
        // CREAMOS LA ACCION PARA ENVIAR EL MAIL AL USUARIO RECION REGISTRADO
        // UNA VEZ QUE EL USUARIO SE TERMINA DE REGISTRAR SE LE DA BIENVENIDA
//        notificacionServicio.enviar("Bienvenido al tinder de mascotas", "Tinder de mascota", usuario.getMail());
    }
    

//METODO MODIFICAR
////    @Transactional
    public void modificar(MultipartFile archivo, String id,String nombre, String apellido, String mail, String clave,String clave2,String idZona)throws ErrorServicio{
        
        Zona zona = zonaRepositorio.getOne(idZona);
        
        validar(nombre,apellido,mail,clave,clave2,zona);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if(respuesta.isPresent()){
       
        Usuario usuario = respuesta.get();
        usuario.setApellido(apellido);
        usuario.setNombre(nombre);
        usuario.setMail(mail);
        usuario.setZona(zona);
        
        // clave incriptada
        String enciptada=new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(enciptada);
       
        
        String idFoto= null;
        
        if(usuario.getFoto() != null){
            idFoto=usuario.getFoto().getId();
        }
        Foto foto = fotoServicio.actualizar(idFoto, archivo);
        usuario.setFoto(foto);
        
        usuarioRepositorio.save(usuario);
        }else{
            throw  new ErrorServicio("No se encontro el error solicitado");
        }
    }
   
    
// METODO DESHABILITAR
    @Transactional
    public void deshabilitar(String id) throws ErrorServicio{
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if(respuesta.isPresent()){
        Usuario usuario = respuesta.get();
        usuario.setBaja(new Date());
        
        usuarioRepositorio.save(usuario);
        }else{
            throw  new ErrorServicio("No se encontro el usuario solicitado");
        }
    }
    
    
    // metodo HABILITAR
    @Transactional
     public void habilitar(String id) throws ErrorServicio{
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if(respuesta.isPresent()){
        Usuario usuario = respuesta.get();
        usuario.setBaja(null);
        
        usuarioRepositorio.save(usuario);
        }else{
            throw  new ErrorServicio("No se encontro el usuario solicitado");
        }
    }
    
   
     //METODO VALIDAR
   public void validar( String nombre, String apellido, String mail, String clave, String clave2, Zona zona) throws ErrorServicio{
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre del usuario no puede ser  nulo");
        }
        
         if(apellido == null || apellido.isEmpty()){
            throw new ErrorServicio("El apellido  del usuario no puede ser nulo");
        }
          if(mail== null || mail.isEmpty()){
            throw new ErrorServicio("El mail  del usuario no puede ser nulo");
        }
            if(clave== null || clave.isEmpty()|| clave.length()<6){
            throw new ErrorServicio("La clave  del usuario no puede ser nulo");
        }
            
            if(!clave.equals(clave2)){
                throw new ErrorServicio("las claves deben ser iguales");
            }
            if(zona == null){
                 throw new ErrorServicio("No se encontro la zona solicitada");
            }
    }

    
   @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.BuscarPorMail(mail);
        if(usuario != null){
            List<GrantedAuthority>permisos= new ArrayList<>();
            
            GrantedAuthority p1= new SimpleGrantedAuthority ("Role_Usuario_Registrado");
            permisos.add(p1);

           
             ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true); 
            session.setAttribute("usuariosession", usuario);


            User user = new User(usuario.getMail(),usuario.getClave(),permisos);
            return user;
        }else{
            return null;
        }
    }  

    
}  

 