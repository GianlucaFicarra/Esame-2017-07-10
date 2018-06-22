package it.polito.tdp.artsmia.model;

public class Arco {
	
	private ArtObject o1;
	private ArtObject o2;
	private int peso;
	
	public Arco(ArtObject o1, ArtObject o2) {
		this.o1 = o1;
		this.o2 = o2;
	}
	public ArtObject getO1() {
		return o1;
	}
	public void setO1(ArtObject o1) {
		this.o1 = o1;
	}
	public ArtObject getO2() {
		return o2;
	}
	public void setO2(ArtObject o2) {
		this.o2 = o2;
	}
	public void setPeso(int int1) {
		this.peso=peso;
	}
	public int getPeso() {
		return this.peso;
	}
	
	

}
