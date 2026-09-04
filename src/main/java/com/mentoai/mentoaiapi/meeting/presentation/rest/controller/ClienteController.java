package com.mentoai.mentoaiapi.meeting.presentation.rest.controller;

import com.mentoai.mentoaiapi.meeting.application.service.ClienteService;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.presentation.rest.mapper.ClienteRestMapper;
import com.mentoai.mentoaiapi.meeting.presentation.rest.request.AlterarStatusClienteRequest;
import com.mentoai.mentoaiapi.meeting.presentation.rest.request.AtualizarClienteRequest;
import com.mentoai.mentoaiapi.meeting.presentation.rest.request.CriarClienteRequest;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.ClienteResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRestMapper clienteRestMapper;

    public ClienteController(ClienteService clienteService, ClienteRestMapper clienteRestMapper)
    {
        this.clienteService = clienteService;
        this.clienteRestMapper = clienteRestMapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody CriarClienteRequest request) 
    {
        Cliente cliente = clienteService.criar(
            request.nome(),
            request.segmento(),
            request.porte()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRestMapper.toResponse(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) 
    {
        Cliente cliente = clienteService.buscarPorId(id);

        return ResponseEntity.ok(clienteRestMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarClienteRequest request) {

        Cliente cliente = clienteService.atualizar(
                id,
                request.nome(),
                request.segmento(),
                request.porte()
        );

        return ResponseEntity.ok(clienteRestMapper.toResponse(cliente));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClienteResponse> alterarStatus(@PathVariable Long id, @Valid @RequestBody AlterarStatusClienteRequest request) {
        Cliente cliente = clienteService.alterarStatus(
                id,
                request.status()
        );

        return ResponseEntity.ok(clienteRestMapper.toResponse(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
    List<Cliente> clientes = clienteService.listar();

    List<ClienteResponse> response = clientes.stream().map(clienteRestMapper::toResponse).toList();
    return ResponseEntity.ok(response);
}
}