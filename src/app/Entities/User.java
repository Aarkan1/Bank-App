package app.Entities;


import app.annotations.Column;

public class User {
    @Column("person_nr")
    private String personNr;
    @Column
    private String name;
    @Column("age")
    private int age;

    public String getId(){
        return personNr;
    }

    public String getName() { return name; }

    @Override
    public String toString(){
        return String.format("User: { personNr: %s, name: %s, age: %d }", personNr, name, age);
    }
}
