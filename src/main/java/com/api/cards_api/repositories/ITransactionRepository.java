package com.api.cards_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.cards_api.models.TransactionsModel;

import jakarta.transaction.Transaction;

@Repository
public interface ITransactionRepository extends JpaRepository<TransactionsModel, Long> {

    /**
     * Metodo para consultar una transaccion por id de tarjeta
     * 
     * @param idCard
     * @return Optional<TransactionsModel>
     */
    @Query("SELECT t FROM TransactionsModel t WHERE t.idCard = :idCard")
    Optional<TransactionsModel> findByIdCard(@Param("idCard") Long idCard);

    /**
     * Metodo para consultar una transaccion por id
     * 
     * @param id
     * @return Optional<TransactionsModel>
     */
    @Query("SELECT t FROM TransactionsModel t WHERE t.id = :id")
    Optional<TransactionsModel> findByIdTransaction(@Param("id") Long id);

}
