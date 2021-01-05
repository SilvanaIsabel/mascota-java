
package edu.egg.mascota.web.repositorio;

import edu.egg.mascota.web.entidades.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepositorio extends JpaRepository<Zona , String> {
    
}
