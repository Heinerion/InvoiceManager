package de.heinerion.betriebe.loading;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.data.listable.TexTemplate;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.util.PathTools;
import de.heinerion.betriebe.util.PathUtil;
import de.heinerion.betriebe.view.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.betriebe.view.panels.ProgressIndicator;
import de.heinerion.util.FormatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class IOImpl implements IO, LoadListener {
  private static final Logger logger = LogManager.getLogger(IOImpl.class);
  private static final LoadingManager loadingManager = new LoadingManager();
  private static TextFileLoader fileLoader = new TextFileLoader();
  private static boolean registered;
  private ProgressIndicator progress;

  private String lastMessage;

  private IOImpl() {
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
    Session.getAvailableCompanies().forEach(IOImpl::loadAllTemplates);
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

  @Override
  public void saveAddresses() {
    final List<Address> addresses = DataBase.getAddresses();

    try {
      fileLoader.saveAddresses(addresses);
    } catch (final IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error(e);
      }

      throw new AddressesCouldNotBeSavedException(addresses, e);
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
  @Override
  public void updateTemplates(List<InvoiceTemplate> vorlagen) {
    speichereVorlagen(vorlagen, Session.getActiveCompany());
    ladeVorlagen();
  }

  @Override
  public void load() {
    load(progress);
  }

  /*
   * Attention:
   * This method is decoupled from the view, so the progress indicator could become null anytime.
   * Chances are, this special "race condition" only occur in tests, nonetheless this case has to be thought of.
   */
  @Override
  public void load(ProgressIndicator indicator) {
    loadingManager.determineFileNumbers();
    progress = indicator;
    final int number = loadingManager.getFileNumber();

    DataBase.removeAllInvoices();
    if (progress != null) {
      progress.setMaximum(number);
    }
    loadingManager.load();
    DataBase.getInvoices().determineHighestInvoiceNumbers();

    IOImpl.ladeVorlagen();
    IOImpl.ladeSpezielle();

    if (progress != null) {
      progress.setString(Translator.translate("progress.done"));
    }
  }

  @Override
  public void registerListenersAndLoaders() {
    registerToLoadingManager();

    addLoader(Address.class);
    addLoader(Company.class, this::continueWithCompany);
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

    Loader loader = LoaderFactory.getLoader(ArchivedInvoice.class, company.getFolderFile());
    loader.init();
    loader.addListener(loadingManager);
    loadingManager.loadClass(ArchivedInvoice.class, loader);
  }

  private <T extends Loadable> void addLoader(Class<T> classToLoad) {
    addLoader(classToLoad, null);
  }

  private <T extends Loadable> void addLoader(Class<T> classToLoad, LoadableCallback callback) {
    Function<File, Loader> loaderGenerator = LoaderFactory
        .getLoaderFactory(classToLoad)
        .orElseThrow(() -> new LoadingException(classToLoad));

    File loadDirectory = getLoadDirectory(classToLoad);
    Loader loader = initLoader(loaderGenerator, loadDirectory);

    loadingManager.addLoader(classToLoad, loader, callback);
  }

  private <T extends Loadable> File getLoadDirectory(Class<T> classToLoad) {
    File loadDirectory = new File(PathTools.getPath(classToLoad));
    if (logger.isDebugEnabled()) {
      logger.debug("loadDirectory {}", loadDirectory.getAbsolutePath());
    }
    return loadDirectory;
  }

  private Loader initLoader(Function<File, Loader> loaderClass, File loadDirectory) {
    Loader loader = loaderClass.apply(loadDirectory);

    if (logger.isDebugEnabled()) {
      logger.debug("fileLoader {}", loader.getDescriptiveName());
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
