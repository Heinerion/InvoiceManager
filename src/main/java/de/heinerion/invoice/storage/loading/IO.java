package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IO {
  private static final Logger logger = LogManager.getLogger(IO.class);
  private static final LoadingManager loadingManager = new LoadingManager();
  private static TextFileLoader fileLoader = new TextFileLoader();
  private boolean listenersAndLoadersRegistered;

  private PathUtilNG pathUtil;

  IO(PathUtilNG pathUtil) {
    this.pathUtil = pathUtil;
  }

  private String getTemplatePath(Company company) {
    return pathUtil.getTemplateFileName(company.getDescriptiveName());
  }

  public Map<Company, List<InvoiceTemplate>> loadInvoiceTemplates() {
    return Session.getAvailableCompanies().stream()
        .collect(Collectors.toMap(company -> company, this::loadInvoiceTemplates, (oldValue, newValue) -> newValue));
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
        .filter(template -> template.getContent() == null)
        .forEach(template -> template.setContent(new String[0][0]));

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

  /*
   * Attention:
   * This method is decoupled from the view, so the progress indicator could become null anytime.
   * Chances are, this special "race condition" only occur in tests, nonetheless this case has to be thought of.
   */
  public void load(StatusComponent progress, LoadListener listener) {
    if (!listenersAndLoadersRegistered) {
      listenersAndLoadersRegistered = registerListenersAndLoaders(listener);
    }

    loadingManager.determineFileNumbers();

    setProgressMax(progress, loadingManager.getFileNumber());
    loadingManager.load();
    setProgressMessage(progress);
  }

  private boolean registerListenersAndLoaders(LoadListener listener) {
    registerListener(listener);
    registerLoaders();

    return true;
  }

  private void registerListener(LoadListener listener) {
    loadingManager.addListener(listener);
  }

  private void registerLoaders() {
    if (logger.isDebugEnabled()) {
      logger.debug("setup Loaders");
    }

    addLoader(Company.class, this::continueWithCompany);
    addLoader(Address.class, whatever -> {
    });
  }

  private void setProgressMax(StatusComponent progress, int number) {
    if (progress != null) {
      progress.setProgressMax(number);
    }
  }

  private void setProgressMessage(StatusComponent progress) {
    if (progress != null) {
      progress.setMessage(Translator.translate("progress.done"));
    }
  }

  private void continueWithCompany(Loadable loadable) {
    Company company = (Company) loadable;

    if (logger.isDebugEnabled()) {
      logger.debug("add invoice fileLoader for {}", company.getDescriptiveName());
    }

    String basePath = pathUtil.determinePath(Invoice.class);
    Loader loader = LoaderFactory.getArchivedInvoiceLoader(company.getFolderFile(basePath));
    loader.init();
    loader.addListener(loadingManager);
    loadingManager.loadClass(ArchivedInvoice.class, loader);
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
