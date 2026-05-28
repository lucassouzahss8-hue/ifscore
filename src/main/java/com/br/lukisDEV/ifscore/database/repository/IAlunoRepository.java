package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IAlunoRepository extends JpaRepository<AlunoEntity, UUID> {
    Optional<AlunoEntity> findByEmail(String email);

    boolean existsByEmail(@NotBlank @Email String email);
}
