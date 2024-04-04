package com.api.cards_api.models;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "cards")
public class CardsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCard; // Auto incremente

    private Long idProduct; // 6 digitos

    private Long cardNumber; // 16 digitos

    private String cardHolder; // nombre y apellido del titular

    private Number cardCvv; // 3 digitos

    private Date cardExpirationDate; // MM/YY

    private Boolean cardActive; // true - activa, false - inactiva -- si es NULL (por defecto) significa que no se a creado una tarjeta para ese cliente

    private Long cardBalance; // saldo en dolares

    private Boolean blockedUp; // true - bloqueada, false - desbloqueada

    private Date dateAdd; // fecha de creacion

    private Date dateUpd; // fecha de actualizacion

    public Long getIdCard() {
        return idCard;
    }

    public void setIdCard(Long idCard) {
        this.idCard = idCard;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Number getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(Number cardCvv) {
        this.cardCvv = cardCvv;
    }

    public Date getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(Date cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    public Boolean getCardActive() {
        return cardActive;
    }

    public void setCardActive(Boolean cardActive) {
        this.cardActive = cardActive;
    }

    public Long getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(Long cardBalance) {
        this.cardBalance = cardBalance;
    }

    public Boolean getBlockedUp() {
        return blockedUp;
    }

    public void setBlockedUp(Boolean blockedUp) {
        this.blockedUp = blockedUp;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }

}
