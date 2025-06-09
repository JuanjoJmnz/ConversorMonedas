package com.juanjojmnz.conversormonedas.Controller;

import com.juanjojmnz.conversormonedas.Entities.Conversor;
import com.juanjojmnz.conversormonedas.Service.ConversorServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ConversorController {

    private final ConversorServicio exchangeRateService;

    @GetMapping("/")
    public String form(Model model) {
        model.addAttribute("conversion", new Conversor());
        model.addAttribute("monedas", exchangeRateService.obtenerMonedasDisponibles());
        return "index";
    }

    @PostMapping("/convertir")
    public String convertir(@ModelAttribute Conversor conversion, Model model) {
        Conversor resultado = exchangeRateService.convertir(
                conversion.getMonedaOrigen(),
                conversion.getMonedaDestino(),
                conversion.getCantidad()
        );
        model.addAttribute("resultado", resultado);
        model.addAttribute("monedas", exchangeRateService.obtenerMonedasDisponibles());
        return "resultado";
    }
}