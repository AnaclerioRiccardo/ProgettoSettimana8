package it.entity;

import java.time.LocalDate;

public class Movimento {
	
	//Attributi
	private int id;		///non va passato su postman perche' lo inserisco automaticamente
	private double importo;
	private TipoMovimento tipo;
	private String data = LocalDate.now().toString();	//Restituisce la data odierna; non serve passarla su postman xk la crea in automatico
	private String ibanConto;
	
	//Getter e setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getImporto() {
		return importo;
	}
	public void setImporto(double importo) {
		this.importo = importo;
	}
	public TipoMovimento getTipo() {
		return tipo;
	}
	public void setTipo(TipoMovimento tipo) {
		this.tipo = tipo;
	}
	public String getIbanConto() {
		return ibanConto;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setIbanConto(String ibanConto) {
		this.ibanConto = ibanConto;
	}

}
