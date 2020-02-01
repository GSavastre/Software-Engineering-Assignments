package app.style;

import java.io.IOException;

import app.Main;
import app.auth.Auth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class RegisterViewController {
	
	ObservableList<String> roles = FXCollections.observableArrayList("socio", "admin");
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtLastName;
	
	@FXML
	private TextField txtMail;
	
	@FXML
	private TextField txtPassword;
	
	@FXML
	private TextField txtPasswordRepeat;
	
	@FXML
	private ChoiceBox drbRole;
	
	@FXML
	private void initialize() throws IOException{
		drbRole.setValue(roles.get(0));
		drbRole.setItems(roles);
	}
	
	@FXML
	private void btnRegister() throws IOException{
		if(Auth.Register(txtName.getText(), txtLastName.getText(), txtMail.getText(), txtPassword.getText(), txtPasswordRepeat.getText(), drbRole.getValue().toString())) {
			System.out.println("Registrazione avvenuta con successo");
		}else {
			System.out.println("Errore registrazione");
		}
		
		Main.CloseRegisterStage();
	}
	
	@FXML
	private void btnCancel() throws IOException{
		Main.CloseRegisterStage();
	}
}
