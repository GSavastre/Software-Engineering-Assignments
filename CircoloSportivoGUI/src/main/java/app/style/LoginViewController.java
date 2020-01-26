package app.style;

import java.io.IOException;

import app.Main;
import javafx.fxml.FXML;

public class LoginViewController {
	private Main main;
	
	@FXML
	private void btnRegister() throws IOException {
		main.ShowRegisterScene();
	}
}
