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
		
		tbvEvents.setItems(eventi);
		tbvEvents.getSelectionModel().setCellSelectionEnabled(true);
		ObservableList selectedCell = tbvEvents.getSelectionModel().getSelectedCells();
		
		selectedCell.addListener(new ListChangeListener() {
			public void onChanged(Change c) {
				TablePosition tablePosition = (TablePosition) selectedCell.get(0);
				Object val = tablePosition.getTableColumn().getCellData(tablePosition.getRow());
				
				Evento search = new Evento(val.toString());
				
				Evento eventoSelezionato = eventi.get(eventi.indexOf(search));
				
				System.out.println("Evento selezionato "+ eventoSelezionato.nome+" "+eventoSelezionato.getClass().toString());
				
				gpLabelsGrid.setVisible(true);
				hbButtons.setVisible(true);
				
				lblEventName.setText(eventoSelezionato.nome);
				lblEventType.setText(eventoSelezionato.getClass().getSimpleName());
				lblEventUsersNumber.setText(Integer.toString(eventoSelezionato.getNumIscritti()));
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
	}
	
	
}
