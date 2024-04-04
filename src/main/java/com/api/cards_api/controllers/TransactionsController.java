package com.api.cards_api.controllers;

import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.cards_api.models.CardsModel;
import com.api.cards_api.models.TransactionsModel;
import com.api.cards_api.services.CardService;
import com.api.cards_api.services.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/transaction")
public class TransactionsController extends CardController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService; // Asegúrate de tener una instancia de CardService disponible

    /**
     * Metodo para crear una transaccion
     * 
     * @param request LinkedHashMap<String, Object>
     * @return Object
     */
    @PostMapping(path = "/purchase")
    public Object createTransaction(@RequestBody LinkedHashMap<String, Object> request) {
        try {
            // Acceder al valor de cardId desde el LinkedHashMap
            Object cardId = request.get("cardId");
            Object price = request.get("price");

            // Validaciones
            if (cardId == null) {
                return ResponseEntity.badRequest().body(
                        "Bad Request: Es requerido el campo cardId, el cual es el identificador de 16 dígitos de la tarjeta");
            }

            if (price == null) {
                return ResponseEntity.badRequest()
                        .body("Bad Request: Es requerido el campo price, el cual es el monto de la transacción");
            }

            String cardIdString = cardId.toString();
            if (cardIdString.length() != 16) {
                return ResponseEntity.badRequest().body("Bad Request: El campo cardId debe ser de 16 dígitos");
            }

            if (!(price instanceof Integer) && !(price instanceof Double)) {
                return ResponseEntity.badRequest().body("Bad Request: El campo price debe ser un número");
            }

            if (Double.parseDouble(price.toString()) <= 0) {
                return ResponseEntity.badRequest().body("Bad Request: El campo price debe ser mayor a cero");
            }

            // Validar que el cardId exista
            Optional<CardsModel> exist = this.cardService.getCardByCardNumber(Long.parseLong(cardIdString));
            if (!exist.isPresent()) {
                return ResponseEntity.badRequest().body("Bad Request: No existe una tarjeta con ese numero");
            }

            // validar si tengo saldo suficiente
            CardsModel card = exist.get();

            if (card.getCardBalance() < Double.parseDouble(price.toString())) {
                return ResponseEntity.badRequest().body("Bad Request: No tienes saldo suficiente");
            }
            // No deve estar venciada la tarjeta
            if (card.getCardExpirationDate().before(new java.util.Date())) {
                return ResponseEntity.badRequest().body("Bad Request: La tarjeta esta vencida");
            }
            // Debe estar activa la tarjeta
            if (!card.getCardActive()) {
                return ResponseEntity.badRequest().body("Bad Request: La tarjeta esta desactivada");
            }
            // No estar bloqueada
            if (card.getBlockedUp()) {
                return ResponseEntity.badRequest().body("Bad Request: La tarjeta esta bloqueada");
            }

            // Crear la transacción
            TransactionsModel transaction = new TransactionsModel();
            transaction.setIdCard(card.getIdCard());
            transaction.setTransactionValue(Long.parseLong(price.toString()));
            transaction.setStep(2);
            transaction.setDateAdd(new java.sql.Date(new java.util.Date().getTime()));
            transaction.setDateUpd(new java.sql.Date(new java.util.Date().getTime()));

            // Actualizar el saldo de la tarjeta
            card.setCardBalance(card.getCardBalance() - Long.parseLong(price.toString()));
            this.cardService.saveCard(card);

            return ResponseEntity.ok(this.transactionService.saveTransaction(transaction));
        } catch (Error e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error interno");
        }
    }

    /**
     * Metodo para obtener una transaccion por id
     * 
     * @param idTransaction Long
     * @return Object
     */
    @GetMapping(path = "/{idTransaction}")
    public Object getTransaction(@PathVariable("idTransaction") Long idTransaction) {
        try {
            Optional<TransactionsModel> exist = this.transactionService.getTransactionById(idTransaction);
            if (!exist.isPresent()) {
                return ResponseEntity.badRequest().body("Bad Request: No existe una transacción con ese id");
            }
            return ResponseEntity.ok(exist);
        } catch (Error e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error interno");
        }
    }

    @PostMapping(path = "/anulation")
    public Object anulationTransaction(@RequestBody LinkedHashMap<String, Object> request) {
        try {
            // Acceder al valor de cardId desde el LinkedHashMap
            Object cardId = request.get("cardId");
            Object transactionId = request.get("transactionId");

            // Validaciones
            if (cardId == null) {
                return ResponseEntity.badRequest().body(
                        "Bad Request: Es requerido el campo cardId, el cual es el identificador de 16 dígitos de la tarjeta");
            }

            if (transactionId == null) {
                return ResponseEntity.badRequest()
                        .body("Bad Request: Es requerido el campo transactionId, el cual es el monto de la transacción");
            }

            String cardIdString = cardId.toString();
            if (cardIdString.length() != 16) {
                return ResponseEntity.badRequest().body("Bad Request: El campo cardId debe ser de 16 dígitos");
            }

            // Validar que el cardId exista
            Optional<CardsModel> exist = this.cardService.getCardByCardNumber(Long.parseLong(cardIdString));
            if (!exist.isPresent()) {
                return ResponseEntity.badRequest().body("Bad Request: No existe una tarjeta con ese numero");
            }

            Optional<TransactionsModel> existT = this.transactionService
                    .getTransactionById(Long.parseLong(transactionId.toString()));
            if (!existT.isPresent()) {
                return ResponseEntity.badRequest().body("Bad Request: No existe una transacción con ese id");
            }

            // La transaccion no debe ser mayor a 24 horas
            TransactionsModel transaction = existT.get();
            long diff = new java.util.Date().getTime() - transaction.getDateAdd().getTime();
            long diffHours = diff / (60 * 60 * 1000) % 24;
            if (diffHours > 24) {
                return ResponseEntity.badRequest()
                        .body(" Bad Request: La transacción no puede ser anulada ha pasado mas de 24 horas");
            }

            // La transaccion debe estar aprobada
            if ((int) transaction.getStep() != 2) {
                return ResponseEntity.badRequest()
                        .body(" Bad Request: La transacción no puede ser anulada no esta en estado aprobada");
            }

            // Actualizar el saldo de la tarjeta
            CardsModel card = exist.get();
            card.setCardBalance(card.getCardBalance() + transaction.getTransactionValue());
            this.cardService.saveCard(card);

            // Actualizar la transaccion
            transaction.setStep(4);
            transaction.setDateUpd(new java.sql.Date(new java.util.Date().getTime()));

            return ResponseEntity.ok(this.transactionService.saveTransaction(transaction));
        } catch (Error e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error interno");
        }
    }

}
