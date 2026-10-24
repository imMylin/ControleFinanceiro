package com.example.controlefinanceiro.Repository;

import com.example.controlefinanceiro.Model.Salario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface SalarioRepository extends JpaRepository<Salario, Long> {

    Optional<Salario> findByDataDeReferencia(LocalDate dataDeReferencia);

    boolean existsByDataDeReferencia(LocalDate dataDeReferencia);
}
