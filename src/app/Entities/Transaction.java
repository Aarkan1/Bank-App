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
    private float amount;
    @Column
    private java.sql.Timestamp date;
    @Column("account_from")
    private java.math.BigInteger accountFrom;

    public boolean isIncome(long amount){
        return amount == Long.parseLong(accountFrom.toString()) ? false : true;
    }

    public String getMessage() {
        return message;
    }

    public float getAmount() {
        return amount;
    }

    public ZonedDateTime getDate() {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("Europe/Berlin"));
    }

    public String getDateAsString() {
        return getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ');
    }
}
