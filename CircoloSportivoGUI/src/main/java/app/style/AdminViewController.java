package app.style;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import app.*;
import app.database.DB;
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
	
	private ObservableList<Evento> eventi;
	private ObservableList<Persona> utenti;

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
	
	//TODO: Muovi i metodi load in DB
	private void LoadData() {
		eventi = FXCollections.observableArrayList();
		utenti = FXCollections.observableArrayList();
		
		try (Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS, DB.USER, DB.PASSWORD);
				Statement stmt = conn.createStatement();){
			String eventsQueryString = "SELECT * FROM eventi;";
			String usersQueryString ="SELECT * FROM utenti;";
			
			//Fetch events
			ResultSet rset = stmt.executeQuery(eventsQueryString);
			while(rset.next()) {
				String rsNome = rset.getString("nome");
				String rsTipo = rset.getString("tipo");
				
				if(rsTipo.contentEquals("corso")) {
					eventi.add(new Corso(rsNome));
				}else if(rsTipo.contentEquals("gara")) {
					eventi.add(new Gara(rsNome));
				}
			}
			
			//Fetch users
			rset = stmt.executeQuery(usersQueryString);
			while(rset.next()) {
				String rsName = rset.getString("nome");
				String rsLastName = rset.getString("cognome");
				String rsEmail = rset.getString("email");
				String rsPassword = rset.getString("password");
				String rsRole = rset.getString("ruolo");
				
				if(rsRole.contentEquals("socio")) {
					utenti.add(new Socio(rsName, rsLastName, rsEmail, rsPassword));
				}else if(rsRole.contentEquals("admin")) {
					utenti.add(new Admin(rsName, rsLastName, rsEmail, rsPassword));
				}
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		//Update TableViews
		tbEvents.setItems(eventi);
		tbUsers.setItems(utenti);
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
