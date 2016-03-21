package de.heinerion.betriebe.fileoperations;

import de.heinerion.betriebe.data.RechnungData;
import de.heinerion.betriebe.data.TexTemplate;
import de.heinerion.betriebe.fileoperations.io.FileHandler;
import de.heinerion.betriebe.data.Vorlage;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.enums.Pfade;
import de.heinerion.betriebe.fileoperations.loading.*;
import de.heinerion.betriebe.loader.TextFileLoader;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.FormatUtil;
import de.heinerion.betriebe.tools.PathTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static de.heinerion.betriebe.exceptions.HeinerionException.handleException;

public final class IO implements LoadListener {
  private static final Logger logger = LogManager.getLogger(IO.class);

  private static TextFileLoader loader = new TextFileLoader();

  private static final LoadingManager loadingManager = new LoadingManager();
  private static ProgressIndicator progress;

  private String lastMessage;

  static {
    // Kick off the registrations
    new IO();
  }

  private IO() {
    registerListenersAndLoaders();
  }

  private void registerListenersAndLoaders() {
    registerToLoadingManager();

    addLoader(Company.class, CompanyLoader.class);
    addLoader(Address.class, AddressLoader.class);

    // TODO at this point, we don't know even one company
    for (Company company : Session.getAvailableCompanies()) {
      if (logger.isDebugEnabled()) {
        logger.debug("add invoice loader for {}", company.getDescriptiveName());
      }
      loadingManager.addLoader(RechnungData.class, new RechnungDataLoader(company.getPfad()));
    }
  }

  private void registerToLoadingManager() {
    if (logger.isDebugEnabled()) {
      logger.debug("setup Loaders");
    }
    loadingManager.addListener(this);
  }

  private <T extends Loadable, X extends Loader<T>> void addLoader(Class<T> classToLoad, Class<X> loaderClass) {
    if (logger.isDebugEnabled()) {
      logger.debug("add {}", loaderClass.getSimpleName());
    }

    File loadDirectory = getLoadDirectory(classToLoad);
    Loader<T> loader = initLoader(loaderClass, loadDirectory);

    loadingManager.addLoader(classToLoad, loader);
  }

  private <T extends Loadable> File getLoadDirectory(Class<T> classToLoad) {
    File loadDirectory = new File(PathTools.getPath(classToLoad));
    if (logger.isDebugEnabled()) {
      logger.debug("loadDirectory {}", loadDirectory.getAbsolutePath());
    }
    return loadDirectory;
  }

  private <T extends Loadable, X extends Loader<T>> Loader<T> initLoader(Class<X> loaderClass, File loadDirectory) {
    Loader<T> loader = null;

    try {
      Constructor<X> loaderConstructor = loaderClass.getConstructor(File.class);
      loader = loaderConstructor.newInstance(loadDirectory);
      if (logger.isDebugEnabled()) {
        logger.debug("loader {}", loader.getDescriptiveName());
      }
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      handleException(getClass(), e);
    }

    return loader;
  }

  private static String getTemplatePath(Company company) {
    return Pfade.VORLAGEN.file(company.getDescriptiveName());
  }

  /**
   * Lädt die Namen der Vorlage LaTeX Dokumente und erstellt daraus ein
   * TexVorlagenliste
   */
  public static void ladeSpezielle() {
    List<TexTemplate> templates = new ArrayList<>();
    File special = new File(Pfade.VORLAGENSPEZIAL.getPath());
    int count = 0;
    String[] specials = special.list();
    if (null != specials) {
      for (String s : specials) {
        count++;
        if (logger.isDebugEnabled()) {
          logger.debug("load special template {}", s);
        }
        templates.add(new TexTemplate(s));
      }
    }

    if (logger.isInfoEnabled()) {
      logger.info(count + " templates loaded");
    }

    for (TexTemplate template : templates) {
      DataBase.addTexTemplate(null, template);
    }
  }

  public static void ladeVorlagen() {
    for (Company company : Session.getAvailableCompanies()) {
      loadAllTemplates(company);
    }
  }

  private static void loadAllTemplates(Company company) {
    for (Vorlage template : ladeVorlagen(company)) {
      DataBase.addTemplate(company, template);
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
    for (Vorlage template : result) {
      if (template.getInhalt() == null) {
        template.setInhalt(new String[0][0]);
      }
    }
    return result;
  }

  public static void saveAddresses() {
    final List<Address> addresses = DataBase.getAddresses();

    try {
      loader.saveAddresses(addresses);
    } catch (final IOException e) {
      handleException(IO.class, e);
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

  // TODO an der Effizienz arbeiten...
  public static void updateTemplates(List<Vorlage> vorlagen) {
    speichereVorlagen(vorlagen, Session.getActiveCompany());
    ladeVorlagen();
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
    DataBase.getInvoices().getMaxNumber();

    IO.ladeVorlagen();
    IO.ladeSpezielle();

    if (progress != null) {
      progress.setString(Translator.translate("progress.done"));
    }
  }

  @Override
  public void notifyLoading(String message, Loadable loadable) {
    if (!message.equals(lastMessage)) {
      lastMessage = message;
    }

    if (progress != null) {
      int maxValue = progress.getMaximum();
      int oldValue = progress.getValue();
      int newValue = oldValue + 1;
      progress.setValue(newValue);

      double percentage = (double) newValue / maxValue;
      String status = message + " ("
          + FormatUtil.formatPercentage(percentage) + ")";
      progress.setString(status);
    }

    DataBase.addLoadable(loadable);
  }
}
