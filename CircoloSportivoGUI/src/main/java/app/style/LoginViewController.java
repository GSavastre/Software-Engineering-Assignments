package app.style;

import java.io.IOException;

import app.Main;
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
		String role = Auth.Login(txtEmail.getText(), txtPassword.getText());
		
		if(role.contentEquals("admin")) {
			Main.ShowAdminScene();
		}else if(role.contentEquals("socio")) {
			Main.ShowUserScene();
		}else {
			Main.ShowErrorScene("Login fallito");
		}
	}
}
