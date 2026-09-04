package com.mentoai.mentoaiapi.meeting.presentation.rest.controller;

import com.mentoai.mentoaiapi.meeting.application.service.ClienteService;
import com.mentoai.mentoaiapi.meeting.application.service.ReuniaoService;
import com.mentoai.mentoaiapi.meeting.presentation.rest.mapper.ReuniaoRestMapper;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.ReuniaoResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ReuniaoService reuniaoService;
    private final ReuniaoRestMapper reuniaoMapper;

    public ClienteController(
            ClienteService clienteService,
            ReuniaoService reuniaoService,
            ReuniaoRestMapper reuniaoMapper) {
        this.clienteService = clienteService;
        this.reuniaoService = reuniaoService;
        this.reuniaoMapper = reuniaoMapper;
    }

    @GetMapping("/{id}/reunioes")
    public ResponseEntity<List<ReuniaoResponse>> listarReunioes(@PathVariable Long id) {
        return ResponseEntity.ok(
                reuniaoService.listarPorCliente(id).stream().map(reuniaoMapper::toResponse).toList());
    }
}
