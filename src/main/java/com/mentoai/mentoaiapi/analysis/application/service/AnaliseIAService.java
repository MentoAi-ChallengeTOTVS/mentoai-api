package com.mentoai.mentoaiapi.analysis.application.service;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.domain.enums.SentimentoGeral;
import com.mentoai.mentoaiapi.analysis.domain.enums.StatusProcessamento;
import com.mentoai.mentoaiapi.analysis.domain.repository.AnaliseIARepository;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.repository.ReuniaoRepository;
import com.mentoai.mentoaiapi.shared.exception.ConflictException;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
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

    @Transactional(readOnly = true)
    public AnaliseIA buscarPorReuniao(Long reuniaoId) {
        return analiseRepository.buscarPorReuniaoId(reuniaoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Análise não encontrada para a reunião: " + reuniaoId));
    }
}
