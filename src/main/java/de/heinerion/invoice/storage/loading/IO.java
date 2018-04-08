package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.data.listable.TexTemplate;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.storage.PathTools;
import de.heinerion.invoice.view.common.StatusComponent;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IO  {
  private static final Logger logger = LogManager.getLogger(IO.class);
  private static final LoadingManager loadingManager = new LoadingManager();
  private static Serializer fileLoader = new TextFileLoader();
  private boolean registered;

  private PathUtilNG pathUtil;

  @Autowired
  IO(PathUtilNG pathUtil) {
    this.pathUtil = pathUtil;
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

  private String getTemplatePath(Company company) {
    return pathUtil.getTemplateFileName(company.getDescriptiveName());
  }

  /**
   * Lädt die Namen der Vorlage LaTeX Dokumente und erstellt daraus ein
   * TexVorlagenliste
   */
  public List<TexTemplate> loadTexTemplates() {
    List<TexTemplate> templates = new ArrayList<>();
    File special = new File(pathUtil.getTexTemplatePath());
    int count = 0;
    String[] specials = special.list();
    if (null != specials) {
      for (String s : specials) {
        count++;
        if (logger.isDebugEnabled()) {
          logger.debug("load special template {}", s);
        }
        templates.add(new TexTemplate(pathUtil.getTexTemplatePath(), s));
      }
    }

    if (logger.isInfoEnabled()) {
      logger.info(count + " tex templates loaded");
    }

    return templates;
  }

  public Map<Company, List<InvoiceTemplate>> loadInvoiceTemplates() {
    Map<Company, List<InvoiceTemplate>> companyToTemplates = new HashMap<>();

    List<Company> companies = Session.getAvailableCompanies();
    for (Company company : companies) {
      companyToTemplates.put(company, loadInvoiceTemplates(company));
    }

    return companyToTemplates;
  }

  /**
   * Lädt Vorlage von der Festplatte
   *
   * @param company Der Betrieb
   * @return Die Betriebsgebundene Vorlagenliste
   */
  private List<InvoiceTemplate> loadInvoiceTemplates(Company company) {
    final List<InvoiceTemplate> result = FileHandler.load(new InvoiceTemplate(),
        getTemplatePath(company));

    result.stream()
        .filter(template -> template.getInhalt() == null)
        .forEach(template -> template.setInhalt(new String[0][0]));

    return result;
  }

  public void saveAddresses(List<Address> addresses) {
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
   * Schreibt die Vorlagen f&uuml;r Dienstleistungen betriebsabh&auml;ngig auf
   * die Festplatte
   *
   * @param vorlagen Rechnungsvorlagen
   * @param company  Gibt den betroffenen Betrieb an
   * @see #speichereListe(List, String)
   */
  public void saveInvoiceTemplates(List<InvoiceTemplate> vorlagen, Company company) {
    speichereListe(vorlagen, getTemplatePath(company));
  }

  /*
   * Attention:
   * This method is decoupled from the view, so the progress indicator could become null anytime.
   * Chances are, this special "race condition" only occur in tests, nonetheless this case has to be thought of.
   */
  public void load(StatusComponent<?> progress, LoadListener listener) {
    if (!registered) {
      registerListenersAndLoaders(listener);
      registered = true;
    }

    loadingManager.determineFileNumbers();
    final int number = loadingManager.getFileNumber();

    if (progress != null) {
      progress.setProgressMax(number);
    }
    loadingManager.load();

    if (progress != null) {
      progress.setMessage(Translator.translate("progress.done"));
    }
  }

  private void registerListenersAndLoaders(LoadListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("setup Loaders");
    }
    loadingManager.addListener(listener);

    addLoader(Company.class, this::continueWithCompany);
    addLoader(Address.class);
  }

  private void continueWithCompany(Loadable loadable) {
    Company company = (Company) loadable;

    if (logger.isDebugEnabled()) {
      logger.debug("add invoice fileLoader for {}", company.getDescriptiveName());
    }

    String basePath = pathUtil.determinePath(Invoice.class);
    Loader loader = LoaderFactory.getLoader(ArchivedInvoice.class, company.getFolderFile(basePath));
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
}
