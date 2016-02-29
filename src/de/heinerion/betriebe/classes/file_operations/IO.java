package de.heinerion.betriebe.classes.file_operations;

import de.heinerion.betriebe.classes.data.RechnungData;
import de.heinerion.betriebe.classes.data.TexVorlage;
import de.heinerion.betriebe.classes.file_operations.io.FileHandler;
import de.heinerion.betriebe.classes.file_operations.loading.*;
import de.heinerion.betriebe.classes.texting.Vorlage;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.enums.Pfade;
import de.heinerion.betriebe.enums.Utilities;
import de.heinerion.betriebe.exceptions.HeinerionException;
import de.heinerion.betriebe.loader.TextFileLoader;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.tools.FormatTools;
import de.heinerion.betriebe.tools.PathTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class IO implements LoadListener {
  private static final Logger logger = LogManager.getLogger(IO.class);

  private static TextFileLoader loader = new TextFileLoader();

  private static final LoadingManager loadingManager = new LoadingManager();
  private static ProgressIndicator progress;

  private String lastMessage;

  private IO() {
    loadingManager.addListener(this);

    final File companyFile = new File(PathTools.getPath(Company.class));
    final Loader<Company> companyLoader = new CompanyLoader(companyFile);
    loadingManager.addLoader(Company.class, companyLoader);

    final File file = new File(PathTools.getPath(Address.class));
    final Loader<Address> addressLoader = new AddressLoader(file);
    loadingManager.addLoader(Address.class, addressLoader);

    // TODO here we don't know even one company
    for (final Company company : Session.getAvailableCompanies()) {
      loadingManager.addLoader(RechnungData.class, new RechnungDataLoader(
          company.getPfad()));
    }
  }

  // TODO Vorlagen anders als mit zwei Listen lösen
  private static String getTemplatePath(Company company) {
    return Pfade.VORLAGEN.file(company.getDescriptiveName());
  }

  /**
   * Lädt die Namen der Vorlage LaTeX Dokumente und erstellt daraus ein
   * TexVorlagenliste
   */
  public static void ladeSpezielle() {
    final List<TexVorlage> vorlagen = new ArrayList<>();
    final File spezial = new File(Pfade.VORLAGENSPEZIAL.getPath());
    int anzahl = 0;
    final String[] specials = spezial.list();
    if (null != specials) {
      for (final String s : specials) {
        anzahl++;
        if (logger.isDebugEnabled()) {
          logger.debug("{} spezielle Vorlagen geladen", s);
        }
        vorlagen.add(new TexVorlage(s));
      }
    }

    if (logger.isInfoEnabled()) {
      logger.info(anzahl + " Vorlagen geladen");
    }

    for (final TexVorlage template : vorlagen) {
      DataBase.addTexTemplate(null, template);
    }
  }

  public static void ladeVorlagen() {
    for (final Company company : Session.getAvailableCompanies()) {
      for (final Vorlage template : ladeVorlagen(company)) {
        DataBase.addTemplate(company, template);
      }
    }
  }

  /**
   * Lädt Vorlage von der Festplatte
   *
   * @param company Der Betrieb
   * @return Die Betriebsgebundene Vorlagenliste
   */
  private static List<Vorlage> ladeVorlagen(Company company) {
    final List<Vorlage> result = FileHandler.load(new Vorlage(),
        getTemplatePath(company));
    for (Vorlage vorlage : result) {
      if (vorlage.getInhalt() == null) {
        vorlage.setInhalt(new String[0][0]);
      }
    }
    return result;
  }

  public static void saveAddresses() {
    final List<Address> addresses = DataBase.getAddresses();

    try {
      loader.saveAddresses(addresses, Utilities.SYSTEM.getPath());
    } catch (final IOException e) {
      HeinerionException.rethrow(e);
    }
  }

  /**
   * Speichert übergebene Liste
   *
   * @param liste Die zu speichernde Liste
   * @param pfad  Das Speicherziel
   */
  private static void speichereListe(List<?> liste, String pfad) {
    FileHandler.writeObject(liste, pfad);
  }

  /**
   * Schreibt die Vorlagen f&uuml;r Dienstleistungen betriebsabh&auml;ngig auf
   * die Festplatte
   *
   * @param vorlagen Rechnungsvorlagen
   * @param company  Gibt den betroffenen Betrieb an
   * @see #speichereListe(List, String)
   */
  public static void speichereVorlagen(List<Vorlage> vorlagen, Company company) {
    speichereListe(vorlagen, getTemplatePath(company));
  }

  public static void load() {
    load(progress);
  }

  public static void load(ProgressIndicator indicator) {
    loadingManager.init();
    IO.progress = indicator;
    final int number = loadingManager.getFileNumber();

    DataBase.removeAllInvoices();
    progress.setMaximum(number);
    loadingManager.load();
    DataBase.getRechnungen().getMaxNumber();

    IO.ladeVorlagen();
    IO.ladeSpezielle();

    if (progress != null) {
      progress.setString("fertig...");
    }
  }

  @Override
  public void notifyLoading(String message, Loadable loadable) {
    if (!message.equals(lastMessage)) {
      lastMessage = message;
    }

    if (progress != null) {
      final int maxValue = progress.getMaximum();
      final int oldValue = progress.getValue();
      final int newValue = oldValue + 1;
      progress.setValue(newValue);

      final double percentage = (double) newValue / maxValue;
      final String status = message + " ("
          + FormatTools.formatPercentage(percentage) + ")";
      progress.setString(status);
    }

    DataBase.addLoadable(loadable);
  }
}
