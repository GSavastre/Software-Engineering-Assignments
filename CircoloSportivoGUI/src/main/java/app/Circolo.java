//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

public class Circolo
{

  public String nome = "";
  public Persona[] partecipanti = null;
  public Evento[] eventi = null;

  Circolo(String nome)
  {
    this.nome = nome;
  }

  Circolo(String nome, Persona[] partecipanti)
  {
    this.nome = nome;
    this.partecipanti = partecipanti;
  }

  Circolo(String nome, Evento[] eventi)
  {
    this.nome = nome;
    this.eventi = eventi;
  }

  Circolo(String nome, Persona[] partecipanti, Evento[] eventi)
  {
    this.nome = nome;
    this.partecipanti = partecipanti;
    this.eventi = eventi;
  }

  public Persona[] AggiungiPersona(Persona persona)
  {
    if (partecipanti == null)
    {
      partecipanti = new Persona[0];
    }

    // Creo nuovo array maggiorato per il nuovo partecipante
    Persona[] nuovaLista = new Persona[partecipanti.length + 1];

    // Controllo che la persona non sia già un partecipante del circolo
    if (this.PresenzaIscritto(persona) == -1)
    {
      System.arraycopy(partecipanti, 0, nuovaLista, 0, partecipanti.length);
      nuovaLista[partecipanti.length] = persona;

      partecipanti = nuovaLista;
    }

    return partecipanti;
  }

  /*
     * Rimuove una persona dalla lista di iscritti dell'evento
     * Nel caso l'evento non abbia iscritti oppure non esista tale persona nella lista di iscritti verrà ritornata la
     * lista corrente di iscritti all'evento.
     */
  public Persona[] RimuoviPersona(Persona persona)
  {

    if (partecipanti != null)
    {
      // Creo nuovo array ridotto per l'esclusione della persona rimossa
      Persona[] nuovaLista = new Persona[partecipanti.length - 1];

      // Controllo che la persona sia già iscritta all'evento
      int indicePresenza = this.PresenzaIscritto(persona);

      if (indicePresenza == 0)
      {
        System.arraycopy(partecipanti, 1, nuovaLista, 0,
            partecipanti.length - 1);
        partecipanti = nuovaLista;
      }
      else if (indicePresenza > 0)
      {
        // Ricopio la prima parte dell'array
        System.arraycopy(partecipanti, 0, nuovaLista, 0, indicePresenza);

        // Ricopio la seconda parte escludendo la persona rimossa
        System.arraycopy(partecipanti, indicePresenza + 1, nuovaLista,
            indicePresenza, partecipanti.length - indicePresenza - 1);

        partecipanti = nuovaLista;
      }
    }

    return partecipanti;
  }

  /*
   * Aggiunge un evento alla lista di eventi del circolo
   * nel caso non ci siano ancora eventi registrati all'interno del circolo
   * verrà creato un nuovo array con il nuovo evento come unico elemento
   */

  public Evento[] AggiungiEvento(Evento evento)
  {
    if (eventi == null)
    {
      eventi = new Evento[0];
    }

    // Creo nuovo array maggiorato per il nuovo evento
    Evento[] nuovaLista = new Evento[eventi.length + 1];

    // Controllo che l'evento non sia già registrato nel circolo
    if (this.PresenzaEvento(evento) == -1)
    {
      System.arraycopy(eventi, 0, nuovaLista, 0, eventi.length);
      nuovaLista[eventi.length] = evento;

      eventi = nuovaLista;
    }

    return eventi;
  }

  /*
   * Rimuove un evento dall'array di eventi del circolo
   * nel caso non ci siano eventi all'interno del circolo, non sarà rimosso niente
   */
  public Evento[] RimuoviEvento(Evento evento)
  {
    if (eventi != null && eventi.length > 0)
    {
      // Creo nuovo array ridotto per l'esclusione dell'evento rimosso
      Evento[] nuovaLista = new Evento[eventi.length - 1];

      // Controllo che l'evento sia già registrato nel circolo
      int indicePresenza = this.PresenzaEvento(evento);

      if (indicePresenza == 0)
      {
        System.arraycopy(eventi, 1, nuovaLista, 0, eventi.length - 1);
        eventi = nuovaLista;
      }
      else if (indicePresenza > 0)
      {
        // Ricopio la prima parte dell'array
        System.arraycopy(eventi, 0, nuovaLista, 0, indicePresenza);

        // Ricopio la seconda parte escludendo l'evento rimosso
        System.arraycopy(eventi, indicePresenza + 1, nuovaLista, indicePresenza,
            eventi.length - indicePresenza - 1);

        eventi = nuovaLista;
      }
    }

    return eventi;
  }

  /*Cerca l'indice in cui si trova un evento all'interno dell'array di eventi
     * Nel caso non sia trovata ritorna -1
     */
  public int PresenzaEvento(Evento evento)
  {
    int indiceTrovato = -1;

    for (int i = 0; i < this.eventi.length; i++)
    {
      // Overload della funzione equals() per classe di tipo Evento
      if (evento.equals(eventi[i]))
      {
        indiceTrovato = i;
      }
    }

    return indiceTrovato;
  }

  public int PresenzaIscritto(Persona persona)
  {
    int indiceTrovato = -1;

    for (int i = 0; i < this.partecipanti.length; i++)
    {
      // Overload della funzione equals() per classe di tipo Persona
      if (persona.equals(partecipanti[i]))
      {
        indiceTrovato = i;
      }
    }

    return indiceTrovato;
  }

}
