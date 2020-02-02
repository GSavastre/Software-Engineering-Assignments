package app.style;

import java.io.IOException;

import app.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class AdminViewController {
	private Admin utente;
	
	//It would be better to create a separate table with roles and get the various roles from there instead of having them hardcoded
	ObservableList<String> eventTypes = FXCollections.observableArrayList("gara", "corso");
	ObservableList<String> roles = FXCollections.observableArrayList("socio","admin");

	public void setUtente(Persona persona) {
		this.utente = (Admin) persona;
	}
	
	
	@FXML
	private TableView<Evento> tbEvents;
	
	@FXML
	private TableColumn<Evento, String> colEventName;
	
	@FXML
	private TextField txtEventName;
	
	@FXML
	private ChoiceBox<String> cbEventType;
	
	@FXML
	private TableView<Persona> tbUsers;
	
	@FXML
	private TableColumn<Persona, String> colUserEmails;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private ChoiceBox<String> cbRole;
	
	@FXML
	private TextField txtLastName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private VBox vbEventsControls;
	
	@FXML
	private VBox vbUsersControls;
	
	@FXML
	private Label lblEventIstruction, lblUserIstruction;
	
	@FXML
	private void initialize() throws IOException{
		
		//Disable users and events controls until user or event is selected from the respective TableView
		vbUsersControls.setDisable(true);
		vbEventsControls.setDisable(true);
		
		cbEventType.setValue(eventTypes.get(0));
		cbEventType.setItems(eventTypes);
		
		cbRole.setValue(roles.get(0));
		cbRole.setItems(roles);
		
		try {
			colEventName.setCellValueFactory(new PropertyValueFactory<Evento, String>("nome"));
			colUserEmails.setCellValueFactory(new PropertyValueFactory<Persona, String>("mail"));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		LoadData();
	}
	
	private void LoadData() {
		
	}
	
	@FXML
	private void ModifyEvent() {
		
	}
	
	@FXML
	private void ModifyUser() {
		
	}
	
	@FXML
	private void CancelEventModification() {
		txtEventName.clear();
		cbEventType.setValue(eventTypes.get(0));
	}
	
	@FXML
	private void CancelUserModification() {
		txtName.clear();
		txtLastName.clear();
		txtEmail.clear();
		cbRole.setValue(roles.get(0));
	}
}
