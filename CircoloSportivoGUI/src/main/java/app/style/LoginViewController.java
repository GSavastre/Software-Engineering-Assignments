package app.style;

import java.io.IOException;

import app.Admin;
import app.Main;
import app.Persona;
import app.Socio;
import app.auth.Auth;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginViewController
{
  @FXML
  private TextField txtEmail;

  @FXML
  private TextField txtPassword;

  /*
   * Display the register scene
   */
  @FXML
  private void btnRegister() throws IOException
  {
    Main.ShowRegisterScene();
  }

  /*
   * Login a user based on the credentials and display the correct scene
   */
  @FXML
  private void btnLogin() throws IOException
  {
    Persona login = Auth.Login(txtEmail.getText(), txtPassword.getText());

    if (login instanceof Admin)
    {
      Main.ShowAdminScene(login);
      // Main.ShowErrorScene("Login avvenuto con successo!");
    }
    else if (login instanceof Socio)
    {
      Main.ShowUserScene(login);
      // Main.ShowErrorScene("Login avvenuto con successo!");
    }
    else
    {
      Main.ShowErrorScene("Login fallito");
    }
  }
}
