package app.Entities;


import app.annotations.Column;

import java.time.LocalDate;

public class Account {
    @Column("account_nr")
    private long accountNr;
    @Column
    private String type;
    @Column
    private float saldo;

    public String getType() {
        return type;
    }

    public float getSaldo() {
        return saldo;
    }

    @Override
    public String toString() {
        return String.format("Account: { Nr: %d, Type: %s, Saldo: %f }", accountNr, type, saldo);
    }
}
