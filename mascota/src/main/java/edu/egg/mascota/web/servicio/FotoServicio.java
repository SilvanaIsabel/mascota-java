
package edu.egg.mascota.web.servicio;

import edu.egg.mascota.web.entidades.Foto;
import edu.egg.mascota.web.errores.ErrorServicio;
import edu.egg.mascota.web.repositorio.FotoRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {
    @Autowired
    private FotoRepositorio fotoRepositorio;
    
    // METODO PARA GUARDAR LA FOTO
    @Transactional//si el metodo se ejecuta sin largar excepciones, entonces se hace un comit a la base de datos y se aplican todos los cambios
  public Foto guardar(MultipartFile archivo) throws ErrorServicio{
      if(archivo != null){
          
          try{
            Foto foto = new Foto(); 
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());

        return  fotoRepositorio.save(foto);
          }catch(Exception e){
              System.err.println(e.getMessage());
          }
      }
          return null;
      }
 
  
// METODO PARA ACTUALIZAR LA FOTO
   @Transactional
  public Foto actualizar(String idFoto, MultipartFile archivo)throws ErrorServicio{
      if(archivo != null){
          
          try{
            Foto foto = new Foto(); 
            if(idFoto != null){
                Optional<Foto>respuesta=fotoRepositorio.findById(idFoto);
                if(respuesta.isPresent()){
                    foto=respuesta.get();
                }
            }
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());

        return  fotoRepositorio.save(foto);
          }catch(Exception e){
              System.err.println(e.getMessage());
          }
      }
          return null;
  }
  } 

