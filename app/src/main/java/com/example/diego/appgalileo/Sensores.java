package com.example.diego.appgalileo;

public class Sensores {

	private Boolean aLedGalileo;
	private Boolean aLedMin;
	private Boolean aLedMax;
	private Boolean aMotorMin;
	private Boolean aMotorMax;
	private String aPantalla;
	private int sPotenciometro;
	private Boolean sPresencia;
	private int sTemperatura;
	private String temperaturaIdeal;

	public String getTemperaturaIdeal() {
		return temperaturaIdeal;
	}

	public void setTemperaturaIdeal(String temperaturaIdeal) {
		this.temperaturaIdeal = temperaturaIdeal;
	}

	public String getTemperaturaReal() {
		return temperaturaReal;
	}

	public void setTemperaturaReal(String temperaturaReal) {
		this.temperaturaReal = temperaturaReal;
	}

	private String temperaturaReal;

	public Sensores() {
		super();
	}

	public Boolean getaLedGalileo() {
		return aLedGalileo;
	}

	public void setaLedGalileo(Boolean aLedGalileo) {
		this.aLedGalileo = aLedGalileo;
	}

	public Boolean getaLedMin() {
		return aLedMin;
	}

	public void setaLedMin(Boolean aLedMin) {
		this.aLedMin = aLedMin;
	}

	public Boolean getaLedMax() {
		return aLedMax;
	}

	public void setaLedMax(Boolean aLedMax) {
		this.aLedMax = aLedMax;
	}

	public Boolean getaMotorMin() {
		return aMotorMin;
	}

	public void setaMotorMin(Boolean aMotorMin) {
		this.aMotorMin = aMotorMin;
	}

	public Boolean getaMotorMax() {
		return aMotorMax;
	}

	public void setaMotorMax(Boolean aMotorMax) {
		this.aMotorMax = aMotorMax;
	}

	public String getaPantalla() {
		return aPantalla;
	}

	public void setaPantalla(String aPantalla) {
		this.aPantalla = aPantalla;
	}

	public int getsPotenciometro() {
		return sPotenciometro;
	}

	public void setsPotenciometro(int sPotenciometro) {
		this.sPotenciometro = sPotenciometro;
	}

	public Boolean getsPresencia() {
		return sPresencia;
	}

	public void setsPresencia(Boolean sPresencia) {
		this.sPresencia = sPresencia;
	}

	public int getsTemperatura() {
		return sTemperatura;
	}

	public void setsTemperatura(int sTemperatura) {
		this.sTemperatura = sTemperatura;
	}
}