package app;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import com.github.javafaker.Faker;

import personale.*;
import strutture.Sede;

public class Client
{

  private static final int PORT = 5555;
  private static final String HOST = "localhost";

  private static final int MAXIMPIEGATI = 3;
  private static final int MAXSEDI = 3;
  private static final int SLEEPTIME = 4000;
  private static Faker nameGenerator = new Faker();

  private static ArrayList<Sede> sedi = Sede.CaricaDaFile();

  public static void main(String[] args)
  {
    System.out.println(
        "Per controllare il corretto funzionamento della concorrenza da parte di molteplici clienti bisogna avviare piu' Client.java");
    // Genera sedi casuali e per ogni sede un insieme di impiegati casuali
    Init();
    new Client().Run();
  }

  public void Run()
  {
    try
    {
      Socket client = new Socket(HOST, PORT);
      Random r = new Random();
      Request rq;

      ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
      ObjectInputStream is = null;

      // Scelgo una sede casuale dalla lista di sedi
      int scelta = r.nextInt(sedi.size());
      Sede sceltaSede = sedi.get(scelta);

      // Prendo impiegati di vario ruolo casualmente dalla sede scelta
      Funzionario rndFunz = sceltaSede.GetRndFunzionario();
      Dirigente rndDirigente = sceltaSede.GetRndDirigente();
      Amministratore rndAmministratore = sceltaSede.GetRndAmministratore();

      System.out.format(
          "\nSvolgo operazioni di ricerca per il dirigente %s %s%n",
          rndDirigente.nome, rndDirigente.cognome);

      // Il numero di impiegati da stampare
      int numImpiegati = r.nextInt(sceltaSede.impiegati.size()) + 1;

      // I parametri della ricerca sono composti da num di impiegati e i loro
      // ruoli
      /*Da notare il fatto che è stato richiesto di stampare gli amministratori ma il server
       * si rende conto del fatto che la richiesta sia stata eseguita da un dirigente e quindi non
       * stampa gli amministratori (come da consegna)
       */

      String paramRicerca = String.valueOf(numImpiegati)
          + ",Amministratore,Dirigente,Operaio,Funzionario";
      rq = new Request(rndDirigente, "search", paramRicerca);

      os.writeObject(rq);
      os.flush();

      if (is == null)
      {
        is = new ObjectInputStream(
            new BufferedInputStream(client.getInputStream()));
      }

      Object o = is.readObject();

      if (o instanceof Response)
      {
        Response rs = (Response) o;

        // Se l'esito della ricerca è falso oppure non si hanno impiegati nella
        // lista di impiegati nell'oggetto Response allora stampa messaggio di
        // errore
        if (rs.GetEsito() == false || rs.GetRisultato().size() == 0)
        {

          System.out.format("Ricerca fallita per %s %sn", rndDirigente.nome,
              rndDirigente.cognome);
        }
        else
        {
          for (Impiegato i : rs.GetRisultato())
          {
            i.Print();
          }
        }
      }
      // Chiude client e simula il logout e il login del prossimo impiegato
      // La funzione di login esiste in Auth e sul serverThread ma non viene
      // spedita una richiesta dal Client per semplicità
      client.close();
      Thread.sleep(SLEEPTIME);

      System.out.format(
          "\n\nSvolgo operazioni di ricerca per l'amministratore %s %s%n",
          rndAmministratore.nome, rndAmministratore.cognome);

      client = new Socket(HOST, PORT);
      os = new ObjectOutputStream(client.getOutputStream());
      is = null;

      // Stessi parametri per la ricerca da parte di un dirigente, il server
      // avrà l'incarico di capire chi chiama la ricerca
      numImpiegati = r.nextInt(sceltaSede.impiegati.size()) + 1;
      paramRicerca = String.valueOf(numImpiegati)
          + ",Amministratore,Dirigente,Operaio,Funzionario";
      rq = new Request(rndAmministratore, "search", paramRicerca);

      os.writeObject(rq);
      os.flush();

      if (is == null)
      {
        is = new ObjectInputStream(
            new BufferedInputStream(client.getInputStream()));
      }

      o = is.readObject();

      if (o instanceof Response)
      {
        Response rs = (Response) o;

        if (rs.GetEsito() == false || rs.GetRisultato().size() == 0)
        {

          System.out.format("Ricerca fallita per %s %sn",
              rndAmministratore.nome, rndAmministratore.cognome);
        }
        else
        {
          for (Impiegato i : rs.GetRisultato())
          {
            i.Print();
          }
        }
      }

      // Chiude client e simula il logout e il login del prossimo impiegato
      client.close();

      Thread.sleep(SLEEPTIME);
      System.out.format(
          "\n\nSvolgo operazioni di modifica dati per il funzionario %s %s%n",
          rndFunz.nome, rndFunz.cognome);
      client = new Socket(HOST, PORT);
      os = new ObjectOutputStream(client.getOutputStream());
      is = null;

      // Proviamo ad assegnare il codice fiscale dell'amministratore al
      // dirigente
      String parametriInvalidi = String.join(",", rndDirigente.codiceFiscale,
          rndDirigente.nome, rndDirigente.cognome,
          rndAmministratore.codiceFiscale, rndDirigente.sedeLavorativa,
          rndDirigente.inizioAttivita.toString(),
          rndDirigente.fineAttivita.now().plusYears(10).toString());

      // Proviamo ad assegnare dei valori validi
      String parametriValidi = String.join(",", rndDirigente.codiceFiscale,
          "pippo", "pluto", rndDirigente.codiceFiscale,
          rndDirigente.sedeLavorativa, rndDirigente.inizioAttivita.toString(),
          rndDirigente.fineAttivita.now().plusYears(10).toString());

      rq = new Request(rndFunz, "update", parametriInvalidi);

      os.writeObject(rq);
      os.flush();

      if (is == null)
      {
        is = new ObjectInputStream(
            new BufferedInputStream(client.getInputStream()));
      }

      o = is.readObject();

      if (o instanceof Response)
      {
        Response rs = (Response) o;

        if (rs.GetEsito() == false || rs.GetRisultato().size() == 0)
        {
          System.out.format("\nAssegnamento dati fallito per %s %sn",
              rndFunz.nome, rndFunz.cognome);
        }
        else
        {
          System.out.format(
              "\nAssegnamento dati completato con successo per %s %s%n",
              rndFunz.nome, rndFunz.cognome);
          for (Impiegato i : rs.GetRisultato())
          {
            i.Print();
          }
        }
      }
      // Chiude client e simula il logout e il login del prossimo impiegato
      client.close();

      Thread.sleep(SLEEPTIME);
      client = new Socket(HOST, PORT);
      os = new ObjectOutputStream(client.getOutputStream());
      rq = new Request(rndFunz, "update", parametriValidi);

      os.writeObject(rq);
      os.flush();

      is = new ObjectInputStream(
          new BufferedInputStream(client.getInputStream()));

      o = is.readObject();

      if (o instanceof Response)
      {
        Response rs = (Response) o;

        if (rs.GetEsito() == false || rs.GetRisultato().size() == 0)
        {

          System.out.format("\nAssegnamento dati fallito per %s %sn",
              rndFunz.nome, rndFunz.cognome);
        }
        else
        {
          System.out.format(
              "\nAssegnamento dati completato con successo per %s %s%n",
              rndFunz.nome, rndFunz.cognome);
          for (Impiegato i : rs.GetRisultato())
          {
            i.Print();
          }
        }
      }

      client.close();
    }
    catch (Exception e)
    {
      e.getMessage();
      e.printStackTrace();
    }
  }

