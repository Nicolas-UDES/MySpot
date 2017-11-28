package com.ift604.udes.myspot.Entites;

import java.io.Serializable;
import java.util.Date;

public class Marking implements Serializable {
	private int id;

	private Date date;

	private double amount;

	private double used;

	private boolean emptied;

	private double strength;

	private Territory location;

	private Player player;

	public Marking() {
	}

	public Marking(int id, Date date, double amount, double strength, Territory location, Player player) {
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.strength = strength;
		this.location = location;
		this.player = player;

		used = 0.0;
		emptied = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
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

	public double getUsed() {
		return used;
	}

	public void setUsed(double used) {
		this.used = used;
	}

	public boolean isEmptied() {
		return emptied;
	}

	public void setEmptied(boolean emptied) {
		this.emptied = emptied;
	}
}
