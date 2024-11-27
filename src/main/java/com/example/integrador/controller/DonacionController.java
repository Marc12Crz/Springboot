package com.example.integrador.controller;



import com.example.integrador.model.Albergue;
import com.example.integrador.model.Producto;

import com.example.integrador.repository.ProductoRepository;

import com.example.integrador.service.AlbergueService;
import com.example.integrador.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DonacionController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private AlbergueService albergueService;

    @GetMapping("/donaciones")
    public String mostrarProductosDonacion(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        return "donaciones";
    }
    @GetMapping("/donar/{id}")
    public String donarProducto(@PathVariable("id") Integer idProducto, Model model) {
        Producto producto = productoService.obtenerProductoPorId(idProducto);
        model.addAttribute("producto", producto);
        List<Albergue> albergues = albergueService.listarAlbergues();
        model.addAttribute("albergues", albergues);
        return "formulario_donacion";
    }
    @PostMapping("/procesar_donacion")
    public String procesarDonacion(@RequestParam("idProducto") Integer idProducto,
                                   @RequestParam("idAlbergue") Integer idAlbergue,
                                   @RequestParam("mensaje") String mensaje,
                                   RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("mensaje", "Gracias por tu donación!");
        return "DonacionExitosa";
    }

}
