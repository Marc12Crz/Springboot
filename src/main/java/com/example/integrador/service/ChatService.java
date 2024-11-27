package com.example.integrador.service;

import com.example.integrador.model.Chat;
import com.example.integrador.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    // Obtener mensajes por usuario
    public List<Chat> getMessagesByUser(Long userId) {
        return chatRepository.findByUsuarioId(userId);
    }

    // Obtener mensajes por albergue
    public List<Chat> getMessagesByAlbergue(Long idAlbergue) { // Cambié Integer por Long
        return chatRepository.findByAlbergueId(idAlbergue);
    }

    // Guardar un nuevo mensaje de chat
    public Chat saveMessage(Chat chat) {
        chat.setFechaEnvio(LocalDateTime.now()); // Configurar fecha de envío antes de guardar
        return chatRepository.save(chat);
    }

    // Obtener mensajes por usuario y albergue
    public List<Chat> getMessagesByUserAndAlbergue(Long userId, Long albergueId) {
        return chatRepository.findByUsuarioAndAlbergue(userId, albergueId);
    }
}
