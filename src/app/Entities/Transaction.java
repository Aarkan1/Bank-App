package app.Entities;


import app.annotations.Column;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    @Column
    private long id;
    @Column
    private String message;
    @Column
    private double amount;
    @Column
    private java.sql.Timestamp date;
    @Column("account_from")
    private String accountFrom;

    public boolean isIncome(String account){
        return accountFrom.equals(account) ? false : true;
    }

    public String getMessage() {
        return message;
    }

    public double getAmount() {
        return amount;
    }

    public ZonedDateTime getDate() {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("Europe/Berlin"));
    }

    public String getDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YY-MM-dd");
        return getDate().format(formatter);
    }
}
