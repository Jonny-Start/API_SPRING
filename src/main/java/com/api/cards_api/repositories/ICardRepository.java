package com.api.cards_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.cards_api.models.CardsModel;

@Repository
public interface ICardRepository extends JpaRepository<CardsModel, Long> {

    // arrayList<CardsModel>

}
