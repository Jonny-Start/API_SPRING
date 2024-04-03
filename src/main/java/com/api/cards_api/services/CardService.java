package com.api.cards_api.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    public CardsModel saveCard(CardsModel card) {
        return cardRepository.save(card);
    }

    public Optional<CardsModel> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    public CardsModel updateById(CardsModel request, Long id) {
        CardsModel card = cardRepository.findById(id).get();
        card.setBlockedUp(request.getBlockedUp());
        card.setCardActive(false);
        card.setCardHolder("Jonny Alejandro");
        cardRepository.save(card);
        return card;
    }

    public boolean deleteCard(Long id) {
        try {
            cardRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
