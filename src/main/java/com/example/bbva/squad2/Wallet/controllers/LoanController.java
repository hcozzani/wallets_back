package com.example.bbva.squad2.Wallet.controllers;

import com.example.bbva.squad2.Wallet.dtos.LoanSimulationDTO;
import com.example.bbva.squad2.Wallet.exceptions.WalletsException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Value("${loan.interest-rate}")
    private double tasaDeInteres;

    @PostMapping("/simulate")
    @Operation(summary = "Simulador de prestamos para el usuario loggeado")
    public ResponseEntity<LoanSimulationDTO> simulateLoan(
            @RequestParam double monto,
            @RequestParam int meses) {

        if (monto <= 0 || meses <= 0) {
            throw new WalletsException(HttpStatus.BAD_REQUEST, "El monto y el plazo deben ser mayores a cero.");
        }

        // Calcular intereses, cuotas y pagos
        double interesMensual = monto * tasaDeInteres;
        double interesTotal = interesMensual * meses;
        double totalAPagar = monto + interesTotal;
        double cuotaMensual = totalAPagar / meses;

        // Crear el DTO con los datos calculados
        LoanSimulationDTO simulation = new LoanSimulationDTO(
                monto,
                meses,
                cuotaMensual,
                totalAPagar,
                tasaDeInteres
        );

        return ResponseEntity.ok(simulation);
    }
}
