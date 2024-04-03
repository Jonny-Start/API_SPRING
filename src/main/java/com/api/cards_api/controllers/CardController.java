package com.api.cards_api.controllers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.cards_api.models.CardsModel;
import com.api.cards_api.services.CardService;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService cardService;

    // @GetMapping("/test");
    @GetMapping
    public ArrayList<CardsModel> getCard() {
        return this.cardService.getCard();
    }

    @GetMapping(path = "/{id}")
    public Optional<CardsModel> getCardById(@PathVariable("id") Long id) {
        return this.cardService.getCardById(id);
    }

    @PostMapping
    public CardsModel saveCard(@RequestBody CardsModel card) {
        return this.cardService.saveCard(card);
    }

    @PutMapping(path = "/{id}")
    public CardsModel updateById(@RequestBody CardsModel request, @PathVariable("id") Long id) {
        return this.cardService.updateById(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteCard(@PathVariable("id") Long id) {
        if (this.cardService.deleteCard(id)) {
            return "Card deleted";
        } else {
            return "Card not found";
        }
    }
}