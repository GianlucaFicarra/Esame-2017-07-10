package it.polito.tdp.artsmia.model;

public class ArtObjectsAndCount { //(5) javabeen per contenere i risultati della query

	private int artObjectId;
	private int count;
	
	public ArtObjectsAndCount(int artObjectId, int count) {
		super();
		this.artObjectId = artObjectId;
		this.count = count;
	}
	public int getArtObjectId() {
		return artObjectId;
	}
	public void setArtObjectId(int artObjectId) {
		this.artObjectId = artObjectId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
