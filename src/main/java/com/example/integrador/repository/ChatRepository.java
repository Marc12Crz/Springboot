package com.example.integrador.repository;

import com.example.integrador.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    // Buscar mensajes por usuario
    @Query("SELECT c FROM Chat c WHERE c.usuario.id = :idUsuario")
    List<Chat> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    // Buscar mensajes por albergue
    @Query("SELECT c FROM Chat c WHERE c.albergue.idAlbergue = :idAlbergue")
    List<Chat> findByAlbergueId(@Param("idAlbergue") Long idAlbergue);

    // Buscar mensajes por usuario y albergue
    @Query("SELECT c FROM Chat c WHERE c.usuario.id = :userId AND c.albergue.idAlbergue = :albergueId")
    List<Chat> findByUsuarioAndAlbergue(@Param("userId") Long userId, @Param("albergueId") Long albergueId);
}
