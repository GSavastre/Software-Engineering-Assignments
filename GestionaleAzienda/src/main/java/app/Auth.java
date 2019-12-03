package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import filemanager.FileManager;
import personale.Impiegato;

public class Auth
{

  private static FileManager files = new FileManager();

  // Login di un impiegato in base alle credenziali
  public static Impiegato Login(String nome, String cognome, String password)
  {
    // Carico l'impiegato per il controllo
    Impiegato impiegato = Impiegato.CaricaDaFile(nome, cognome);

    // Se non esiste l'impiegato con quella combinazione nome-cognome
    if (impiegato.equals(null))
    {
      System.out.println("Login fallito, controlla le credenziali!");
      return null;
    }

    // Leggo il file Auth che contiene la coppia codice fiscale univoco -
    // password
    try (
        BufferedReader fin = new BufferedReader(new FileReader(files.FILEAUTH)))
    {
      String riga = null;
      String[] parametri;

      while ((riga = fin.readLine()) != null)
      {
        if (!riga.startsWith("#"))
        {
          parametri = riga.split(",");

          // Se le password combaciano allora il login e' avvenuto con successo
          if (impiegato.codiceFiscale.contentEquals(parametri[0])
              && password.contentEquals(parametri[1]))
          {
            return impiegato;
          }
        }
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
      e.getMessage();
      System.out.println("Impossibile procedere con l'accesso dell'impiegato!");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
      System.out.println("Errore nel processo di accesso dell'impiegato!");
    }

    return null;
  }

  // Lo stesso di login di sopra ma utilizzando String[]
  public static Impiegato Login(String[] credenziali)
  {

    String nome;
    String cognome;
    String password;

    try
    {
      nome = credenziali[0];
      cognome = credenziali[1];
      password = credenziali[2];
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
      return null;
    }

    Impiegato impiegato = Impiegato.CaricaDaFile(nome, cognome);

    if (impiegato.equals(null))
    {
      System.out.println("Login fallito, controlla le credenziali!");
      return null;
    }

    try (
        BufferedReader fin = new BufferedReader(new FileReader(files.FILEAUTH)))
    {
      String riga = null;
      String[] parametri;

      while ((riga = fin.readLine()) != null)
      {
        if (!riga.startsWith("#"))
        {
          parametri = riga.split(",");

          if (impiegato.codiceFiscale.contentEquals(parametri[0])
              && password.contentEquals(parametri[1]))
          {
            return impiegato;
          }
        }
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
      e.getMessage();
      System.out.println("Impossibile procedere con l'accesso dell'impiegato!");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
      System.out.println("Errore nel processo di accesso dell'impiegato!");
    }

    return null;
  }

  // Registra un nuovo impiegato sul file Auth
  public static boolean Register(Impiegato nuovoImpiegato, String password)
  {
    ArrayList<String> elementi = new ArrayList<String>();

    try (
        BufferedReader fin = new BufferedReader(new FileReader(files.FILEAUTH)))
    {
      String riga = null;

      while ((riga = fin.readLine()) != null)
      {
        if (!riga.startsWith("#"))
        {
          // Se il codice fiscale esiste (sarabbe piu' semplice usare il metodo
          // di Impiegato)
          if (nuovoImpiegato.codiceFiscale.contentEquals(riga.split(",")[0]))
          {
            return false;
          }
          else
          {
            elementi.add(riga);
          }
        }
      }

      // Aggiunta credenziali del nuovo impiegato
      elementi.add(String.join(",", nuovoImpiegato.codiceFiscale, password));
      // Sovrascrittura su file
      File fvecchio = new File(files.FILEAUTH);
      fvecchio.delete();

      File fout = new File(files.FILEAUTH);
      try
      {
        FileWriter fnuovo = new FileWriter(fout, false);
        // TODO: In questo caso l'uso di dictionary faciliterebbe la scrittura
        // di commenti in caso di cambiamento della struttura della classe (si
        // possono ciclare le chiavi)
        fnuovo.write("#codiceFiscale,password" + System.lineSeparator());

        for (String s : elementi)
        {
          fnuovo.write(s.toLowerCase() + System.lineSeparator());
        }

        fnuovo.close();
        return true;

      }
      catch (IOException e)
      {
        e.getMessage();
        System.out.println("Errore nella sovrascrittura del file impiegati!");
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
      e.getMessage();
      System.out.println(
          "Impossibile procedere con la registrazione del nuovo impiegato!");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
      System.out
          .println("Errore nel processo di registrazione del nuovo impiegato!");
    }

    return false;
  }
}
