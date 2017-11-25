package com.ift604.udes.myspot.Entites;

import java.util.Date;

/**
 * Created by Squirrel on 2017-11-25.
 */

public class Drinking {

    private int id;

    private double amount;

    private Date date;

    private Territory location;

    private Player player;

    public Drinking() {
    }

    public Drinking(int id, double amount, Date date, Territory location, Player player) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.location = location;
        this.player = player;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Territory getLocation() {
        return location;
    }

    public void setLocation(Territory location) {
        this.location = location;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}