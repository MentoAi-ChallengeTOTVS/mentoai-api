package com.mentoai.mentoaiapi.meeting.application.service;

import com.mentoai.mentoaiapi.analysis.application.service.AnaliseIAService;
import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.meeting.application.dto.UploadTranscricaoResult;
import com.mentoai.mentoaiapi.meeting.domain.entity.Reuniao;
import com.mentoai.mentoaiapi.meeting.domain.entity.Transcricao;
import com.mentoai.mentoaiapi.shared.exception.BusinessException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UploadTranscricaoService {

    private static final int TAMANHO_MAXIMO_ARQUIVO = 1024 * 1024;
    private static final String FORMATO_ARQUIVO = "TXT";
    private static final String IDIOMA = "pt-BR";
    private static final char UTF_8_BOM = '\uFEFF';

    private final ReuniaoService reuniaoService;
    private final TranscricaoService transcricaoService;
    private final AnaliseIAService analiseIAService;

    public UploadTranscricaoService(
            ReuniaoService reuniaoService,
            TranscricaoService transcricaoService,
            AnaliseIAService analiseIAService) {
        this.reuniaoService = reuniaoService;
        this.transcricaoService = transcricaoService;
        this.analiseIAService = analiseIAService;
    }

    @Transactional
    public UploadTranscricaoResult executar(
            String nomeOriginalArquivo,
            byte[] arquivo,
            Long clienteId,
            Long usuarioId,
            LocalDateTime dataReuniao,
            Integer duracaoMinutos) {
        String nomeArquivo = validarNomeArquivo(nomeOriginalArquivo);
        String conteudo = validarEDecodificar(arquivo);

        Reuniao reuniao = reuniaoService.criar(dataReuniao, duracaoMinutos, clienteId, usuarioId);
        Transcricao transcricao = transcricaoService.registrar(
                conteudo, nomeArquivo, FORMATO_ARQUIVO, IDIOMA, reuniao.getId());
        AnaliseIA analise = analiseIAService.criarPendente(reuniao.getId());

        return new UploadTranscricaoResult(
                reuniao.getId(),
                transcricao.getId(),
                analise.getId(),
                analise.getStatusProcessamento());
    }

    private String validarNomeArquivo(String nomeOriginalArquivo) {
        if (nomeOriginalArquivo == null || nomeOriginalArquivo.isBlank()) {
            throw new BusinessException("O nome do arquivo é obrigatório");
        }

        String caminhoNormalizado = nomeOriginalArquivo.replace('\\', '/');
        String nomeArquivo = caminhoNormalizado.substring(caminhoNormalizado.lastIndexOf('/') + 1).trim();

        if (nomeArquivo.isBlank() || nomeArquivo.indexOf('\0') >= 0) {
            throw new BusinessException("O nome do arquivo é inválido");
        }
        if (!nomeArquivo.toLowerCase(Locale.ROOT).endsWith(".txt")) {
            throw new BusinessException("Apenas arquivos com extensão .txt são permitidos");
        }
        return nomeArquivo;
    }

    private String validarEDecodificar(byte[] arquivo) {
        if (arquivo == null || arquivo.length == 0) {
            throw new BusinessException("O arquivo não pode estar vazio");
        }
        if (arquivo.length > TAMANHO_MAXIMO_ARQUIVO) {
            throw new BusinessException("O arquivo excede o limite de 1 MiB");
        }

        String conteudo;
        try {
            conteudo = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(arquivo))
                    .toString();
        } catch (CharacterCodingException exception) {
            throw new BusinessException("O arquivo deve conter texto UTF-8 válido", exception);
        }

        if (!conteudo.isEmpty() && conteudo.charAt(0) == UTF_8_BOM) {
            conteudo = conteudo.substring(1);
        }
        if (conteudo.isBlank()) {
            throw new BusinessException("O arquivo deve possuir conteúdo textual");
        }
        return conteudo;
    }
}
