package com.example.integrador.service;

import com.example.integrador.model.Producto;
import com.example.integrador.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    public Producto obtenerProductoPorId(int idProducto) {
        Optional<Producto> producto = productoRepository.findById(idProducto);
        return producto.orElse(null);
    }
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }
}