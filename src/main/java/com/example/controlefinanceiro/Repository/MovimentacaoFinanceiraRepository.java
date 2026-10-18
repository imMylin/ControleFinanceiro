package com.example.controlefinanceiro.Repository;

import com.example.controlefinanceiro.Model.MovimentacaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceira, Long> {
}
