package app.Entities;


import app.annotations.Column;

import java.time.LocalDate;

public class Account {
    @Column("account_nr")
    private String accountNr;
    @Column
    private String name;
    @Column
    private String type;
    @Column
    private double saldo;
    private boolean addedAccount = false;

    private int offset = 0;

    public Account(String seperator) {
        this.name = seperator;
    }

    public Account() {
    }

    public void setAddedAccount() {
        addedAccount = true;
    }

    public int getOffset() {
        return offset;
    }

    public void incrementOffset() {
        offset += 10;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getAccountNr() {
        return accountNr;
    }

    @Override
    public String toString() {
        return addedAccount ? name : String.format("%s\t\t%-3.2f", name, saldo);
    }
}
