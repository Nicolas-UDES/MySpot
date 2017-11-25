package com.ift604.udes.myspot.Entites;

import java.io.Serializable;
import java.util.Date;

public class Mark implements Serializable {
	private int id;

	private Date date;

	private double amount;

	private double strength;

	public Mark() {
	}

	public Mark(int id, Date date, double amount, double strength) {
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.strength = strength;
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
}
