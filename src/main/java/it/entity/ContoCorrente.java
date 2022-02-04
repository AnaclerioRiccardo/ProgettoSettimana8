package it.entity;

import java.time.LocalDate;

public class ContoCorrente {
	
	//Attributi
	private String iban;
	private double saldo;
	private String intestatario;
	private String dataCreazione = LocalDate.now().toString();	//Restituisce la data odierna; non serve passarla su postman xk la crea in automatico
	
	//Metosi
	public void versa(double importo) {
		saldo+=importo;
	}
	
	public void preleva(double importo) {
		saldo-=importo;
	}
	
	//Getter e Setter
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	public String getIntestatario() {
		return intestatario;
	}
	public void setIntestatario(String intestatario) {
		this.intestatario = intestatario;
	}
	public String getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(String dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

}
