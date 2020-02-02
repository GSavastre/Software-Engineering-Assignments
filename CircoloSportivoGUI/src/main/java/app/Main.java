//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

import java.io.IOException;

import app.style.AdminViewController;
import app.style.UserViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application
{

  private static Stage primaryStage;
  private static BorderPane mainLayout;
  private static Stage registerStage;

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Assegnamento 4");
    ShowMainView();
  }

  /*
   * Display the main scene with the login form
   */
  public void ShowMainView() throws IOException
  {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Main.class.getResource("style/LoginView.fxml"));
    mainLayout = loader.load();
    Scene scene = new Scene(mainLayout);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /*
   * Display the registration scene on top of the main scene for a user to register onto the application
   */
  public static void ShowRegisterScene() throws IOException
  {
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

  /*
   * When an admin logs in display the appropriate view
   */
  public static void ShowAdminScene(Persona persona) throws IOException
  {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Main.class.getResource("style/AdminView.fxml"));
    mainLayout = loader.load();
    Scene scene = new Scene(mainLayout);
    primaryStage.setScene(scene);
    primaryStage.show();

    // Retrieve controller to set the logged in admin
    AdminViewController controller = loader
        .<AdminViewController> getController();
    controller.setUtente(persona);
  }

  /*
   * When a regular user logs in display the appropriate view
   */
  public static void ShowUserScene(Persona persona) throws IOException
  {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Main.class.getResource("style/UserView.fxml"));
    mainLayout = loader.load();
    Scene scene = new Scene(mainLayout);
    primaryStage.setScene(scene);
    primaryStage.show();

    // Retrieve controller to set the logged in user
    UserViewController controller = loader.<UserViewController> getController();
    controller.setUtente(persona);
  }

  /*
   * Close the register stage after pressing the register button
   */
  public static void CloseRegisterStage()
  {
    if (registerStage != null)
    {
      registerStage.close();
    }
  }

  /*
   * Display alert
   */
  public static void ShowErrorScene(String message)
  {
    System.out.println(message);
    Alert alert = new Alert(AlertType.WARNING);
    alert.setContentText(message);
    alert.setTitle("Alert");

    alert.showAndWait();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
