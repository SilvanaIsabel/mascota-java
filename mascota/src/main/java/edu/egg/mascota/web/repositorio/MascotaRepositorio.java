/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.mascota.web.repositorio;

import edu.egg.mascota.web.entidades.Mascota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota , String>{
    @Query("SELECT c FROM Mascota c Where c.usuario.id = :id")
    public List<Mascota>BuscarMascotasPorUsuario(@Param("id")String id);
}
