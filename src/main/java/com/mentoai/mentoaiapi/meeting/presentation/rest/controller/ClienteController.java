package com.mentoai.mentoaiapi.meeting.presentation.rest.controller;

import com.mentoai.mentoaiapi.meeting.application.service.ClienteService;
import com.mentoai.mentoaiapi.meeting.application.service.ReuniaoService;
import com.mentoai.mentoaiapi.meeting.domain.entity.Cliente;
import com.mentoai.mentoaiapi.meeting.domain.repository.ClienteFiltro;
import com.mentoai.mentoaiapi.meeting.domain.repository.Pagina;
import com.mentoai.mentoaiapi.meeting.presentation.rest.mapper.ClienteRestMapper;
import com.mentoai.mentoaiapi.meeting.presentation.rest.mapper.ReuniaoRestMapper;
import com.mentoai.mentoaiapi.meeting.presentation.rest.request.AlterarStatusClienteRequest;
import com.mentoai.mentoaiapi.meeting.presentation.rest.request.AtualizarClienteRequest;
import com.mentoai.mentoaiapi.meeting.presentation.rest.request.CriarClienteRequest;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.ClientePageResponse;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.ClienteResponse;
import com.mentoai.mentoaiapi.meeting.presentation.rest.response.ReuniaoResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ReuniaoService reuniaoService;
    private final ReuniaoRestMapper reuniaoMapper;
    private final ClienteRestMapper clienteRestMapper;

    public ClienteController(
            ClienteService clienteService,
            ReuniaoService reuniaoService,
            ReuniaoRestMapper reuniaoMapper,
            ClienteRestMapper clienteRestMapper) {
        this.clienteService = clienteService;
        this.reuniaoService = reuniaoService;
        this.reuniaoMapper = reuniaoMapper;
        this.clienteRestMapper = clienteRestMapper;
    }

    @GetMapping("/{id}/reunioes")
    public ResponseEntity<List<ReuniaoResponse>> listarReunioes(@PathVariable Long id) {
        return ResponseEntity.ok(
                reuniaoService.listarPorCliente(id).stream().map(reuniaoMapper::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody CriarClienteRequest request) {
        Cliente cliente = clienteService.criar(request.nome(), request.segmento(), request.porte());

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRestMapper.toResponse(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable @Positive Long id) {
        Cliente cliente = clienteService.buscarPorId(id);

        return ResponseEntity.ok(clienteRestMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable @Positive Long id,
            @Valid @RequestBody AtualizarClienteRequest request) {
        Cliente cliente = clienteService.atualizar(id, request.nome(), request.segmento(), request.porte());

        return ResponseEntity.ok(clienteRestMapper.toResponse(cliente));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClienteResponse> alterarStatus(@PathVariable @Positive Long id,
            @Valid @RequestBody AlterarStatusClienteRequest request) {
        Cliente cliente = clienteService.alterarStatus(id, request.status());

        return ResponseEntity.ok(clienteRestMapper.toResponse(cliente));
    }

    @GetMapping
    public ResponseEntity<ClientePageResponse> listar(@RequestParam(required = false) @Size(max = 255) String nome,

                                                      @RequestParam(required = false) @Size(max = 100) String segmento,

                                                      @RequestParam(required = false) @Size(max = 50) String porte,

                                                      @RequestParam(required = false) Boolean status,

                                                      @RequestParam(defaultValue = "0") @Min(0) int page,

                                                      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,

                                                      @RequestParam(defaultValue = "nome") String sort,

                                                      @RequestParam(defaultValue = "asc") String direction) {

        ClienteFiltro filtro = new ClienteFiltro(nome, segmento, porte, status);

        Pagina<Cliente> resultado = clienteService.listar(filtro, page, size, sort, direction);

        List<ClienteResponse> clientes = resultado.conteudo().stream().map(clienteRestMapper::toResponse).toList();

        ClientePageResponse response = new ClientePageResponse(clientes, resultado.pagina(), resultado.tamanho(),
                resultado.totalElementos(), resultado.totalPaginas());

        return ResponseEntity.ok(response);
    }
}
