package com.mentoai.mentoaiapi.analysis.infrastructure.persistence.mapper;

import com.mentoai.mentoaiapi.analysis.domain.entity.AnaliseIA;
import com.mentoai.mentoaiapi.analysis.infrastructure.persistence.entity.AnaliseIAJpaEntity;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.entity.ReuniaoJpaEntity;
import com.mentoai.mentoaiapi.meeting.infrastructure.persistence.mapper.ReuniaoPersistenceMapper;
import org.springframework.stereotype.Component;

@Component
public class AnaliseIAPersistenceMapper {

    private final ReuniaoPersistenceMapper reuniaoMapper;

    public AnaliseIAPersistenceMapper(ReuniaoPersistenceMapper reuniaoMapper) {
        this.reuniaoMapper = reuniaoMapper;
    }

    public AnaliseIA toDomain(AnaliseIAJpaEntity entity) {
        return new AnaliseIA(entity.getId(), reuniaoMapper.toDomain(entity.getReuniao()),
                entity.getResumoExecutivo(), entity.getSentimentoGeral(), entity.getStatusProcessamento(),
                entity.getCriacao(), entity.getIniciadoEm(), entity.getFinalizadoEm(), entity.getMensagemErro());
    }

    public AnaliseIAJpaEntity toJpaEntity(AnaliseIA domain, ReuniaoJpaEntity reuniao) {
        AnaliseIAJpaEntity entity = new AnaliseIAJpaEntity();
        entity.setId(domain.getId());
        entity.setReuniao(reuniao);
        entity.setResumoExecutivo(domain.getResumoExecutivo());
        entity.setSentimentoGeral(domain.getSentimentoGeral());
        entity.setStatusProcessamento(domain.getStatusProcessamento());
        entity.setCriacao(domain.getCriacao());
        entity.setIniciadoEm(domain.getIniciadoEm());
        entity.setFinalizadoEm(domain.getFinalizadoEm());
        entity.setMensagemErro(domain.getMensagemErro());
        return entity;
    }
}
