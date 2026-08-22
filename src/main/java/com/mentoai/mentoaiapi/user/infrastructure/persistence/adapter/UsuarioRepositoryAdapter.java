package com.mentoai.mentoaiapi.user.infrastructure.persistence.adapter;

import com.mentoai.mentoaiapi.user.domain.entity.Usuario;
import com.mentoai.mentoaiapi.user.domain.repository.UsuarioRepository;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import com.mentoai.mentoaiapi.user.infrastructure.persistence.repository.SpringDataUsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final SpringDataUsuarioRepository repository;
    private final UsuarioPersistenceMapper mapper;

    public UsuarioRepositoryAdapter(SpringDataUsuarioRepository repository, UsuarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return mapper.toDomain(repository.save(mapper.toJpaEntity(usuario)));
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Usuario> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }
}
