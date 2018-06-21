/**
 * Sample Skeleton for 'Artsmia.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxLUN"
	private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaComponenteConnessa"
	private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

	@FXML // fx:id="btnCercaOggetti"
	private Button btnCercaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaOggetti"
	private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="txtObjectId"
	private TextField txtObjectId; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	@FXML
	void doAnalizzaOggetti(ActionEvent event) {//questa costruisce il grafo
		
		/*(1)grafo con oggetti e la loro copresenza nelle varie mostre
		 * arco collega due oggetti se sono stati esposti contemporaneamente nella stessa exhibition ed il peso
         * dell’arco rappresenta il numero di exhibition in cui tali oggetti sono stati contemporaneamente esposti
         * Lo memorizzo dentro il model, tra le sue funzioni deve avere 
		 * le strutture dati che servono all algoritmo, tra cui il grafo
		 * --->creo classe model*/
		
		model.creaGrafo();
		txtResult.setText(String.format("Grafo creato: %d vertici, %s archi\n", model.getGraphNumVertices(), model.getGraphNumEdges()));

	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {

		/*c-d componente connesse non è altro che fare una visita partendo
		 * da un vertice e dimmi quanti vertici trovi */
		
		String idObjStr = txtObjectId.getText();
		//prendo numero scritto nelliDoggetto, il cuo valore è un oggetto sottoforma di stringa
		
		//--->  e lo converto in intero verificando che sia tale
		int idObj;
		try {
			idObj = Integer.parseInt(idObjStr);
		} catch (NumberFormatException e) {
			txtResult.appendText("Inserire un numero intero valido\n");
			return;
		}

		//se è intero devo verificare che sia valido come id di oggetto
		if (!model.isObjIdValid(idObj)) {
			txtResult.appendText(String.format("Non esiste alcun oggetto con id=%d\n", idObj));
			return ;
		}
		
		//se tutto va bene calcolo dimensione componente connessa partendo da questo id
		int dimCC = model.calcolaDimensioneCC(idObj) ;
		
		if(dimCC>1) {
		List<Integer> valoriTendina = null;
		for(int i=2; i<=dimCC; i++)
			valoriTendina.add(i);
		
			boxLUN.getItems().addAll(valoriTendina);
		}
	
		
		txtResult.appendText(String.format("La componente connessa che contiene il vertice %d ha %d vertici\n", idObj, dimCC));

	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		txtResult.setText("doCercaOggetti");
		
		Integer lun = boxLUN.getValue();
		if(lun==null)
			txtResult.appendText("Inserire lunghezza cammino\n");
			
		String idObjStr = txtObjectId.getText();
	
		int idObj;
		try {
			idObj = Integer.parseInt(idObjStr);
		} catch (NumberFormatException e) {
			txtResult.appendText("Inserire un numero intero valido\n");
			return;
		}
		
		List<ArtObject> lista= model.camminoMassimo(idObj, lun) ;
		Collections.sort(lista);
		
		for(ArtObject art: lista) {
			txtResult.appendText(art.toString());
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}
}
