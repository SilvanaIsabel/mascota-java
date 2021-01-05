
package edu.egg.mascota.web.servicio;

import edu.egg.mascota.web.entidades.Foto;
import edu.egg.mascota.web.entidades.Mascota;
import edu.egg.mascota.web.entidades.Usuario;
import edu.egg.mascota.web.enumeracion.Sexo;
import edu.egg.mascota.web.errores.ErrorServicio;

import edu.egg.mascota.web.repositorio.MascotaRepositorio;
import edu.egg.mascota.web.repositorio.UsuarioRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;


public class MascotaServicio {
     @Autowired
    private UsuarioRepositorio usuarioRepositorio;
     @Autowired
     private MascotaRepositorio  mascotaRepositorio;
     @Autowired
     private FotoServicio fotoServicio; // se hace el vinculo
  
     
// SI FALLA ALGO DEL SERVICIO DE MASCOTA ESA TRANSACCION SE VUELVE ATRAS Y NO SE IMPACTA NADA EN LA BASE DE DATOS   
//METODO AGREGAR MASCOTA
      @Transactional
     public void agregarMacota( MultipartFile archivo, String idUsuario, String nombre, Sexo sexo)throws ErrorServicio {
      Usuario usuario= usuarioRepositorio.findById(idUsuario).get();
      validar(nombre,sexo);
      
      Mascota mascota = new Mascota();
      mascota.setNombre(nombre);
      mascota.setSexo(sexo);
      mascota.setAlta(new Date());
      
      // SI FALLA ALGO EN EL SERVIVIO DE FOTOS TB SE VUELVE ATRAS
      Foto foto = fotoServicio.guardar(archivo);
      mascota.setFoto(foto);
      
      mascotaRepositorio.save(mascota);
  }
  

//METODO MODIFICAR
  @Transactional
  public void modificar( MultipartFile archivo,String idUsuario, String idMascota, String nombre ,Sexo sexo)throws ErrorServicio {
     validar(nombre,sexo);
     Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
     if(respuesta.isPresent()){
       Mascota mascota = respuesta.get();
       if(mascota.getUsuario().getId().equals(idUsuario)){
           mascota.setNombre(nombre);
           mascota.setSexo(sexo);
           
           String idFoto= null;
           if(mascota.getFoto() != null){
               idFoto = mascota.getFoto().getId();
           }
           
           Foto foto=fotoServicio.actualizar(idFoto, archivo);
           mascota.setFoto(foto);
          
           mascotaRepositorio.save(mascota);
          
           // si no es el mismo usuario
       }else{
         throw  new  ErrorServicio("No tiene permisos solicitados para realizar la operacion");   
       }
     }else{
         throw  new ErrorServicio("No existe una mascota con el identificador solicitado");
     }
  }


// METODO ELIMINAR
    @Transactional
  public void eliminar(String idUsuario, String idMascota) throws ErrorServicio{
      // lo q hace es buscar la mascota por id
       Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
     if(respuesta.isPresent()){
      Mascota mascota = respuesta.get();
       if(mascota.getUsuario().getId().equals(idUsuario)){
       mascota.setBaja(new Date());
       }
  }else{
          throw  new ErrorServicio("No existe una mascota con el identificador solicitado");
       }
     }
  
       
         
     

    
  // EL METODO VALIDAR NO ES TRANSACCIONAL
  //METODO VALIDAR
  public void validar(String nombre, Sexo sexo) throws ErrorServicio{
      if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre de la mascota no puede ser nulo o vacio");
        }
      if(sexo == null ){
            throw new ErrorServicio("El sexo de la mascota no puede ser nulo");
        }
  }
}