package com.mentoai.mentoaiapi.user.presentation.rest.controller;

import com.mentoai.mentoaiapi.user.application.service.UsuarioService;
import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.presentation.rest.mapper.UsuarioRestMapper;
import com.mentoai.mentoaiapi.user.presentation.rest.request.AlterarStatusUsuarioRequest;
import com.mentoai.mentoaiapi.user.presentation.rest.request.AtualizarUsuarioRequest;
import com.mentoai.mentoaiapi.user.presentation.rest.request.CriarUsuarioRequest;
import com.mentoai.mentoaiapi.user.presentation.rest.response.UsuarioResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;


@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRestMapper mapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioRestMapper mapper) {
        this.usuarioService = usuarioService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody CriarUsuarioRequest request) {
        Usuario usuarioSalvo = usuarioService.criar(
                request.nome(),
                request.email(),
                request.senha(),
                request.perfil()
        );

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioSalvo.getId())
                .toUri();

        return ResponseEntity.created(uri).body(mapper.toResponse(usuarioSalvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listar(
            @PageableDefault(page = 0, size = 10, sort = "nome") Pageable pageable) {
        Page<UsuarioResponse> responsePage = usuarioService.listar(pageable)
                .map(mapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarUsuarioRequest request) {
        Usuario usuarioAtualizado = usuarioService.atualizar(
                id,
                request.nome(),
                request.email(),
                request.senha(),
                request.perfil()
        );
        return ResponseEntity.ok(mapper.toResponse(usuarioAtualizado));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UsuarioResponse> alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AlterarStatusUsuarioRequest request) {
        Usuario usuarioAtualizado = usuarioService.alterarStatus(id, request.ativo());
        return ResponseEntity.ok(mapper.toResponse(usuarioAtualizado));
    }
}