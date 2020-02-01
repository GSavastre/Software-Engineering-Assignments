package app.style;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import app.Admin;
import app.database.DB;
import app.*;

public class UserViewController {
	
	@FXML
	private TableView<Evento> tbvEvents;
	
	@FXML
	private TableColumn<Evento, String> col_eventName;
	
	@FXML
	private Label lblEventName, lblEventType, lblEventUsersNumber;
	
	@FXML
	private GridPane gpLabelsGrid;
	
	@FXML
	private HBox hbButtons;
	
	@FXML
	private Button btnSubscribe;
	
	@FXML
	private Button btnCancelSub;
	
	private Persona utente;
	
	private Evento eventoSelezionato;
	
	private ObservableList<Evento> eventi;
	
	public void setUtente(Persona persona) {
		this.utente = persona;
	}
	
	
	@SuppressWarnings("unchecked")
	@FXML
	private void initialize() throws IOException{
		
		//Nascondi bottoni e gridpane contenente informazioni se non vengono selezionati eventi
		gpLabelsGrid.setVisible(false);
		hbButtons.setVisible(false);
		
		try {
			col_eventName.setCellValueFactory(new PropertyValueFactory<Evento, String>("nome"));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		LoadEvents();
		
		tbvEvents.getSelectionModel().setCellSelectionEnabled(true);
		ObservableList selectedCell = tbvEvents.getSelectionModel().getSelectedCells();
		
		selectedCell.addListener(new ListChangeListener() {
			public void onChanged(Change c) {
				TablePosition tablePosition = (TablePosition) selectedCell.get(0);
				Object val = tablePosition.getTableColumn().getCellData(tablePosition.getRow());
				
				Evento search = new Evento(val.toString());
				
				eventoSelezionato = eventi.get(eventi.indexOf(search));
				
				//System.out.println("Evento selezionato "+ eventoSelezionato.nome+" "+eventoSelezionato.getClass().toString());
				
				gpLabelsGrid.setVisible(true);
				hbButtons.setVisible(true);
				
				lblEventName.setText(eventoSelezionato.nome);
				lblEventType.setText(eventoSelezionato.getClass().getSimpleName());
				lblEventUsersNumber.setText(Integer.toString(eventoSelezionato.getNumIscritti()));
				
				//System.out.println("Ricerca iscritto :"+Integer.toString(eventoSelezionato.PresenzaIscritto(utente)));
				
				//Disable subscription cancellation if user is not already subscribed to the selected event
				if(eventoSelezionato.PresenzaIscritto(utente) != -1) {
					btnSubscribe.setDisable(true);
					btnCancelSub.setDisable(false);
				}else {
					btnSubscribe.setDisable(false);
					btnCancelSub.setDisable(true);
				}
			}
		});
		
	}
	
	private void LoadEvents() {
		eventi = FXCollections.observableArrayList();
		
		//Event fetching
		try(Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS, DB.USER, DB.PASSWORD);
				Statement stmt = conn.createStatement();){
			String strSelect = "SELECT * FROM eventi;";
			
			ResultSet rset = stmt.executeQuery(strSelect);
			
			while(rset.next()) {
				String rsNome = rset.getString("nome");
				String rsTipo = rset.getString("tipo");
				
				if(rsTipo.contentEquals("corso")) {
					eventi.add(new Corso(rsNome));
				}else if(rsTipo.contentEquals("gara")){
					eventi.add(new Gara(rsNome));
				}
				
			}
			
			//Subscriber selection query
			strSelect = "SELECT u.nome, u.cognome, u.email, u.password, u.ruolo " 
						+ "FROM utenti AS u, eventi AS e, iscrizione_eventi AS iscrizione "
						+ "WHERE u.id = iscrizione.utente AND iscrizione.evento = e.id AND e.nome = ";
			//Subscribers fetching
			for(Evento e : eventi) {
				//Get all subscribers of event with e.nome name
				String eventNameParam = "'"+e.nome+"';";
				rset = stmt.executeQuery(strSelect + eventNameParam);
				System.out.println(strSelect + eventNameParam);
				
				//Add all subsribers to event
				while(rset.next()) {
					String subName = rset.getString("nome");
					String subLastName = rset.getString("cognome");
					String subEmail = rset.getString("email");
					String subPassword = rset.getString("password");
					String subRole = rset.getString("ruolo");
					
					if(subRole.contentEquals("socio")) {
						e.AggiungiPersona(new Socio(subName, subLastName, subEmail, subPassword));
					}else if(subRole.contentEquals("admin")) {
						e.AggiungiPersona(new Admin(subName, subLastName, subEmail, subPassword));
					}
					
				}
			}
			//End of subscribers fetching
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		//End of event fetching
		tbvEvents.setItems(eventi);
	}
	
	@FXML
	private void Subscribe() {
		try(Connection conn = DriverManager.getConnection(DB.URL+DB.ARGS, DB.USER,DB.PASSWORD);
				Statement stmt = conn.createStatement();){
			String queryString = "SELECT id FROM eventi WHERE nome='"+eventoSelezionato.nome+"';";
			//System.out.println(queryString);
			int eventId = -1;
			int userId = -1;
			
			ResultSet rset = stmt.executeQuery(queryString);
			while(rset.next()) {
				eventId = rset.getInt("id");
			}
			
			queryString = "SELECT id FROM utenti WHERE email='"+utente.mail+"';";
			//System.out.println(queryString);
			rset = stmt.executeQuery(queryString);
			while(rset.next()) {
				userId = rset.getInt("id");
			}
			
			//System.out.println("EventID: "+eventId+" UserId: "+userId);
			
			if(eventId != -1 && userId != -1) {
				String values = String.join(",",Integer.toString(userId), Integer.toString(eventId));
				queryString = "INSERT INTO iscrizione_eventi (utente,evento) VALUES("+values+")";
				//System.out.println(queryString);
				
				int countUpdated = stmt.executeUpdate(queryString);
				
				//Update the number of participants to the event
				if(countUpdated != 0) {
					eventoSelezionato.AggiungiPersona(utente);
					UpdateLabels();
				}
			}
		}catch(SQLException e){
			e.getMessage();
		}
	}
	
	@FXML
	private void CancelSub() {
		try(Connection conn = DriverManager.getConnection(DB.URL+DB.ARGS, DB.USER,DB.PASSWORD);
				Statement stmt = conn.createStatement();) {
			String queryString = "SELECT id FROM eventi WHERE nome='"+eventoSelezionato.nome+"';";
			int eventId = -1;
			int userId = -1;
			
			ResultSet rset = stmt.executeQuery(queryString);
			while(rset.next()) {
				eventId = rset.getInt("id");
			}
			
			queryString = "SELECT id FROM utenti WHERE email='"+utente.mail+"';";
			//System.out.println(queryString);
			rset = stmt.executeQuery(queryString);
			while(rset.next()) {
				userId = rset.getInt("id");
			}
			
			if(eventId != -1 && userId != -1) {
				queryString = "DELETE FROM iscrizione_eventi WHERE utente='"+userId+"' AND evento='"+eventId+"'";
				int countUpdated = stmt.executeUpdate(queryString);
				
				if(countUpdated != 0) {
					eventoSelezionato.RimuoviPersona(utente);
					UpdateLabels();
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void UpdateLabels() {
		lblEventUsersNumber.setText(Integer.toString(eventoSelezionato.getNumIscritti()));
		
		if(eventoSelezionato.PresenzaIscritto(utente) != -1) {
			btnSubscribe.setDisable(true);
			btnCancelSub.setDisable(false);
		}else {
			btnSubscribe.setDisable(false);
			btnCancelSub.setDisable(true);
		}
	}
}
