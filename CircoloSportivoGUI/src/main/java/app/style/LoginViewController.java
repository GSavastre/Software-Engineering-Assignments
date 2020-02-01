package app.style;

import java.io.IOException;

import app.Admin;
import app.Main;
import app.Persona;
import app.Socio;
import app.auth.Auth;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginViewController {
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtPassword;
	
	@FXML
	private void btnRegister() throws IOException {
		Main.ShowRegisterScene();
	}
	
	@FXML
	private void btnLogin() throws IOException{
		Persona login = Auth.Login(txtEmail.getText(), txtPassword.getText());
		
		if(login instanceof Admin) {
			Main.ShowAdminScene();
		}else if(login instanceof Socio) {
			Main.ShowUserScene();
		}else {
			Main.ShowErrorScene("Login fallito");
		}
	}
}
