package com.akdpro.api.services;

import com.akdpro.api.models.Producto;
import com.akdpro.api.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TiendaService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto agregarAccesorio(Producto producto) {
        return productoRepository.save(producto);
    }

    public void actualizarStock(Long id, Integer nuevoStock) throws Exception {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
