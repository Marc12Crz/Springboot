package com.example.integrador.service;

import com.example.integrador.model.Albergue;
import com.example.integrador.repository.AlbergueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbergueService {

    @Autowired
    private AlbergueRepository albergueRepository;

    /**
     * Listar todos los albergues.
     * @return Lista de albergues.
     */
    public List<Albergue> listarAlbergues() {
        return albergueRepository.findAll();
    }

    /**
     * Obtener un albergue por su ID.
     * @param id ID del albergue.
     * @return Albergue encontrado o null si no existe.
     */
    public Albergue obtenerPorId(Long id) {
        Optional<Albergue> albergue = albergueRepository.findById(id);
        return albergue.orElse(null);
    }

    /**
     * Guardar o actualizar un albergue.
     * @param albergue Objeto albergue a guardar o actualizar.
     * @return Albergue guardado o actualizado.
     */
    public Albergue guardarAlbergue(Albergue albergue) {
        return albergueRepository.save(albergue);
    }

    /**
     * Eliminar un albergue por su ID.
     * @param id ID del albergue a eliminar.
     * @return true si se eliminó correctamente, false si no existe.
     */
    public boolean eliminarAlbergue(Long id) {
        if (albergueRepository.existsById(id)) {
            albergueRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Buscar albergues por departamento.
     * @param departamento Nombre del departamento.
     * @return Lista de albergues en el departamento especificado.
     */
    public List<Albergue> buscarPorDepartamento(String departamento) {
        return albergueRepository.findByDepartamento(departamento);
    }

    /**
     * Buscar albergues por distrito.
     * @param distrito Nombre del distrito.
     * @return Lista de albergues en el distrito especificado.
     */
    public List<Albergue> buscarPorDistrito(String distrito) {
        return albergueRepository.findByDistrito(distrito);
    }
}
