package com.example.integrador.controller;

import com.example.integrador.model.Albergue;
import com.example.integrador.model.Donacion;
import com.example.integrador.model.Producto;
import com.example.integrador.service.AlbergueService;
import com.example.integrador.service.DonacionService;
import com.example.integrador.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donaciones")
public class DonacionController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private AlbergueService albergueService;
    @Autowired
    private DonacionService donacionService;

    @PostMapping("/guardar")
    public ResponseEntity<?> crearDonacion(@RequestBody Donacion donacion) {
        System.out.println("Inicio de crearDonacion");

        if (donacion.getProducto() == null || donacion.getProducto().getIdProducto() <= 0) {
            System.out.println("ID de Producto es inválido.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de Producto no puede ser nulo o cero");
        }
        if (donacion.getAlbergue() == null || donacion.getAlbergue().getIdAlbergue() == null) {
            System.out.println("ID de Albergue es nulo.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de Albergue no puede ser nulo");
        }

        try {
            Producto producto = productoService.obtenerProductoPorId(donacion.getProducto().getIdProducto());
            Albergue albergue = albergueService.obtenerPorId(donacion.getAlbergue().getIdAlbergue());

            if (producto == null) {
                System.out.println("Producto no encontrado.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Producto no encontrado");
            }
            if (albergue == null) {
                System.out.println("Albergue no encontrado.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Albergue no encontrado");
            }

            donacion.setProducto(producto);
            donacion.setAlbergue(albergue);
            Donacion nuevaDonacion = donacionService.guardarDonacion(donacion);
            System.out.println("Donación guardada exitosamente: " + nuevaDonacion.getId_donacion());
            return ResponseEntity.ok(nuevaDonacion);
        } catch (Exception e) {
            System.out.println("Error al guardar la donación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la donación: " + e.getMessage());
        }
    }





    /**
     * Endpoint para listar todos los productos disponibles para donación.
     *
     * @return ResponseEntity con la lista de productos.
     */
    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }
    @GetMapping("/albergues")
    public ResponseEntity<List<Albergue>> listarAlbergues() {
        List<Albergue> albergues = albergueService.listarAlbergues();
        if (albergues.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(albergues);
    }

}
