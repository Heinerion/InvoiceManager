package de.heinerion.betriebe.fileoperations;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.data.listable.TexTemplate;
import de.heinerion.betriebe.fileoperations.io.FileHandler;
import de.heinerion.betriebe.fileoperations.loading.*;
import de.heinerion.betriebe.gui.tablemodels.archive.ArchivedInvoice;
import de.heinerion.betriebe.loader.TextFileLoader;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.util.FormatUtil;
import de.heinerion.betriebe.util.PathTools;
import de.heinerion.betriebe.util.PathUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static de.heinerion.exceptions.HeinerionException.handleException;

public final class IO implements LoadListener {
  private static final Logger logger = LogManager.getLogger(IO.class);
  private static final LoadingManager loadingManager = new LoadingManager();
  private static TextFileLoader fileLoader = new TextFileLoader();
  private static ProgressIndicator progress;

  static {
    // Kick off the registrations
    new IO();
  }

  private String lastMessage;

  private IO() {
    registerListenersAndLoaders();
  }

  private static String getTemplatePath(Company company) {
    return PathUtil.getTemplateFileName(company.getDescriptiveName());
  }

  /**
   * Lädt die Namen der Vorlage LaTeX Dokumente und erstellt daraus ein
   * TexVorlagenliste
   */
  private static void ladeSpezielle() {
    List<TexTemplate> templates = new ArrayList<>();
    File special = new File(PathUtil.getTexTemplatePath());
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
      logger.info(count + " tex templates loaded");
    }

    for (TexTemplate template : templates) {
      DataBase.addTexTemplate(null, template);
    }
  }

  private static void ladeVorlagen() {
    Session.getAvailableCompanies().forEach(IO::loadAllTemplates);
  }

  private static void loadAllTemplates(Company company) {
    for (InvoiceTemplate template : ladeVorlagen(company)) {
      DataBase.addTemplate(company, template);
    }
  }

  /**
   * Lädt Vorlage von der Festplatte
   *
   * @param company Der Betrieb
   * @return Die Betriebsgebundene Vorlagenliste
   */
  private static List<InvoiceTemplate> ladeVorlagen(Company company) {
    final List<InvoiceTemplate> result = FileHandler.load(new InvoiceTemplate(),
        getTemplatePath(company));

    result.stream()
        .filter(template -> template.getInhalt() == null)
        .forEach(template -> template.setInhalt(new String[0][0]));

    return result;
  }

  public static void saveAddresses() {
    final List<Address> addresses = DataBase.getAddresses();

    try {
      fileLoader.saveAddresses(addresses);
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
  private static void speichereVorlagen(List<InvoiceTemplate> vorlagen, Company company) {
    speichereListe(vorlagen, getTemplatePath(company));
  }

  // TODO an der Effizienz arbeiten...
  public static void updateTemplates(List<InvoiceTemplate> vorlagen) {
    speichereVorlagen(vorlagen, Session.getActiveCompany());
    ladeVorlagen();
  }

  static void load() {
    load(progress);
  }

  /*
   * Attention:
   * This method is decoupled from the view, so the progress indicator could become null anytime.
   * Chances are, this special "race condition" only occur in tests, nonetheless this case has to be thought of.
   */
  public static void load(ProgressIndicator indicator) {
    loadingManager.determineFileNumbers();
    progress = indicator;
    final int number = loadingManager.getFileNumber();

    DataBase.removeAllInvoices();
    if (progress != null) {
      progress.setMaximum(number);
    }
    loadingManager.load();
    DataBase.getInvoices().determineHighestInvoiceNumbers();

    IO.ladeVorlagen();
    IO.ladeSpezielle();

    if (progress != null) {
      progress.setString(Translator.translate("progress.done"));
    }
  }

  private void registerListenersAndLoaders() {
    registerToLoadingManager();

    addLoader(Address.class, AddressLoader.class);
    addLoader(Company.class, CompanyLoader.class, this::continueWithCompany);
  }

  private void registerToLoadingManager() {
    if (logger.isDebugEnabled()) {
      logger.debug("setup Loaders");
    }
    loadingManager.addListener(this);
  }

  private void continueWithCompany(Loadable loadable) {
    Company company = (Company) loadable;

    if (logger.isDebugEnabled()) {
      logger.debug("add invoice fileLoader for {}", company.getDescriptiveName());
    }

    ArchivedInvoiceLoader loader = new ArchivedInvoiceLoader(company.getFolderFile());
    loader.init();
    loader.addListener(loadingManager);
    loadingManager.loadClass(ArchivedInvoice.class, loader);
  }

  private <T extends Loadable, X extends Loader<T>> void addLoader(Class<T> classToLoad, Class<X> loaderClass) {
    addLoader(classToLoad, loaderClass, null);
  }

  private <T extends Loadable, X extends Loader<T>> void addLoader(Class<T> classToLoad, Class<X> loaderClass,
                                                                   LoadableCallback callback) {
    if (logger.isDebugEnabled()) {
      logger.debug("add {}", loaderClass.getSimpleName());
    }

    File loadDirectory = getLoadDirectory(classToLoad);
    Loader<T> loader = initLoader(loaderClass, loadDirectory);

    loadingManager.addLoader(classToLoad, loader, callback);
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
        logger.debug("fileLoader {}", loader.getDescriptiveName());
      }
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      handleException(getClass(), e);
    }

    return loader;
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
