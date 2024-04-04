package com.api.cards_api.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.api.cards_api.models.CardsModel;
import com.api.cards_api.repositories.ICardRepository;

@Service
public class CardService {

    @Autowired
    ICardRepository cardRepository;

    public ArrayList<CardsModel> getCard() {
        return (ArrayList<CardsModel>) cardRepository.findAll();
    }

    /**
     * Metodo para guardar una tarjeta o actualizar
     * 
     * @param card
     * @return CardsModel
     */
    public CardsModel saveCard(CardsModel card) {
        return cardRepository.save(card);
    }

    /**
     * Metodo para consultar una tarjeta por id de producto
     * 
     * @param idProduct
     * @return Optional<CardsModel>
     */
    public Optional<CardsModel> getCardByIdProduct(Long idProduct) {
        return cardRepository.findByIdProduct(idProduct);
    }

    /**
     * Metodo para consultar una tarjeta por cardNumber
     * 
     * @param cardNumber
     * @return Optional<CardsModel>
     */
    public Optional<CardsModel> getCardByCardNumber(Long cardNumber) {
        return cardRepository.findByIdCard(cardNumber);
    }

    /**
     * Metodo para consultar una tarjeta por idCard
     * 
     * @param id
     * @return Optional<CardsModel>
     */
    public Optional<CardsModel> getCardById(Long id) {
        return cardRepository.findById(id);
    }

}
