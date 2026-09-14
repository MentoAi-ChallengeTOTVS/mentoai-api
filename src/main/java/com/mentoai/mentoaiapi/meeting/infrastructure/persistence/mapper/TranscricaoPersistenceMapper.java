package com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.meeting.domain.entity.Transcricao;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ReuniaoJpaEntity;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.TranscricaoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TranscricaoPersistenceMapper {

    private final ReuniaoPersistenceMapper reuniaoMapper;

    public TranscricaoPersistenceMapper(ReuniaoPersistenceMapper reuniaoMapper) {
        this.reuniaoMapper = reuniaoMapper;
    }

    public Transcricao toDomain(TranscricaoJpaEntity entity) {
        return new Transcricao(entity.getId(), entity.getConteudo(), entity.getNomeArquivo(),
                entity.getFormatoArquivo(), entity.getIdioma(), reuniaoMapper.toDomain(entity.getReuniao()),
                entity.getCriacao());
    }

    public TranscricaoJpaEntity toJpaEntity(Transcricao domain, ReuniaoJpaEntity reuniao) {
        TranscricaoJpaEntity entity = new TranscricaoJpaEntity();
        entity.setId(domain.getId());
        entity.setConteudo(domain.getConteudo());
        entity.setNomeArquivo(domain.getNomeArquivo());
        entity.setFormatoArquivo(domain.getFormatoArquivo());
        entity.setIdioma(domain.getIdioma());
        entity.setReuniao(reuniao);
        entity.setCriacao(domain.getCriacao());
        return entity;
    }
}
