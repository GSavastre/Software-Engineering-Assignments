//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class Main extends Application{
	
	private static Stage primaryStage;
	private BorderPane mainLayout;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		/*Button btn = new Button();
		
		btn.setText("Say hello world!");
		btn.setOnAction(e -> System.out.print("Hello, world!"));
		
		StackPane root = new StackPane();
		root.getChildren().add(btn);
		
		Scene scene = new Scene(root, 300, 300);
		
		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();*/
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Assegnamento 4 gestione circolo");
		ShowMainView();
	}
	
	private void ShowMainView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("style/LoginView.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void ShowRegisterScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("style/RegisterView.fxml"));
		BorderPane registerView = loader.load();
		
		Stage registerStage = new Stage();
		registerStage.setTitle("Registra un nuovo utente");
		registerStage.initModality(Modality.WINDOW_MODAL);
		registerStage.initOwner(primaryStage);
		
		Scene registerScene = new Scene(registerView);
		registerStage.setScene(registerScene);
		registerStage.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
	/*public static void main(String[] args) {
		
		Random random = new Random();
		Admin nuovoAdmin = new Admin();
		
		//Inserisco i dati dell'admin iniziale
		System.out.println("Creato utente admin inziale, inserisci i dati...");
		nuovoAdmin.SetAttributes();
		
		//Inserimento numero utenti desiderato
		Scanner input = new Scanner(System.in);
		System.out.print("Quanti utenti vuoi inserire? ");
		int numUtenti = input.nextInt();
		
		while(numUtenti <= 0) {
			System.out.print("Quanti utenti vuoi inserire? ");
			numUtenti = input.nextInt();
		}
		
		Persona[] utenti = new Persona[numUtenti];
		
		//Inserimento numero attivita desiderato
		System.out.print("Quante attivita vuoi inserire? ");
		int numAttivita = input.nextInt();
		
		while(numAttivita <= 0) {
			System.out.print("Quante attivita vuoi inserire? ");
			numAttivita = input.nextInt();
		}
		
		Evento[] attivita = new Evento[numAttivita];
		
		//Inserimento dei dati dei singoli utenti che possono essere in modo casuale admin o soci
		for(int i = 0; i < numUtenti; i++) {
			if(random.nextInt(2) == 0) {
				System.out.println("Aggiungi un Admin...");
				Admin rndAdmin = new Admin();
				rndAdmin.SetAttributes();
				utenti[i] = rndAdmin;
			}else {
				System.out.println("Aggiungi un Socio...");
				Socio rndSocio = new Socio();
				rndSocio.SetAttributes();
				utenti[i] = rndSocio;
			}
		}
		input.nextLine();
		String nomeAttivita = "attivita1";
		
		
		//Inserimento di attivita che possono essere in modo casuale corsi o gare
		for(int i = 0; i < numAttivita; i++) {
			if(random.nextInt(2) == 0) {
				System.out.println("Aggiungi un Corso...");
				System.out.print("Nome del corso: "); nomeAttivita = input.nextLine();
				attivita[i] = new Corso(nomeAttivita);
			}else {
				System.out.println("Aggiungi una Gara..."); 
				System.out.print("Nome della gara: "); nomeAttivita = input.nextLine();
				attivita[i] = new Gara(nomeAttivita);
			}
		}
		
		//------------------Creo un nuovo circolo-------------------------------
		System.out.println("Creo nuovo circolo con "+ utenti.length+" utenti e "+attivita.length+" attivita.");
		System.out.print("Inserisci il nome del nuovo circolo: ");String nomeCircolo = input.nextLine();
		Circolo circolo = new Circolo(nomeCircolo, utenti, attivita);
		
		//------------------Aggiungo un nuovo socio al circolo------------------
		System.out.println("Crea un nuovo socio da aggiungere al circolo");
		Socio nuovoSocio = new Socio();
		nuovoSocio.SetAttributes();
		System.out.println("\nAdmin "+nuovoAdmin.nome+" aggiunge "+nuovoSocio.nome+" alla lista di soci del circolo...");
		Persona[] listaSoci = nuovoAdmin.AggiungiUtente(circolo, nuovoSocio);
		
		StampaUtenti(circolo);
		
		//------------------Modifico i dati di un socio -----------------------
		Persona utente = listaSoci[random.nextInt(listaSoci.length)];
		System.out.println("\n"+nuovoAdmin.nome+" modifica i dati di "+ utente.nome+" "+ utente.cognome);
		nuovoAdmin.ModificaUtente(utente);
		
		StampaUtenti(circolo);
		
		//------------------Elimino un socio dal circolo-----------------------
		utente = listaSoci[random.nextInt(listaSoci.length)];
		System.out.println("\n"+nuovoAdmin.nome+" elimina "+utente.nome+" da "+circolo.nome);
		listaSoci = nuovoAdmin.RimuoviUtente(circolo, utente);
		
		StampaUtenti(circolo);
		
		utente = listaSoci[random.nextInt(listaSoci.length)];
		Evento evento = circolo.eventi[random.nextInt(circolo.eventi.length)];
		
		//-------------------Iscrizione di un socio ad un evento---------------------
		System.out.println("\n"+utente.nome+" si iscrive all'evento "+ evento.nome);
		evento.AggiungiPersona(utente);
		
		StampaUtenti(evento);
		
		//--------------------Eliminazione iscrizione ad un evento---------------------
		System.out.println("\n"+utente.nome+" annulla l'iscrizione all'evento "+ evento.nome);
		evento.RimuoviPersona(utente);
		
		StampaUtenti(evento);
		
		//Aggiungo degli utenti alla prima attività giusto per popolare l'array di iscritti
		System.out.println("Aggiungo tutti gli utenti al primo evento (giusto per avere degli iscritti)");
		for(Persona pers : listaSoci) {
			circolo.eventi[0].AggiungiPersona(pers);
		}
		
		System.out.println("\n\nInformazioni finali:\nUtenti iscritti al circolo");
		
		StampaUtenti(circolo);
		
		for(Evento event : circolo.eventi) {
			StampaUtenti(event);
		}
		
		input.close();
	}
	*/
	
	
	
	//Stampa i partecipanti ad un circolo
	public static void StampaUtenti(Circolo circolo) {
		System.out.println("All'interno del circolo "+circolo.nome+ " sono presenti i seguenti :");
		for(Persona socio : circolo.partecipanti) {
			socio.GetAttributes();
		}
	}
	
	//Stampa gli iscritti ad un evento
	public static void StampaUtenti(Evento evento) {
		
		if(evento.iscritti == null) {
			System.out.println("All'evento "+evento.nome+" non ci sono iscritti");
		}else {
			if(evento instanceof Corso) {
				System.out.println("Al corso "+evento.nome+" ci sono "+evento.iscritti.length+" iscritti:");
			}else {
				System.out.println("Alla gara "+evento.nome+" ci sono "+evento.iscritti.length+" iscritti:");
			}
			
			for(Persona iscritto : evento.iscritti) {
				iscritto.GetAttributes();
			}
		}
	}
}