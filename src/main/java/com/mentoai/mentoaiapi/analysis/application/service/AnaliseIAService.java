package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.analysis.application.dto.AnaliseFilaItemResponse;
import com.mentoai.mentoaiapi.analysis.application.dto.AnaliseFilaResponse;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.entity.ResumoReuniaoRecente;
import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.analysis.domain.repository.AnaliseIARepository;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.repository.ReuniaoRepository;
import com.mentoai.mentoaiapi.shared.exception.ConflictException;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnaliseIAService {

    private final AnaliseIARepository analiseRepository;
    private final ReuniaoRepository reuniaoRepository;

    public AnaliseIAService(AnaliseIARepository analiseRepository, ReuniaoRepository reuniaoRepository) {
        this.analiseRepository = analiseRepository;
        this.reuniaoRepository = reuniaoRepository;
    }

    @Transactional
    public AnaliseIA criarPendente(Long reuniaoId) {
        if (analiseRepository.buscarPorReuniaoId(reuniaoId).isPresent()) {
            throw new ConflictException("A reunião já possui uma análise");
        }
        Reuniao reuniao = reuniaoRepository.buscarPorId(reuniaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Reunião não encontrada: " + reuniaoId));
        return analiseRepository.salvar(new AnaliseIA(
                null, reuniao, null, null, StatusProcessamento.PENDENTE,
                LocalDateTime.now(), null, null, null));
    }

    @Transactional
    public AnaliseIA iniciarProcessamento(Long analiseId) {
        AnaliseIA analise = buscarPorId(analiseId);
        analise.iniciarProcessamento();
        return analiseRepository.salvar(analise);
    }

    @Transactional
    public AnaliseIA concluir(Long analiseId, String resumoExecutivo, SentimentoGeral sentimentoGeral) {
        AnaliseIA analise = buscarPorId(analiseId);
        analise.concluir(resumoExecutivo, sentimentoGeral);
        return analiseRepository.salvar(analise);
    }

    @Transactional
    public AnaliseIA registrarFalha(Long analiseId, String mensagem) {
        AnaliseIA analise = buscarPorId(analiseId);
        analise.falhar(mensagem);
        return analiseRepository.salvar(analise);
    }

    @Transactional(readOnly = true)
    public AnaliseIA buscarPorId(Long id) {
        return analiseRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Análise não encontrada: " + id));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<ResumoReuniaoRecente> buscarResumosRecentesPorCliente(Long clienteId) {
        return analiseRepository.buscarResumosRecentesPorCliente(clienteId);
    }

    @Transactional(readOnly = true)
    public AnaliseIA buscarPorReuniao(Long reuniaoId) {
        return analiseRepository.buscarPorReuniaoId(reuniaoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Análise não encontrada para a reunião: " + reuniaoId));
    }

    @Transactional(readOnly = true)
    public AnaliseFilaResponse consultarFila() {
        List<AnaliseFilaItemResponse> fila = new ArrayList<>();
        List<AnaliseFilaItemResponse> finalizados = new ArrayList<>();

        for (AnaliseIA analise : analiseRepository.listar()) {
            AnaliseFilaItemResponse item = toFilaItem(analise);
            switch (analise.getStatusProcessamento()) {
                case PENDENTE, PROCESSANDO -> fila.add(item);
                case PROCESSADA, ERRO -> finalizados.add(item);
            }
        }

        fila.sort(Comparator.comparing(AnaliseFilaItemResponse::criadoEm)
                .thenComparing(AnaliseFilaItemResponse::analiseId));
        finalizados.sort(Comparator
                .comparing(AnaliseFilaItemResponse::finalizadoEm,
                        Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(AnaliseFilaItemResponse::analiseId, Comparator.reverseOrder()));

        return new AnaliseFilaResponse(fila, finalizados);
    }

    private AnaliseFilaItemResponse toFilaItem(AnaliseIA analise) {
        Reuniao reuniao = analise.getReuniao();
        return new AnaliseFilaItemResponse(
                analise.getId(),
                reuniao.getId(),
                reuniao.getCliente().getId(),
                reuniao.getCliente().getNome(),
                analise.getStatusProcessamento(),
                analise.getCriacao(),
                analise.getIniciadoEm(),
                analise.getFinalizadoEm(),
                analise.getMensagemErro());
    }
}
