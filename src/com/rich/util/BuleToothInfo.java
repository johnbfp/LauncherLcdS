package com.rich.util;

public class BuleToothInfo {

	private String devie_Name;
	private String devie_adress;

	public String getDevie_Name() {
		return devie_Name;
	}

	public void setDevie_Name(String devie_Name) {
		this.devie_Name = devie_Name;
	}

	public BuleToothInfo(String devie_Name, String devie_adress) {
		super();
		this.devie_Name = devie_Name;
		this.devie_adress = devie_adress;
	}

	public String getDevie_adress() {
		return devie_adress;
	}

	public void setDevie_adress(String devie_adress) {
		this.devie_adress = devie_adress;
	}
}
