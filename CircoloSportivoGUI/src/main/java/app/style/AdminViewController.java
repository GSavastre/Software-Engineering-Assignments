package app.style;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import app.Admin;
import app.Corso;
import app.Evento;
import app.Gara;
import app.Main;
import app.Persona;
import app.Socio;
import app.auth.Auth;
import app.database.DB;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class AdminViewController
{
  private Admin utente;

  // It would be better to create a separate table with roles and get the
  // various roles from there instead of having them hardcoded
  ObservableList<String> eventTypes = FXCollections.observableArrayList("gara",
      "corso");
  ObservableList<String> roles = FXCollections.observableArrayList("socio",
      "admin");

  private ObservableList<Evento> eventi;
  private ObservableList<Persona> utenti;

  private Evento eventoSelezionato;
  private Persona utenteSelezionato;

  public void setUtente(Persona persona)
  {
    this.utente = (Admin) persona;
  }

  @FXML
  private TableView<Evento> tbEvents;

  @FXML
  private TableColumn<Evento, String> colEventName;

  @FXML
  private TextField txtEventName;

  @FXML
  private ChoiceBox<String> cbEventType;

  @FXML
  private TableView<Persona> tbUsers;

  @FXML
  private TableColumn<Persona, String> colUserEmails;

  @FXML
  private TextField txtName;

  @FXML
  private ChoiceBox<String> cbRole;

  @FXML
  private TextField txtLastName;

  @FXML
  private TextField txtEmail;

  @FXML
  private TextField txtPassword;

  @FXML
  private VBox vbEventsControls;

  @FXML
  private VBox vbUsersControls;

  @FXML
  private Label lblEventIstruction, lblUserIstruction;

  // TODO: IMplement change choice box values to original values of events/users
  // when selected
  @SuppressWarnings("unchecked")
  @FXML
  private void initialize() throws IOException
  {

    // Disable users and events controls until user or event is selected from
    // the respective TableView
    vbUsersControls.setDisable(true);
    vbEventsControls.setDisable(true);

    cbEventType.setValue(eventTypes.get(0));
    cbEventType.setItems(eventTypes);

    cbRole.setValue(roles.get(0));
    cbRole.setItems(roles);

    /*
     * 
     * TableView cell selection listeners
     * 
     */

    try
    {
      colEventName.setCellValueFactory(
          new PropertyValueFactory<Evento, String>("nome"));
      colUserEmails.setCellValueFactory(
          new PropertyValueFactory<Persona, String>("mail"));
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }

    tbEvents.getSelectionModel().setCellSelectionEnabled(true);
    ObservableList selectedEventCell = tbEvents.getSelectionModel()
        .getSelectedCells();

    tbUsers.getSelectionModel().setCellSelectionEnabled(true);
    ObservableList selectedUserCell = tbUsers.getSelectionModel()
        .getSelectedCells();

    selectedEventCell.addListener(new ListChangeListener()
    {
      public void onChanged(Change c)
      {
        TablePosition tablePosition = (TablePosition) selectedEventCell.get(0);
        Object val = tablePosition.getTableColumn()
            .getCellData(tablePosition.getRow());

        Evento search = new Evento(val.toString());

        eventoSelezionato = eventi.get(eventi.indexOf(search));

        // System.out.println("Evento selezionato "+ eventoSelezionato.nome+"
        // "+eventoSelezionato.getClass().toString());

        vbEventsControls.setDisable(false);
        lblEventIstruction.setVisible(false);

        txtEventName.setText(eventoSelezionato.nome);
        // System.out.println("Setto choice box a
        // :"+eventoSelezionato.getClass().getSimpleName());
        cbEventType.setValue(
            eventoSelezionato.getClass().getSimpleName().toLowerCase());

        // System.out.println("Ricerca iscritto
        // :"+Integer.toString(eventoSelezionato.PresenzaIscritto(utente)));

      }
    });

    selectedUserCell.addListener(new ListChangeListener()
    {
      public void onChanged(Change c)
      {
        TablePosition tablePosition = (TablePosition) selectedUserCell.get(0);
        Object val = tablePosition.getTableColumn()
            .getCellData(tablePosition.getRow());

        Persona search = null;

        for (Persona p : utenti)
        {
          if (p.mail.contentEquals(val.toString()))
          {
            search = p;
          }
        }

        if (search != null)
        {
          utenteSelezionato = search;
          lblUserIstruction.setVisible(false);
          vbUsersControls.setDisable(false);
          txtName.setText(search.nome);
          txtLastName.setText(search.cognome);
          txtEmail.setText(search.mail);
          txtPassword.setText(search.password);
          cbRole.setValue(search.getClass().getSimpleName().toLowerCase());
        }
      }
    });

    /*
     * End of listeners
     */

    LoadData();
  }

  // TODO: Muovi i metodi load in DB + aggiungi id agli oggetti Persona ed
  // Evento
  private void LoadData()
  {
    eventi = FXCollections.observableArrayList();
    utenti = FXCollections.observableArrayList();

    try (
        Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS, DB.USER,
            DB.PASSWORD);
        Statement stmt = conn.createStatement();)
    {
      String eventsQueryString = "SELECT * FROM eventi;";
      String usersQueryString = "SELECT * FROM utenti;";

      // Fetch events
      ResultSet rset = stmt.executeQuery(eventsQueryString);
      while (rset.next())
      {
        String rsNome = rset.getString("nome");
        String rsTipo = rset.getString("tipo");

        if (rsTipo.contentEquals("corso"))
        {
          eventi.add(new Corso(rsNome));
        }
        else if (rsTipo.contentEquals("gara"))
        {
          eventi.add(new Gara(rsNome));
        }
      }

      // Fetch users
      rset = stmt.executeQuery(usersQueryString);
      while (rset.next())
      {
        String rsName = rset.getString("nome");
        String rsLastName = rset.getString("cognome");
        String rsEmail = rset.getString("email");
        String rsPassword = rset.getString("password");
        String rsRole = rset.getString("ruolo");

        if (rsRole.contentEquals("socio"))
        {
          utenti.add(new Socio(rsName, rsLastName, rsEmail, rsPassword));
        }
        else if (rsRole.contentEquals("admin"))
        {
          utenti.add(new Admin(rsName, rsLastName, rsEmail, rsPassword));
        }
      }

    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }

    // Update TableViews
    try
    {
      tbEvents.setItems(eventi);
      tbUsers.setItems(utenti);
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }

  }

  @FXML
  private void AddEvent()
  {
    try (
        Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS, DB.USER,
            DB.PASSWORD);
        Statement stmt = conn.createStatement();)
    {
      // If values are incorrect we will get an SQL error from db
      String eventName = txtEventName.getText().strip();
      String eventType = cbEventType.getValue();

      String nameValue = String.format("\'%s\'", eventName);
      String typeValue = String.format("\'%s\'", eventType);
      String values = String.join(",", nameValue, typeValue);
      String queryString = "INSERT INTO eventi (nome,tipo) VALUES(" + values
          + ")";
      System.out.println(queryString);

      int countUpdated = stmt.executeUpdate(queryString);

      // Only if the query was executed with success we will then update the
      // tableView
      if (countUpdated != 0)
      {
        if (eventType.contentEquals("corso"))
        {
          eventi.add(new Corso(eventName));
        }
        else if (eventType.contentEquals("gara"))
        {
          eventi.add(new Gara(eventName));
        }
        tbEvents.setItems(eventi);
      }

    }
    catch (SQLException e)
    {
      e.printStackTrace();
      Main.ShowErrorScene(e.getMessage());
    }
  }

  /*
   * Add a new user to the db
   */
  @FXML
  private void AddUser()
  {
    // New users info
    String userName = txtName.getText().strip();
    String userLastName = txtLastName.getText().strip();
    String userEmail = txtEmail.getText().strip();
    String userPassword = txtPassword.getText().strip();
    String userRole = cbRole.getValue().strip();

    // Use existing method to register a new user
    if (Auth.Register(userName, userLastName, userEmail, userPassword,
        userPassword, userRole))
    {
      if (userRole.contentEquals("socio"))
      {
        utenti.add(new Socio(userName, userLastName, userEmail, userPassword));
      }
      else if (userRole.contentEquals("admin"))
      {
        utenti.add(new Admin(userName, userLastName, userEmail, userPassword));
      }
      tbUsers.setItems(utenti);
    }
  }

  /*
   * Get current values from the scene nodes and compare them to the event selected in the tableView
   * if the values do not correspond it means that there are new values to update the selected event
   */
  @FXML
  private void ModifyEvent()
  {

    // New possible values
    String eventName = txtEventName.getText().strip();
    String eventType = cbEventType.getValue();
    Evento modifiedEvent = null;

    if (eventType.contentEquals("corso"))
    {
      modifiedEvent = new Corso(eventName);
    }
    else if (eventType.contentEquals("gara"))
    {
      modifiedEvent = new Gara(eventName);
    }

    // Check if the current values in the textField and choiceBox are different
    // from the original event
    if (!eventoSelezionato.equals(modifiedEvent))
    {

      // If they are not equal open a connection to DB and update the event
      // (every event name must be unique on the DB table)
      try (
          Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS,
              DB.USER, DB.PASSWORD);
          Statement stmt = conn.createStatement();)
      {

        String queryString = "UPDATE eventi SET nome ='" + eventName
            + "', tipo = '" + eventType + "' WHERE nome = '"
            + eventoSelezionato.nome + "'";
        int countUpdated = stmt.executeUpdate(queryString);
        if (countUpdated != 0)
        {
          eventi.remove(eventoSelezionato);
          eventi.add(modifiedEvent);
          tbEvents.setItems(eventi);
        }
      }
      catch (SQLException e)
      {
        System.out.println(e.getMessage());
      }
    }
  }

  /*
   * Same as modifyEvent but with users
   */
  @FXML
  private void ModifyUser()
  {

    // Values from the nodes to compare to the tableView user
    String userName = txtName.getText().strip();
    String userLastName = txtLastName.getText().strip();
    String userEmail = txtEmail.getText().strip();
    String userPassword = txtPassword.getText().strip();
    String userRole = cbRole.getValue();

    Persona modifiedUser = null;
    if (userRole.contentEquals("socio"))
    {
      modifiedUser = new Socio(userName, userLastName, userEmail, userPassword);
    }
    else if (userRole.contentEquals("admin"))
    {
      modifiedUser = new Admin(userName, userLastName, userEmail, userPassword);
    }

    // Check if selected user from tableView equals the one created with the
    // values of the nodes
    if (!utenteSelezionato.equals(modifiedUser))
    {
      try (
          Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS,
              DB.USER, DB.PASSWORD);
          Statement stmt = conn.createStatement();)
      {

        String queryString = "UPDATE utenti SET nome ='" + userName + "', "
            + "cognome = '" + userLastName + "', " + "email = '" + userEmail
            + "', " + "password = '" + userPassword + "', " + "ruolo = '"
            + userRole + "' " + "WHERE email = '" + utenteSelezionato.mail
            + "'";
        int countUpdated = stmt.executeUpdate(queryString);
        if (countUpdated != 0)
        {
          utenti.remove(utenteSelezionato);
          utenti.add(modifiedUser);
          tbUsers.setItems(utenti);
        }
      }
      catch (SQLException e)
      {
        System.out.println(e.getMessage());
      }
    }
  }

  /*
   * Delete a selected event from the DB and from the tableView
   */
  @FXML
  private void DeleteEvent() throws IOException
  {
    try (
        Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS, DB.USER,
            DB.PASSWORD);
        Statement stmt = conn.createStatement();)
    {
      String queryString = "DELETE FROM eventi WHERE nome = '"
          + eventoSelezionato.nome + "'";

      int countUpdated = stmt.executeUpdate(queryString);
      if (countUpdated != 0)
      {
        if (eventi.remove(eventoSelezionato))
        {
          tbEvents.setItems(eventi);
        }
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
  }

  /*
   * Delete a selected user from the DB and from the tableView
   */
  @FXML
  private void DeleteUser() throws IOException
  {
    try (
        Connection conn = DriverManager.getConnection(DB.URL + DB.ARGS, DB.USER,
            DB.PASSWORD);
        Statement stmt = conn.createStatement();)
    {
      try
      {
        String queryString = "DELETE FROM utenti WHERE email='"
            + utenteSelezionato.mail + "'";
        int countUpdated = stmt.executeUpdate(queryString);
        if (countUpdated != 0)
        {
          if (utenti.remove(utenteSelezionato))
          {
            tbUsers.setItems(utenti);
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
        System.out.println("Errore delete utente");
      }

    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
  }

  /*
   * Reset the event nodes values to their original values
   */
  @FXML
  private void CancelEventModification()
  {
    txtEventName.setText(eventoSelezionato.getNome());
    cbEventType
        .setValue(eventoSelezionato.getClass().getSimpleName().toLowerCase());
  }

  /*
   * Reset the user nodes values to their original values
   */
  @FXML
  private void CancelUserModification()
  {
    txtName.setText(utenteSelezionato.nome);
    txtLastName.setText(utenteSelezionato.cognome);
    txtEmail.setText(utenteSelezionato.mail);
    txtPassword.setText(utenteSelezionato.password);
    cbRole.setValue(utenteSelezionato.getClass().getSimpleName().toLowerCase());
  }
}
