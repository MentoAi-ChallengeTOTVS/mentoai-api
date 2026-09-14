package com.mentoai.mentoaiapi.alert.presentation.rest.controller;

import com.mentoai.mentoaiapi.alert.application.service.AlertaService;
import com.mentoai.mentoaiapi.alert.application.service.AlertaUsuarioService;
import com.mentoai.mentoaiapi.alert.domain.entity.Alerta;
import com.mentoai.mentoaiapi.alert.domain.entity.AlertaUsuario;
import com.mentoai.mentoaiapi.alert.presentation.rest.mapper.AlertaRestMapper;
import com.mentoai.mentoaiapi.alert.presentation.rest.response.AlertaResponse;
import com.mentoai.mentoaiapi.alert.presentation.rest.response.AlertaUsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController {

    private final AlertaService alertaService;
    private final AlertaUsuarioService alertaUsuarioService;
    private final AlertaRestMapper mapper;

    public AlertaController(AlertaService alertaService,
                            AlertaUsuarioService alertaUsuarioService,
                            AlertaRestMapper mapper) {
        this.alertaService = alertaService;
        this.alertaUsuarioService = alertaUsuarioService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Page<AlertaResponse>> listar(
            @PageableDefault(page = 0, size = 10, sort = "criacao") Pageable pageable) {
        Page<AlertaResponse> page = alertaService.listar(pageable)
                .map(mapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponse> buscarPorId(@PathVariable Long id) {
        Alerta alerta = alertaService.buscarPorId(id);
        return ResponseEntity.ok(mapper.toResponse(alerta));
    }

    @PatchMapping("/{id}/lido")
    public ResponseEntity<AlertaUsuarioResponse> marcarComoLido(@PathVariable Long id) {
        AlertaUsuario alertaUsuario = alertaUsuarioService.marcarComoLido(id);
        return ResponseEntity.ok(mapper.toUsuarioResponse(alertaUsuario));
    }
}