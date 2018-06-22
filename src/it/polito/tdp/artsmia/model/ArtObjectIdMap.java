package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;


public class ArtObjectIdMap  {

	
	private Map<Integer, ArtObject> map;
	
	public ArtObjectIdMap() {
		map = new HashMap<>();
	}

	public ArtObject get(int id) {
		return map.get(id);
	}
	
	public ArtObject get(ArtObject obj) {
		ArtObject old = map.get(obj.getId());
		if (old == null) { 
			map.put(obj.getId(), obj);
			return obj;
		}
		return old; 
	}
	
	public void put(ArtObject obj, int id) {
		map.put(id, obj);
	}

}
