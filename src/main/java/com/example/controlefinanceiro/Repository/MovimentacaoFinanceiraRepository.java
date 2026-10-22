package com.example.controlefinanceiro.Repository;

import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Model.MovimentacaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceira, Long> {

    @Query("""
       SELECT COALESCE(SUM(m.valor), 0)
       FROM MovimentacaoFinanceira m
       WHERE m.tipo = :tipo
       """)
    BigDecimal somarPorTipo(
            @Param("tipo") TipoMovimentacao tipo);


    @Query("""
           SELECT m.categoria, COALESCE(SUM(m.valor), 0)
           FROM MovimentacaoFinanceira m
           WHERE m.tipo = :tipo
           GROUP BY m.categoria
           """)
    List<Object[]> somarPorCategoria(@Param("tipo") TipoMovimentacao tipo);

    @Query("""
           SELECT COALESCE(SUM(m.valor),0)
           FROM MovimentacaoFinanceira m
           WHERE m.tipo = :tipo AND m.data BETWEEN :data_inicial AND :data_final         
           """)
    BigDecimal somarPorPeriodo(@Param("tipo") TipoMovimentacao tipo, @Param("data_inicial") LocalDate data_inicial, @Param("data_final") LocalDate data_final);
}
