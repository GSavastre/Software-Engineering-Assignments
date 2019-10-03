package app;

import java.util.Scanner;

public class Persona {
  
  //TODO: Adattare lo scope degli attributi
  
  public String nome = "";
  public String cognome = "";
  public String mail = "";
  public String password = "";
  
  public Persona() {
		// TODO Auto-generated constructor stub
	  }
  
  Persona(String nome, String cognome, String mail, String password){
    this.nome = nome;
    this.cognome = cognome;
    this.mail = mail;
    this.password = password;
  }
  
  public void GetAttributes() {
	  System.out.println("Nome: "+ this.nome + "\nCognome: " + this.cognome + "\nMail: " + this.mail + "\nPassword: " + this.password); 
  }

//TODO: Aggiungi ripetizione della password
  public void SetAttributes() {
	  Scanner input = new Scanner(System.in);
	  
	  System.out.print("Nome:"); this.nome = input.nextLine();
	  System.out.print("Cognome: "); this.cognome = input.nextLine();
	  System.out.println("Mail: "); this.mail = input.nextLine();
	  System.out.println("Password: "); this.password = input.nextLine();
	  
  }
}