//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import app.style.AdminViewController;
import app.style.UserViewController;
public class Main extends Application{
	
	private static Stage primaryStage;
	private static BorderPane mainLayout;
	private static Stage registerStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Assegnamento 4");
		ShowMainView();
	}
	
	public void ShowMainView() throws IOException {
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
		
		registerStage = new Stage();
		
		registerStage.setTitle("Registra un nuovo utente");
		registerStage.initModality(Modality.WINDOW_MODAL);
		registerStage.initOwner(primaryStage);
		
		Scene registerScene = new Scene(registerView);
		registerStage.setScene(registerScene);
		registerStage.showAndWait();
	}
	
	public static void ShowAdminScene(Persona persona) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("style/AdminView.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//Retrieve controller to set the logged in admin
		AdminViewController controller = loader.<AdminViewController>getController();
		controller.setUtente(persona);
	}
	
	public static void ShowUserScene(Persona persona) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("style/UserView.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//Retrieve controller to set the logged in user
		UserViewController controller = loader.<UserViewController>getController();
		controller.setUtente(persona);
	}
	
	public static void CloseRegisterStage() {
		if(registerStage != null) {
			registerStage.close();
		}
	}
	
	public static void ShowErrorScene(String message) {
		System.out.println(message);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
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