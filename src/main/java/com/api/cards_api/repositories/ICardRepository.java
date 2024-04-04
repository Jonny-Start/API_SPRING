package com.api.cards_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.cards_api.models.CardsModel;

@Repository
public interface ICardRepository extends JpaRepository<CardsModel, Long> {

    /**
     * Metodo para consultar una tarjeta por id de producto
     * 
     * @param idProduct
     * @return Optional<CardsModel>
     */
    @Query("SELECT c FROM CardsModel c WHERE c.idProduct = :idProduct")
    Optional<CardsModel> findByIdProduct(@Param("idProduct") Long idProduct);

    /**
     * Metodo para consultar una tarjeta por id
     * 
     * @param cardNumber
     * @return Optional<CardsModel>
     */
    @Query("SELECT c FROM CardsModel c WHERE c.cardNumber = :cardNumber")
    Optional<CardsModel> findByIdCard(@Param("cardNumber") Long cardNumber);
}
