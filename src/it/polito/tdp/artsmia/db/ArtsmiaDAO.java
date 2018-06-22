package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.ArtObjectIdMap;
import it.polito.tdp.artsmia.model.ArtObjectsAndCount;

public class ArtsmiaDAO {//dao usato dal model per eseguire le operazioni volute dal controller


	public List<ArtObject> listObjects(ArtObjectIdMap map) {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(map.get(artObj));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*(3) VERSIONE 1 POCO EFFICENTE
	//fisso due oggetti che nei cicli verranno fatti variare e mi chiedo le info sui casi dove sono comparsi insieme in un esibition
	//nella select eo1.*, eo2.* per avere gli id,  
	//query che deve chiedere se gli id dei due oggetti sono comparsi nella stessa esibizione, e se si con quale peso
	public int contaExhibitionComuni(ArtObject aop, ArtObject aoa) {
		
		//join tra con se stessa per confrontare due righe diverse della tabella dove prima colonna è uguale e la seconda ha gli id che voglio io
		String sql = "SELECT count(*) AS cnt " + 
				"FROM exhibition_objects eo1, exhibition_objects eo2 " + 
				"WHERE eo1.exhibition_id=eo2.exhibition_id " + 
				"AND eo1.object_id=? " + 
				"AND eo2.object_id=?" ;
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, aop.getId());
			st.setInt(2, aoa.getId());
			
			ResultSet res = st.executeQuery();
			//no while perche ho risultato conto su prima riga
			int conteggio= res.getInt("cnt"); //della collonna che si chiama cnt
			

			conn.close();
			return conteggio;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//(4) VERSIONE 2 MOLTO EFFICENTE
	//LA FUNZIONE SOPRA FATTA HA UN ELEVATA COMPLESSITA, metodo nuovo che sfrutti la nuova query
	
		//vuole lista di oggetti con id e peso-->creo nuova classe(5) e ne restituisco qui una lista
	   //->ArtObjectsAndCount
	public List<ArtObjectsAndCount> listArtObjectsAndCount(ArtObject ao) {
	
		/*Passato un oggetto ArtObject ao, lo inserisco come paremetro e mi faccio tornare dalla query
		 * tutti i vertici a cui è collegato, e se più di una volta allo stesso avrò il contatore che li pesa
		
		String sql= "SELECT count(eo2.exhibition_id) AS cnt, eo2.object_id AS id " + 
				"FROM exhibition_objects eo1, exhibition_objects eo2 " +  //dalle tabelle con mostra/oggetto
				"WHERE eo1.exhibition_id=eo2.exhibition_id " + //join tra le due tramite mostra
				"AND eo1.object_id=? " +               //seleziono le mostre dove ho esposto oggetto passato
				"AND eo1.object_id>eo2.object_id " +  //AB e BA, prendo solo la prima tanto solo uguali
				"GROUP BY eo2.object_id";            //raggruppo per secondo oggetto 
		
		Connection conn = DBConnect.getConnection();
		List<ArtObjectsAndCount> result= new ArrayList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, ao.getId());
	
			
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add( new ArtObjectsAndCount(res.getInt("id"), res.getInt("cnt")));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public int contaExhibitionComuni(ArtObject aop, ArtObject aoa) {
		String sql = "SELECT count(*) AS cnt " + "FROM exhibition_objects eo1, exhibition_objects eo2 "
				+ "WHERE eo1.exhibition_id=eo2.exhibition_id " + "AND eo1.object_id=? " + "AND eo2.object_id=?";

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, aop.getId());
			st.setInt(2, aoa.getId());

			ResultSet res = st.executeQuery();

			res.next();
			int conteggio = res.getInt("cnt");

			conn.close();
			return conteggio;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
	*/
	
	//versione terza
	public List<Arco> getEdges(ArtObjectIdMap map) {
			
			String sql = "select ex1.object_id as id1, ex2.object_id as id2 " + 
					"from exhibition_objects as ex1, exhibition_objects as ex2 " + 
					"where ex1.exhibition_id = ex2.exhibition_id " + 
					"and ex1.object_id < ex2.object_id";
			
			List<Arco> result = new ArrayList<>();
			Connection conn = DBConnect.getConnection();
	
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet res = st.executeQuery();
				while (res.next()) {
					result.add(new Arco(map.get(res.getInt("id1")), map.get(res.getInt("id2"))));
				}
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
	public void contaExhibitionComuni(Arco arco) {
				
				String sql = "SELECT count(*) AS cnt FROM exhibition_objects eo1, exhibition_objects eo2 " + 
							"WHERE eo1.exhibition_id = eo2.exhibition_id AND eo1.object_id = ? "
							+ "AND eo2.object_id=? ";
				
				Connection conn = DBConnect.getConnection();
		
				try {
					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, arco.getO1().getId());
					st.setInt(2, arco.getO1().getId());
					ResultSet res = st.executeQuery();
					
					if (res.next()) { // è un contatore, mi da una riga sola
						arco.setPeso(res.getInt("cnt"));
					}
					conn.close();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}

	
	
	
	/*NOTA</b>: il database fornito insieme al progetto aveva dei dati errati, in
	 * particolare esistevano delle righe (circa 20) in exhibition_objects per cui
	 * il campo object_id non aveva corrispondenza nella tabella objects. Vedi:
	 * 
	 * <pre>
	 * select *
	from exhibition_objects
	where object_id not in (
		select object_id
		from objects
	)
	 * </pre>
	 * 
	 * Occorre eliminare tali situazioni, con:
	 * 
	 * <pre>
	 * delete
	from exhibition_objects
	where object_id not in (
		select object_id
		from objects
	)*/
}
