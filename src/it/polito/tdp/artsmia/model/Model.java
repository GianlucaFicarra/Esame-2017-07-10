package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
/*(2) classe model creata per gestire elenco oggetti
 * e grafo i cui vertici siano gli oggetti, completo tutta la classe*/
	
	private List<ArtObject> oggetti;
	private ArtObjectIdMap map;
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> graph; //grafo non orientato ma pesato
	private ArtsmiaDAO dao;
	
    //lista con la soluzione
	private List<ArtObject> best = null;
	
	public Model() {
		dao = new ArtsmiaDAO();
		map = new ArtObjectIdMap();
		oggetti = dao.listObjects(map);
		
		//stampa default
		System.out.format("Oggetti caricati: %d oggetti\n", this.oggetti.size());
	
	}
	
	
	
	//popola lista artobject(leggendo da DB) e crea grafo
	public void creaGrafo() {
		
		//--crea grafo
		
		/*devo capire il tipo di grafo che mi interessa PESATO SEMPLICE NON ORIENTATO
		 scelgo SimpleWeightedGraph<>(DefaultWeightedEdge.class), con dentro 0 vertici e archi
		 faccio ogni volta new per chiamare più volte il metodo, butto via e ricomincio da capo */
		this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		//--aggiungi vertici(tutti gli oggetti della lista artobject)
		Graphs.addAllVertices(this.graph, this.oggetti);
		
		//--aggiungi archi(tra chi e chi) e con il loro peso
		
		/*VERSIONE 1 POCO EFFICENTE
		 * per definire archi grafo considero tutti gli archi potenziali,
		 * e mi chiedo se nella mia istanza di grafo esista o meno,
		 * considerando ogni arco tracoppia di vertici e vedere se esiste
		
		for(ArtObject aop: this.artObjects) { //per ogni vertice di aprtenza
			for(ArtObject aoa: this.artObjects) { //per ogni vertice di arrivo
				
				/*essendo grafo non orientato non mi importa di aggiungere rami 15 e 51,
				 * cosi per evitare di prenderli entrambi metto controllo aop.getId()<aoa.getId()
				 * che ne prende solo uno dei due, se fosse stato orientato non l avrei messo 
				 * per prendere tutte le coppie perche se orientato 15!=51 
				if(!aop.equals(aoa) && aop.getId()<aoa.getId()) { //se sono dievrsi, cioe arrivo partenza uguale
					
					/*metodo dao che mi dice se sono connessi e con che peso
					 * essendo pesato devo farmi restituire dalla query il peso e caricarlo,
					 * se non fosse stato pesato bastava sapere se ci fosse ramo ed aggiungerli
					int peso = exhibitionComuni(aop, aoa); 
					System.out.format("(%d, %d) peso %d\n", aop.getId(), aoa.getId());//stampa di verifica
					
					if(peso!=0) {
						//se sono connessi aggiunge l'arco al grafo e gli setto il peso
						Graphs.addEdgeWithVertices(this.graph, aop, aoa, peso);
						
						
					}
					
				}
			}
			
		}
		
		 // VERSIONE 2 MOLTO EFFICENTE
		for(ArtObject ao: this.oggetti) {
			//dati gli oggetti, tramite dao faccio query che mi da elenco di risultati
			List<ArtObjectsAndCount> connessi = dao.listArtObjectsAndCount(ao);
				
			//per ogni risultato creo un arco tramite secondo ciclo
			for(ArtObjectsAndCount c: connessi) {
				//oggetto debole, dove inizializzo solo pezzo che usa
				ArtObject destinazione= new ArtObject(c.getArtObjectId(), null, null, null, 0, null, null, null, null, null, 0, null, null, null, null, null);
				Graphs.addEdge(this.graph, ao, destinazione , c.getCount());
				//--> per ogni elemento lista aggiungo arco, quello di partenza è il primo, l'arrivo è elemento lista con count collegato
				System.out.format("(%d, %d) peso %d\n", ao.getId(), destinazione.getId(), c.getCount());//stampa di verifica
			}
		}
		*/
		
		
		
		// arco = collega due oggetti esposti contemporaneamente alla stessa exhibition
		// peso = numero di exhibition in cui vengono esposti insieme
		List<Arco> archi = dao.getEdges(map);
		if(archi != null) {
			for(Arco a : archi) {
				dao.contaExhibitionComuni(a); //conta le volte che sono stati esposti insieme i due vertici e ne setta il peso dell'arco
				Graphs.addEdgeWithVertices(this.graph, a.getO1(), a.getO2(), a.getPeso());
			}
			
			System.out.println("Vertici: "+this.graph.vertexSet().size());
			System.out.println("Archi: "+this.graph.edgeSet().size());
		} else {
			System.out.println("ops");
		}
		
		
		
		// Aggiungi gli archi (con il loro peso)
			System.out.format("Grafo creato: %d vertici, %d archi\n", graph.vertexSet().size(), graph.edgeSet().size());

		
	}


	/*METODO CHIAMATO DA VERSIONE 1 POCO EFFICENTE:
	 * metodo chiamato sopra che dovra chiedere al dao quante exhibition comuni hanno gli oggetti considerati
	 
	private int exhibitionComuni(ArtObject aop, ArtObject aoa) {
		ArtsmiaDAO dao= new ArtsmiaDAO();
		//uso metodo dao dove contare il numero di esibizione, passo tutti oggetti e non solo singolo valore
		int comuni = dao.contaExhibitionComuni( aop,  aoa);
		return comuni;
	}	
	  -------------------------------
	  
	 * VERSIONE 4 - la pi� efficiente di tutte ** esegue una query unica
	 * (complessa, ma una sola) con la quale ottiene in un sol colpo tutti gli archi
	 * del grafo
	 * 
	
	private void addEdgesV3() {
		// TODO: basata sulla query:
		// SELECT eo1.object_id, count(eo2.exhibition_id), eo2.object_id
		// FROM exhibition_objects eo1, exhibition_objects eo2
		// WHERE eo1.exhibition_id=eo2.exhibition_id
		// AND eo2.object_id>eo1.object_id
		// GROUP BY eo1.object_id, eo2.object_id

	}
    */
	
	//per il debug
	public int getGraphNumEdges() {
		return this.graph.edgeSet().size();
	}

	public int getGraphNumVertices() {
		return this.graph.vertexSet().size();
	}

	//vado a cercare nella lista se almeno un elemento appartiene, per vedere se oggetto id è valido
	public boolean isObjIdValid(int idObj) {
		
		//se funzione è chiamata prima di creagrafo non fa nulla
		//perchè lista ancora vuota
		if (this.oggetti == null)
			return false;

		//scandisco lista per verificare se ci sia id passato
		for (ArtObject ao : this.oggetti) {
			if (ao.getId() == idObj)
				return true;
		}
		return false;
	}

	//dato oggetto id calcola la dimensione componente conessa
	public int calcolaDimensioneCC(int idObj) {

		// trova il vertice di partenza di cui ho l'id passato
		ArtObject start = map.get(idObj); //in alternativa trovaVertice(idObj);

		// visita il grafo, creo iteratore che restituisce i vertiti che visita
		//e li raccolgo in una collection (scelgo set)
		Set<ArtObject> visitati = new HashSet<>();
		
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> dfv = new DepthFirstIterator<>(this.graph, start);
		//iteratore che uso per iterare grafo partendo da start chiamando next
		
		while (dfv.hasNext()) //finche cè un elemento successivo per l'iteratore
			visitati.add(dfv.next()); //aggiungo il prox elemento, se diventa falso esco

		// conta gli elementi
		return visitati.size();
	}

	
	
	public List<ArtObject> getArtObjects() {
		return oggetti;
	}

	// SOLUZIONE PUNTO 2
	
	//funzione chiamata dal test model che invoca la ricorsiva
	public List<ArtObject> camminoMassimo(int startId, int LUN) { //passo start e valore max
		
		// trova il vertice di partenza, sfrutto la mappa creata prima
		ArtObject start = map.get(startId);
		if (start == null) //nel caso non lo trovi ho eccezzione
			throw new IllegalArgumentException("Vertice " + startId + " non esistente");

		//iniziaizzo le variabili della ricorsiva: lista col primo vertice
		List<ArtObject> parziale = new ArrayList<>();
		parziale.add(start);

		//inizializzo anche la soluz, eventalemnte mi da peso 0 perche non ho archi
		this.best = new ArrayList<>();
		best.add(start);
                
		        //liv 1 perchè ho gia un elemento
		cerca(parziale, 1, LUN);

		return best;

	}

	/*
	alternativa alla mappa per trovare il vertice di partenza
	private ArtObject trovaVertice(int idObj) {
		// trova il vertice di partenza
		//vad a cercare l artobject che appartiene all'elenco e che ha quell id
		ArtObject start = null;
		for (ArtObject ao : this.oggetti) {
			if (ao.getId() == idObj) {
				start = ao;
				break;
			}
		}
		if (start == null) //nel caso non lo trovi ho eccezzione
			throw new IllegalArgumentException("Vertice " + idObj + " non esistente");
		
		return start;
	}*/
	
	private void cerca(List<ArtObject> parziale, int livello, int LUN) {
		
		if (livello == LUN) {
			// caso terminale
			if (peso(parziale) > peso(best)) { //calcolo peso in funz esterna
				best = new ArrayList<>(parziale); //deepcopy, voglio la più lunga
				System.out.println(parziale);
			}
			return;
		}

		//caso normale: trova vertici adiacenti all'ultimo
		ArtObject ultimo = parziale.get(parziale.size() - 1); //-1 perche pate da 0

		//uso classe Graphs per trovare lista di vertici adiacenti dato quello di passato
		List<ArtObject> adiacenti = Graphs.neighborListOf(this.graph, ultimo);

		//devo filtrare gli adiacenti e prendere solo quelli utili
		for (ArtObject prova : adiacenti) {
			//stessa classification e non già presente in parziale
			if (!parziale.contains(prova) && prova.getClassification() != null
					&& prova.getClassification().equals(parziale.get(0).getClassification())) {
				
				//se verificate avvio la ricorsione
				parziale.add(prova);
				cerca(parziale, livello + 1, LUN);
				parziale.remove(parziale.size() - 1); //backtrack
			}
		}

	}

	//calcola peso cioè la somma dei pesi in quel grafo
	private int peso(List<ArtObject> parziale) {
		int peso = 0;
		for (int i = 0; i < parziale.size() - 1; i++) {
			//prendo tutti gli archi date le coppie di vertici
			DefaultWeightedEdge e = graph.getEdge(parziale.get(i), parziale.get(i + 1));
			
			//salvo il peso arco e lo sommo al peso tot della mia soluzione
			int pesoarco = (int) graph.getEdgeWeight(e);
			peso += pesoarco;
		}
		return peso;
	}

	

}