  // Genera valori casuali per sedi e impiegati
  public static void Init()
  {

    Random r = new Random();
    Sede nuovaSede;
    String companyName;
    String companyAddress;

    for (int i = 0; i < MAXSEDI; i++)
    {
      companyName = nameGenerator.company().name();
      companyAddress = nameGenerator.address().streetAddress(false);
      // Non prendo in considerazione i nomi di sedi già esistenti e quelli che
      // contengano la virgola siccome possono interferire con la lettura del
      // file csv
      if (!companyName.contains(",") && !Sede.NomeDuplicato(companyName))
      {
        nuovaSede = new Sede(companyName, companyAddress);
        nuovaSede.SalvaSuFile();
        sedi.add(nuovaSede);
      }
    }

    // Genera un numero casuali di impiegati
    int maxImpiegati = r.nextInt(MAXIMPIEGATI) + 1;
    Impiegato nuovoImpiegato;

    // Per ogni sede genera almeno un impiegato di ogni tipo oltre che a quelli
    // casuali
    for (Sede s : sedi)
    {
      if (s.GetNumFunzionari() == 0)
      {
        Funzionario fakeFunz = new Funzionario(nameGenerator.name().firstName(),
            nameGenerator.name().lastName(), s.nome, LocalDate.now());
        Auth.Register(fakeFunz, fakeFunz.codiceFiscale);
        s.AddImpiegato(fakeFunz);
      }

      if (s.GetNumDirigenti() == 0)
      {
        Dirigente fakeDir = new Dirigente(nameGenerator.name().firstName(),
            nameGenerator.name().lastName(), s.nome, LocalDate.now());
        Auth.Register(fakeDir, fakeDir.codiceFiscale);
        s.AddImpiegato(fakeDir);
      }

      if (s.GetNumAmministratori() == 0)
      {
        Amministratore fakeAmm = new Amministratore(
            nameGenerator.name().firstName(), nameGenerator.name().lastName(),
            s.nome, LocalDate.now());
        Auth.Register(fakeAmm, fakeAmm.codiceFiscale);
        s.AddImpiegato(fakeAmm);
      }

      if (s.GetNumOperai() == 0)
      {
        Operaio fakeOp = new Operaio(nameGenerator.name().firstName(),
            nameGenerator.name().lastName(), s.nome, LocalDate.now());
        Auth.Register(fakeOp, fakeOp.codiceFiscale);
        s.AddImpiegato(fakeOp);
      }

      for (int i = 0; i < maxImpiegati; i++)
      {
        switch (r.nextInt(4))
        {
          case 0:
            nuovoImpiegato = new Operaio(nameGenerator.name().firstName(),
                nameGenerator.name().lastName(), s.nome, LocalDate.now());
            break;
          case 1:
            nuovoImpiegato = new Amministratore(
                nameGenerator.name().firstName(),
                nameGenerator.name().lastName(), s.nome, LocalDate.now());
            break;
          case 2:
            nuovoImpiegato = new Dirigente(nameGenerator.name().firstName(),
                nameGenerator.name().lastName(), s.nome, LocalDate.now());
            break;
          case 3:
            nuovoImpiegato = new Funzionario(nameGenerator.name().firstName(),
                nameGenerator.name().lastName(), s.nome, LocalDate.now());
            break;

          default:
            nuovoImpiegato = new Operaio(nameGenerator.name().firstName(),
                nameGenerator.name().lastName(), s.nome, LocalDate.now());
        }

        nuovoImpiegato.SalvaSuFile(nuovoImpiegato);
        Auth.Register(nuovoImpiegato, nuovoImpiegato.codiceFiscale);
        s.AddImpiegato(nuovoImpiegato);
      }
    }
  }
}
