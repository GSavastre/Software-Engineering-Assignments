package personale;

import java.time.LocalDate;

import strutture.Sede;

public class Dirigente extends Impiegato
{

  public Dirigente(String nome, String cognome, String codiceFiscale,
      /*Sede*/String sedeLavorativa, LocalDate inizioAttivita,
      LocalDate fineAttivita)
  {
    super(nome, cognome, codiceFiscale, sedeLavorativa, inizioAttivita,
        fineAttivita);
  }

  public Dirigente(String nome, String cognome, /*Sede*/String sedeLavorativa,
      LocalDate inizioAttivita)
  {
    super(nome, cognome, sedeLavorativa, inizioAttivita);
  }

  public Dirigente(String[] parametri)
  {
    super(parametri);
  }

  public void SalvaSuFile()
  {
    super.SalvaSuFile(this);
  }
}
