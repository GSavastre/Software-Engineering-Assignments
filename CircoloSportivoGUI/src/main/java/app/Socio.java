//Savastre Cosmin Gabriele 283110 Assegnamento 1

package app;

public class Socio extends Persona
{

  public Socio(String nome, String cognome, String mail, String password)
  {
    super(nome, cognome, mail, password);
  }

  public Socio()
  {
    super();
  }

  public Persona[] IscriviAdEvento(Evento evento)
  {
    return evento.AggiungiPersona(this);
  }

  public Persona[] CancellaIscrizioneAdEvento(Evento evento)
  {
    return evento.RimuoviPersona(this);
  }

  public boolean equals(Object obj)
  {
    return obj instanceof Socio && this.nome.equals(((Socio) obj).nome)
        && this.cognome.equals(((Socio) obj).cognome)
        && this.mail.equals(((Socio) obj).mail)
        && this.password.equals(((Socio) obj).password);
  }
}
