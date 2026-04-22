package com.akdpro.api.services;

import com.akdpro.api.models.Sede;
import com.akdpro.api.repositories.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SedeService {

    @Autowired
    private SedeRepository sedeRepository;

    public List<Sede> listarSedes() {
        List<Sede> sedes = sedeRepository.findAll();
        System.out.println("Sedes encontradas en DB: " + sedes.size()); // Mira esto en la consola de IntelliJ
        return sedes;
    }

    public Sede crearSede(Sede sede) {
        return sedeRepository.save(sede);
    }

    public Sede actualizarSede(Long id, Sede detallesSede) throws Exception {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new Exception("Sede no encontrada con ID: " + id));

        sede.setNombre(detallesSede.getNombre());
        sede.setDireccion(detallesSede.getDireccion());
        sede.setCiudad(detallesSede.getCiudad());

        return sedeRepository.save(sede);
    }

    public void eliminarSede(Long id) {
        sedeRepository.deleteById(id);
    }
}
