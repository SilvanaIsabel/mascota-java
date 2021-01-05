/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.mascota.web.servicio;

import edu.egg.mascota.web.entidades.Mascota;
import edu.egg.mascota.web.entidades.Voto;
import edu.egg.mascota.web.errores.ErrorServicio;
import edu.egg.mascota.web.repositorio.MascotaRepositorio;
import edu.egg.mascota.web.repositorio.VotoRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoServicio {
    // LO PRIMERO QUE HACEMOS ES CREAR EL VINCULO EN LA CLASE
     
    // VAMOS A VINCULAR LA NOTIFICACION A LAS ACCIONES DE VOTACION
//     @Autowired
//    private NotificacionServicio notificacionServicio;
    @Autowired
    private VotoRepositorio votoRepositorio;
     @Autowired
    private MascotaRepositorio mascotaRepositorio;
    
     
    public void votar(String idUsuario, String idMascota1,String idMascota2)throws ErrorServicio{
       Voto voto = new  Voto();
       voto.setFecha(new Date());
       
         // validacion el id de la mascota 1 sea distinto al id de la mascota 2
        if(idMascota1.equals(idMascota2)){
        throw new ErrorServicio("No puede votarse a si mismo");
    }  
       
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota1);
        if(respuesta.isPresent()){
            Mascota mascota1= respuesta.get();
            if(mascota1.getUsuario().getId().equals(idUsuario)){
                voto.setMascota1(mascota1);
            }else{
              throw new ErrorServicio("No tiene los permisos para realizar la operacion");
            }
        }else{
            throw new ErrorServicio("No existe esa mascota vinculada a ese icentificador");
        }
        //  MASCOTA2
        Optional<Mascota>respuesta2 = mascotaRepositorio.findById(idMascota2);
        if(respuesta2.isPresent()){
            Mascota mascota2 = respuesta2.get();
            voto.setMascota2(mascota2);
            
            //CREAMOS LA ACCION PARA ENVIAR UN MAIL CUANDO LA MASCOTA FUE VOTADA
//            notificacionServicio.enviar("Tu mascota ha sido votada","Tinder de mascota", mascota2.getUsuario().getMail());
        }else{
            throw new ErrorServicio("No existe esa mascota vinculada a ese identificador");
        }
          votoRepositorio.save(voto);
    }
    
   //METODO RECIBIR UN voto
    
    public void responder( String idUsuario,String idVoto)throws ErrorServicio{
       // LO PRIMERO QUE HACEMOS ES BUSCAR AL VOTO
       
       Optional<Voto> respuesta= votoRepositorio.findById(idVoto);
       if(respuesta.isPresent()){
           Voto voto = respuesta.get();
           voto.setRespuesta(new Date());
           
           if(voto.getMascota2().getUsuario().getId().equals(idUsuario)){
               
               // CREAMOS LA ACCION PARA ENVIAR UN MAIL CUANDO SE RESPONDE A UN VOTO
               // le pedimos al usuario el mail
//               notificacionServicio.enviar("Tu voto fue correspondido","Tinder de mascota",voto.getMascota1().getUsuario().getMail());
               votoRepositorio.save(voto); 
           
           }else{
               throw new ErrorServicio("No tiene permiso para realizar la operacion");
           }
           }else{
           throw new ErrorServicio("No existe el voto solicitado");
       }
           
    }
}
