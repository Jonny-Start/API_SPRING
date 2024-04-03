package com.api.cards_api.models;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class TransactionsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction; // identificador de la transaccion

    private Long idCard; // identificador de la tarjeta

    private Long transactionValue; // valor de la transaccion

    private Number step; // 1 - en proceso, 2 - aprobado, 3 - rechazado, 4 - anulado

    private Date dateAdd; // fecha de creacion

    private Date dateUpd; // fecha de actualizacion

    public Long getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(Long idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Long getIdCard() {
        return idCard;
    }

    public void setIdCard(Long idCard) {
        this.idCard = idCard;
    }

    public Long getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(Long transactionValue) {
        this.transactionValue = transactionValue;
    }

    public Number getStep() {
        return step;
    }

    public void setStep(Number step) {
        this.step = step;
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
