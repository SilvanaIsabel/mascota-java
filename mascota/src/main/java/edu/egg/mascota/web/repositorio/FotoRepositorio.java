
package edu.egg.mascota.web.repositorio;

import edu.egg.mascota.web.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface  FotoRepositorio extends JpaRepository<Foto,String> {
    
}
