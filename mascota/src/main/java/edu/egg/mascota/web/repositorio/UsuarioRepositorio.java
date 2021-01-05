
package edu.egg.mascota.web.repositorio;

import edu.egg.mascota.web.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario , String> {
    @Query("SELECT c FROM Usuario c Where c.mail = :mail")
    public Usuario BuscarPorMail(@Param("mail")String mail );
}
