package com.mentoai.mentoaiapi.meeting.application.service;

import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.entity.Transcricao;
import com.mentoai.mentoaiapi.meeting.domain.repository.ReuniaoRepository;
import com.mentoai.mentoaiapi.meeting.domain.repository.TranscricaoRepository;
import com.mentoai.mentoaiapi.shared.exception.ConflictException;
import com.mentoai.mentoaiapi.shared.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TranscricaoService {

    private final TranscricaoRepository transcricaoRepository;
    private final ReuniaoRepository reuniaoRepository;

    public TranscricaoService(
            TranscricaoRepository transcricaoRepository, ReuniaoRepository reuniaoRepository) {
        this.transcricaoRepository = transcricaoRepository;
        this.reuniaoRepository = reuniaoRepository;
    }

    @Transactional
    public Transcricao registrar(
            String conteudo, String nomeArquivo, String formatoArquivo, String idioma, Long reuniaoId) {
        if (transcricaoRepository.buscarPorReuniaoId(reuniaoId).isPresent()) {
            throw new ConflictException("A reunião já possui uma transcrição");
        }
        Reuniao reuniao = reuniaoRepository.buscarPorId(reuniaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Reunião não encontrada: " + reuniaoId));
        return transcricaoRepository.salvar(new Transcricao(
                null, conteudo, nomeArquivo, formatoArquivo, idioma, reuniao, LocalDateTime.now()));
    }

    @Transactional(readOnly = true)
    public Transcricao buscarPorId(Long id) {
        return transcricaoRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transcrição não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public Transcricao buscarPorReuniao(Long reuniaoId) {
        return transcricaoRepository.buscarPorReuniaoId(reuniaoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transcrição não encontrada para a reunião: " + reuniaoId));
    }

    @Transactional(readOnly = true)
    public List<Transcricao> listar() {
        return transcricaoRepository.listar();
    }
}
