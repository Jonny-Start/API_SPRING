package com.api.cards_api.controllers;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public CardService getCardService() {
        return cardService;
    }

    /**
     * Metodo para crear una tarjeta
     * 
     * @param request
     * @param card
     * @return Object
     */
    @PostMapping
    public Object createCard(@RequestBody CardsModel request, CardsModel card) {
        try {
            // Validaciones
            if (request.getIdProduct() == null) {
                return ("Bad Request: "
                        + "Es requerido el campo idProduct, el cual es el identificador de 6 dígitos cliente");
            }
            // IdProduct tine que ser de 6 digitos
            if (request.getIdProduct().toString().length() != 6) {
                return ("Bad Request: " + "El campo idProduct debe ser de 6 dígitos");
            }
            // Validar que el idProduct exista
            Optional<CardsModel> exist = this.cardService.getCardByIdProduct(request.getIdProduct());
            if (exist.isPresent()) {
                return ResponseEntity.badRequest().body("Bad Request: Ya existe una una cuenta para este cliente");
            }

            // Numero de cliente
            card.setIdProduct(request.getIdProduct());

            return this.cardService.saveCard(card);
        } catch (Error e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error interno");
        }
    }

    /**
     * Metodo para generar un número de tarjeta (Asignar una tarjeta a un cliente)
     * 
     * @param productId
     * @return Optional<CardsModel>
     */
    @GetMapping(path = "/{productId}/number")
    public Object createCard(@RequestBody CardsModel request, @PathVariable("productId") Long productId) {

        // Validar que el idProduct exista
        Optional<CardsModel> exist = this.cardService.getCardByIdProduct(productId);
        if (!exist.isPresent()) {
            return ResponseEntity.badRequest().body("Bad Request: No existe una cuenta para este cliente");
        }
        // Validar que el idProduct no tenga una tarjeta activa
        if (exist.get().getCardNumber() != null) {
            return ResponseEntity.badRequest().body("Bad Request: Ya existe una tarjeta para este cliente");
        }
        // Validar que enviaron el nombre del titular
        if (request.getCardHolder() == null) {
            return ResponseEntity.badRequest().body("Bad Request: Es requerido el campo cardHolder");
        }

        // Generar Numeros aleatorios de 10 digitos para el número de tarjeta
        Long cardNumberGenerado = (long) (Math.random() * 9000000000L) + 1000000000L;
        // Generar Numeros aleatorios de 3 digitos para el cvv
        Integer cardCvv = (int) (Math.random() * 1000);
        // Generar Fecha de expiración de la tarjeta (Fecha actual + 3 años) en formato
        // yyyy-MM-dd
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = currentDate.plusYears(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedExpirationDate = expirationDate.format(formatter);

        String primeros6Digitos = productId.toString().substring(0, 6);
        String siguientes10Digitos = cardNumberGenerado.toString().substring(0, 10);
        String cardNumber = primeros6Digitos + siguientes10Digitos;

        // CardsModel card = new CardsModel();
        CardsModel card = this.cardService.getCardById(exist.get().getIdCard()).get();
        // Asignar valores a la tarjeta
        card.setCardNumber(Long.parseLong(cardNumber));
        card.setCardHolder(request.getCardHolder());
        card.setCardCvv(cardCvv);
        card.setCardExpirationDate(Date.valueOf(formattedExpirationDate));
        card.setCardActive(false);
        card.setCardBalance(0L);
        card.setBlockedUp(false);
        card.setDateAdd(Date.valueOf(LocalDate.now()));
        card.setDateUpd(Date.valueOf(LocalDate.now()));

        return ResponseEntity.ok(this.cardService.saveCard(card));
    }

    /**
     * Metodo para Activar una tarjeta
     * 
     * @param cardId
     * @return Object
     */
    @PostMapping(path = "/enroll")
    public ResponseEntity<Object> activeCard(@RequestBody LinkedHashMap<String, Object> request) {
        // Acceder al valor de cardId desde el LinkedHashMap
        Object cardId = request.get("cardId");

        // Validaciones
        if (cardId == null) {
            return ResponseEntity.badRequest().body(
                    "Bad Request: Es requerido el campo cardId, el cual es el identificador de 16 dígitos de la tarjeta");
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

        // Validar que la tarjeta no este activa
        if (exist.get().getCardActive()) {
            return ResponseEntity.badRequest().body("Bad Request: La tarjeta ya esta activa");
        }

        // Activar tarjeta
        CardsModel card = this.cardService.getCardById(exist.get().getIdCard()).get();

        // Update card active status
        card.setCardActive(true);
        card.setDateUpd(Date.valueOf(LocalDate.now()));

        return ResponseEntity.ok(this.cardService.saveCard(card));
    }

    /**
     * Metodo para Bloquear una tarjeta
     * 
     * @param cardId
     * @return Object
     */
    @DeleteMapping(path = "/{cardId}")
    public Object blockCard(@PathVariable("cardId") String cardId) {

        // Validaciones
        if (cardId == null) {
            return ResponseEntity.badRequest().body(
                    "Bad Request: Es requerido el campo cardId, el cual es el identificador de 16 dígitos de la tarjeta");
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

        // Validar que la tarjeta no este bloqueada
        if (exist.get().getBlockedUp()) {
            return ResponseEntity.badRequest().body("Bad Request: La tarjeta ya esta bloqueada");
        }

        // Bloquear tarjeta
        CardsModel card = this.cardService.getCardById(exist.get().getIdCard()).get();

        // Update card active status
        card.setBlockedUp(true);
        card.setDateUpd(Date.valueOf(LocalDate.now()));

        return ResponseEntity.ok(this.cardService.saveCard(card));
    }

    /**
     * Metodo para recargar saldo a una tarjeta
     * 
     * @param request
     * @return Object
     */
    @PostMapping(path = "/balance")
    public ResponseEntity<Object> rechargeBalance(@RequestBody LinkedHashMap<String, Object> request) {
        // Acceder al valor de cardId desde el LinkedHashMap
        Object cardId = request.get("cardId");
        Object balance = request.get("balance");

        // Validaciones
        if (cardId == null) {
            return ResponseEntity.badRequest().body(
                    "Bad Request: Es requerido el campo cardId, el cual es el identificador de 16 dígitos de la tarjeta");
        }

        if (balance == null) {
            return ResponseEntity.badRequest().body(
                    "Bad Request: Es requerido el campo balance, cual es la cantidad de dinero a recargar");
        }

        String cardIdString = cardId.toString();
        if (cardIdString.length() != 16) {
            return ResponseEntity.badRequest().body("Bad Request: El campo cardId debe ser de 16 dígitos");
        }

        Double balanceDouble = Double.parseDouble(balance.toString());
        if (balanceDouble <= 0) {
            return ResponseEntity.badRequest().body("Bad Request: El campo balance debe ser mayor a 0");
        }

        // Validar que el cardId exista
        Optional<CardsModel> exist = this.cardService.getCardByCardNumber(Long.parseLong(cardIdString));
        if (!exist.isPresent()) {
            return ResponseEntity.badRequest().body("Bad Request: No existe una tarjeta con ese numero");
        }

        // Activar tarjeta
        CardsModel card = this.cardService.getCardById(exist.get().getIdCard()).get();

        // Update card balance
        card.setCardBalance(card.getCardBalance() + balanceDouble.longValue());
        card.setDateUpd(Date.valueOf(LocalDate.now()));

        return ResponseEntity.ok(this.cardService.saveCard(card));
    }

    /**
     * Metodo para consultar el saldo de una tarjeta
     * 
     * @param cardId
     * @return Object
     */
    @GetMapping(path = "/balance/{cardId}")
    public Object getBalance(@PathVariable("cardId") String cardId) {
        // Validaciones
        if (cardId == null) {
            return ResponseEntity.badRequest().body(
                    "Bad Request: Es requerido el campo cardId, el cual es el identificador de 16 dígitos de la tarjeta");
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
        // String formattedBalance = String.format("%,d", exist.get().getCardBalance());
        return ResponseEntity.ok(String.format("%,d", exist.get().getCardBalance()));
    }

}