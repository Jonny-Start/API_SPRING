package com.api.cards_api.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.cards_api.models.CardsModel;
import com.api.cards_api.models.TransactionsModel;
import com.api.cards_api.repositories.ITransactionRepository;

@Service
public class TransactionService {

    @Autowired
    ITransactionRepository transactionRepository;

    public ArrayList<TransactionsModel> getCard() {
        return (ArrayList<TransactionsModel>) transactionRepository.findAll();
    }

    /**
     * Metodo para guardar una transaccion o actualizar
     * 
     * @param transaction
     * @return TransactionsModel
     */
    public TransactionsModel saveTransaction(TransactionsModel transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Metodo para obtener una transaccion por id
     * 
     * @param id
     * @return TransactionsModel
     */

    public Optional<TransactionsModel> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
}
