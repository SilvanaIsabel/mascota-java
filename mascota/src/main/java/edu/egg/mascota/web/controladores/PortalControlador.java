
package edu.egg.mascota.web.controladores;

import edu.egg.mascota.web.entidades.Zona;
import edu.egg.mascota.web.errores.ErrorServicio;
import edu.egg.mascota.web.repositorio.ZonaRepositorio;
import edu.egg.mascota.web.servicio.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

// UN CLASE DE TIPO CONTROLADOR
@Controller
@RequestMapping("/")// CONFIGURA CUAL ES LA URL
public class PortalControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;
    
     @Autowired
    private ZonaRepositorio zonaRepositorio;
    
    @GetMapping("/")// EJECUTA TODO LO QUE ESTA DENTRO ESTE METODO
    public String index(){
        return "index.html";// FINALMENTE DEVUELVE LA VISTA
    }
    
    @PreAuthorize("hasAnyRole('Role_Usuario_Registrado')")// SI ES UN USUARIO REGISTRADO VA A PODER ENTRAR A LA URL BARRA INICIO
    @GetMapping("/inicio")// EJECUTA TODO LO QUE ESTA DENTRO ESTE METODO
    public String inicio(){
        return "inicio.html";
    }
    
    
      @GetMapping  ("/login")// EJECUTA TODO LO QUE ESTA DENTRO ESTE METODO
    public String login(@RequestParam(required=false)String error , @RequestParam(required=false)String logout,  ModelMap modelo){
        // si y solo si este error es distinto de nulo
        if(error != null){
            
        modelo.put("error", " Nombre de Usuario o Clave incorrecta");
        }
        // si logout es distinto a null
        if(logout !=null){
            modelo.put("logout", "Ha salido correctamente de la plataforma");
        }
        return "login.html";// FINALMENTE DEVUELVE LA VISTA
    }
    
    
      @GetMapping("/registro")// EJECUTA TODO LO QUE ESTA DENTRO ESTE METODO
    public String registro(ModelMap modelo){
        List<Zona> zonas= zonaRepositorio.findAll();
        modelo.put("zonas", zonas);
        return "registro.html";// FINALMENTE DEVUELVE LA VISTA
    }
    
       @PostMapping("/registrar")// EJECUTA TODO LO QUE ESTA DENTRO ESTE METODO
    public String registrar(ModelMap modelo,MultipartFile archivo,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String mail,@RequestParam String clave1,@RequestParam String clave2,@RequestParam String idZona){
        
        try {
            usuarioServicio.registrar(archivo, nombre, apellido, mail, clave1,clave2,idZona);
        } catch (ErrorServicio ex) {
            
        List<Zona> zonas= zonaRepositorio.findAll();
        modelo.put("zonas", zonas);
            
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave", clave1);//la clave tiene que tener 6 digitos
            modelo.put("clave2", clave2);
           return"registro.html";
        }
         
        modelo.put("titulo","Bienvenido al Tinder de Mascotas");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satifactoria");
        return "exito.html";// FINALMENTE DEVUELVE LA VISTA
    }
}
