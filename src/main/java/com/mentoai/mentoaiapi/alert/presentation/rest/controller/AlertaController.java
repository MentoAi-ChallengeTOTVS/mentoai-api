package com.mentoai.mentoaiapi.alert.presentation.rest.controller;

import com.mentoai.mentoaiapi.alert.application.service.AlertaService;
import com.mentoai.mentoaiapi.alert.application.service.AlertaUsuarioService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController {

    private final AlertaService alertaService;
    private final AlertaUsuarioService alertaUsuarioService;

    public AlertaController(AlertaService alertaService, AlertaUsuarioService alertaUsuarioService) {
        this.alertaService = alertaService;
        this.alertaUsuarioService = alertaUsuarioService;
    }
    @GetMapping
    public String getAlertaService() {
        return "teste";
    }
}
