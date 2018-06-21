package it.polito.tdp.artsmia.model;

import java.util.List;

public class TestModel {

	//creo test per valutare cosa sto facendo
	public static void main(String[] args) {
		Model m= new Model();
		
		m.creaGrafo();
		
		
		/*VERSIONE 1 POCO EFFICENTE
		 * QUERY DI contaExhibitionComuni:
		 * 
		 * fisso due oggetti che nei cicli verranno fatti variare e mi chiedo le info sui casi dove sono comparsi insieme in un esibition
	       nella select eo1.*, eo2.* per avere gli id,  
	      query che deve chiedere se gli id dei due oggetti sono comparsi nella stessa esibizione, e se si con quale peso
		 * FUNZIONA SE GRAFO PICCOLI PERCHE HA COMPLESSITA QUADRATICA
		 * 
		 *  "SELECT count(*) AS cnt " + 
				"FROM exhibition_objects eo1, exhibition_objects eo2 " + 
				"WHERE eo1.exhibition_id=eo2.exhibition_id " + 
				"AND eo1.object_id=? " + 
				"AND eo2.object_id=?" ;*/
		 
		  
		  
		/* *DEVO ALLEGGERIRLA E MIGLIORARLA, FACCIO QUERY MIGLIORE
		 *1) mantego questa schema non facendo query al DB per salvarle,
		 *le salvo nelle strutture dati di java tipo mappe e ci faccio relazioni li
		
		 *2) oppure non devo fare questa operazione, avendo densita bassa la
		 *maggior parte del tempo ha densita 0, così lo miglioro query:
		 * chiedo al Db di ottenere gli archi che escono dal vertice passato
		 * e mi faccio tornare elenco degli archi con il loro peso adiacenti al vertice dato
		 * riduco la complessita facendo lavorare di più il DB
		*/
		
		
		 /* VERSIONE 2 MOLTO EFFICENTE
		 *SELECT count(eo2.exhibition_id), eo2.object_id
			FROM exhibition_objects eo1, exhibition_objects eo2 
			WHERE eo1.exhibition_id=eo2.exhibition_id 
			AND eo1.object_id=821 
			AND eo1.object_id>eo2.object_id  (per non prendere ramo due volte)
			GROUP BY eo2.object_id
			
			dao che riceve come parametro un objects e non due, e sputa in uscita
			una lista di object con i pesi, poi la elaboro per aggiungere gli archi
		 */

		
		
		
		/*---------------------------------------
		List<ArtObject> list = m.getArtObjects() ;
		for( ArtObject ao : list) {
			int dimCC = m.calcolaDimensioneCC(ao.getId()) ;
			System.out.format("Vertice %d, dimensione %d\n", ao.getId(), dimCC);
		}*/
		
		
		
		
       int dimCC = m.calcolaDimensioneCC(2) ;
       System.out.format("La componente connessa che contiene il vertice 2 ha %d vertici\n", dimCC);
		
		//List<ArtObject> best1 = m.camminoMassimo(5342,1) ;
	}

}
