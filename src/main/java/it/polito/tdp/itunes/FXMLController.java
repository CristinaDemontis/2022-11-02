/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.AnalisiGrafo;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlaylist"
    private Button btnPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtDTOT"
    private TextField txtDTOT; // Value injected by FXMLLoader

    @FXML // fx:id="txtMax"
    private TextField txtMax; // Value injected by FXMLLoader

    @FXML // fx:id="txtMin"
    private TextField txtMin; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPlaylist(ActionEvent event) {

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	String inputMin = txtMin.getText(); 
    	String inputMax = txtMax.getText(); 
    	Genre genere = cmbGenere.getValue();
    	double min = 0; 
    	double max = 0; 
    	
    	try {
    		min = Double.parseDouble(inputMin); 
    		max = Double.parseDouble(inputMax); 
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore valido per il min e max ");
    		return; 
    	}
    	
    	if(genere == null) {
    		txtResult.setText("Scegliere un genere dalla tendina! ");
    		return; 
    	}else {
    		this.model.creaGrafo(min, max, genere);
    		txtResult.setText("Grafo creato. \n");
    		txtResult.appendText("- Vertici: "+ this.model.getnVertici()+"\n");
    		txtResult.appendText("- Archi: "+ this.model.getnArchi()+"\n");
    		txtResult.appendText("\n");

    		txtResult.appendText("ANALISI GRAFO: \n");
    		List<AnalisiGrafo> risultato =  this.model.analizzaGrafo(min, max, genere);
    		for(AnalisiGrafo a : risultato) {
    			txtResult.appendText(a.toString()+"\n");
    		}
    		
    		
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPlaylist != null : "fx:id=\"btnPlaylist\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDTOT != null : "fx:id=\"txtDTOT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMax != null : "fx:id=\"txtMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMin != null : "fx:id=\"txtMin\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Genre> result = this.model.getGenre(); 
    	for(Genre g: result) {
    		cmbGenere.getItems().add(g); 
    	}
    	
    }

}
