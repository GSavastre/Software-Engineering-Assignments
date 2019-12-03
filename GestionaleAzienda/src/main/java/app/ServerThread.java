package app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import personale.Amministratore;
import personale.Dirigente;
import personale.Funzionario;
import personale.Impiegato;
import personale.Operaio;
import strutture.Sede;

public class ServerThread implements Runnable
{

  private Server server;
  private Socket socket;

  public ServerThread(final Server server, final Socket socket)
  {
    this.server = server;
    this.socket = socket;
  }

  public void run()
  {
    ObjectInputStream is = null;
    ObjectOutputStream os = null;

    try
    {
      is = new ObjectInputStream(
          new BufferedInputStream(this.socket.getInputStream()));
    }
    catch (Exception e)
    {
      e.getMessage();
      e.printStackTrace();

      return;
    }

    // Utile per l'identificazione del thread nel server durante le varie stampe
    // su console
    String id = String.valueOf(this.hashCode());

    try
    {
      Object message = is.readObject();

      if (message instanceof Request)
      {
        Request rq = (Request) message;
        Response rs = new Response();

        // Oggetto Impiegato che richiede l'azione, puo' essere null (ad es. in
        // caso di login)
        Impiegato richiedente = rq.GetRichiedente();

        // Azione richiesta
        String azione = rq.GetAzione();

        // Non conviene usare un array di string
        String[] parametri = rq.GetParametri().split(",");
        System.out.format("Thread %s receives %s request from client%n", id,
            azione);

        // Risultato dell'azione richiesta
        ArrayList<Impiegato> ris = new ArrayList<Impiegato>();

        switch (azione)
        {
          case "login":
          {
            Impiegato accesso = Auth.Login(parametri);

            // Il login ritorna un oggetto di tipo Impiegato
            if (accesso != null)
            {
              ris.add(accesso);
              rs.SetEsito(true);
              rs.SetRisultato(ris);
            }
            else
            {
              // Accesso fallito
              rs.SetEsito(false);
            }
          }
            break;

          // uso i parametri (string) del messaggio per generare un oggetto
          // impiegato invece di dover usare un oggetto da se nel messaggio che
          // verra' usato solo per la registrazione
          case "register":
          {
            String password = parametri[parametri.length - 1];
            if (Auth.Register(new Impiegato(parametri), password))
            {
              rs.SetEsito(true);
            }
          }
            break;

          case "search":
          {
            // Quanti impiegati si voglio ottenere max nel risultato
            int numRisultati;
            try
            {
              numRisultati = Integer.parseInt(parametri[0]);
            }
            catch (Exception e)
            {
              // Valore default altrimenti
              numRisultati = 10;
            }

            // Interpretazione classi da String a Class<?> per la ricerca
            ArrayList<Class<?>> mansioni = new ArrayList<Class<?>>();
            for (int i = 1; i < parametri.length; i++)
            {
              Class<?> mansione;
              switch (parametri[i])
              {
                case "Amministratore":
                  mansione = Amministratore.class;
                  break;
                case "Dirigente":
                  mansione = Dirigente.class;
                  break;
                case "Funzionario":
                  mansione = Funzionario.class;
                  break;
                default:
                  mansione = Operaio.class;
                  break;
              }
              try
              {
                // Se il richiedente è un dirigente
                if (richiedente instanceof Dirigente)
                {
                  // Non aggiungere gli amministratori alla ricerca
                  if (!mansione.isInstance(Amministratore.class))
                  {
                    mansioni.add(mansione);
                  }
                }
                else
                {
                  mansioni.add(mansione);
                }
              }
              catch (Exception e)
              {
                e.getMessage();
                System.out.println(
                    "Errore nell riconoscimento della classe " + parametri[i]);
              }
            }

            // Nel caso non siano riconosciute le mansioni cerca solo gli operai
            if (mansioni.size() == 0)
            {
              mansioni.add(Operaio.class);
            }

            ris = Sede.CaricaDaFile(richiedente.sedeLavorativa)
                .Ricerca(numRisultati, mansioni);

            if (ris.size() > 0)
            {
              rs.SetEsito(true);
              rs.SetRisultato(ris);
            }
            else
            {
              rs.SetEsito(false);
            }
          }
            break;

          case "update":
          {
            // Codice fiscale dell'impiegato da modificare
            String cf = parametri[0];
            String[] paramNuovi = Arrays.copyOfRange(parametri, 1,
                parametri.length);
            // Se il codice fiscale della persona da modificare non esiste
            // oppure appartiene alla persona stessa da modificare esegui
            // modificas
            if (!Impiegato.CodiceFiscaleEsiste(paramNuovi[2])
                || cf.contentEquals(paramNuovi[2]))
            {
              // Carico la "vecchia" personas
              Impiegato impiegato = Impiegato.CaricaDaFile(cf);
              if (impiegato != null)
              {
                // Ottenere la classe è importante per salvare anche il ruolo
                // sul file csv
                switch (impiegato.getClass().getSimpleName())
                {
                  case "Operaio":
                    impiegato.SalvaSuFile(new Operaio(paramNuovi));
                    break;
                  case "Dirigente":
                    impiegato.SalvaSuFile(new Dirigente(paramNuovi));
                    break;
                  case "Funzionario":
                    impiegato.SalvaSuFile(new Funzionario(paramNuovi));
                    break;
                  case "Amministratore":
                    impiegato.SalvaSuFile(new Amministratore(paramNuovi));
                    break;
                }
              }
              else
              {
                // Genero una nuova persona
                try
                {
                  // Il ruolo verrà eventualmente messo alla fine
                  int ruolo = Integer
                      .parseInt(paramNuovi[paramNuovi.length - 1]);
                  switch (ruolo)
                  {
                    case 1:
                      impiegato = new Amministratore(paramNuovi);
                      break;
                    case 2:
                      impiegato = new Dirigente(paramNuovi);
                      break;
                    case 3:
                      impiegato = new Funzionario(paramNuovi);
                      break;
                    default:
                      impiegato = new Operaio(paramNuovi);
                      break;
                  }
                }
                catch (Exception e)
                {
                  System.out.println("Invalid role for worker creation");
                  impiegato = new Operaio(paramNuovi);
                }
              }

              ris.add(impiegato);
              rs.SetEsito(true);
              rs.SetRisultato(ris);
            }
            else
            {
              rs.SetEsito(false);
            }
          }
            break;

          default:
            System.out.format("Request %s is not recognized by thread %s%n",
                azione, id);
            break;
        }

        if (os == null)
        {
          os = new ObjectOutputStream(
              new BufferedOutputStream(this.socket.getOutputStream()));
        }

        System.out.format("Thread %s sends: %s result to its client%n", id,
            rs.GetEsito());
        os.writeObject(rs);
        os.flush();

        // TODO:Causa problemi

        /*if(this.server.GetPool().getActiveCount() == 1) {
        	this.server.Close();
        }*/

        this.socket.close();
        return;

      }
    }
    catch (ClassNotFoundException | IOException e)
    {
      e.getMessage();
      e.printStackTrace();

      System.exit(0);
    }
  }
}
