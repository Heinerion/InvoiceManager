package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.data.DataBase;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Flogger
@Service
@RequiredArgsConstructor
public class IO {
  private static final LoadingManager loadingManager = new LoadingManager();
  private boolean listenersAndLoadersRegistered;

  private final TextFileLoader fileLoader;
  private final LoaderFactory loaderFactory;
  private final PathUtilNG pathUtil;

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
        .filter(template -> template.getInhalt() == null)
        .forEach(template -> template.setInhalt(new String[0][0]));

    return result;
  }

  public void saveCompanies(Collection<Company> companies) {
    try {
      fileLoader.saveCompanies(companies);
    } catch (final IOException e) {
      log.atSevere().withCause(e).log("could not save the companies");

      throw new RuntimeException(companies.stream()
          .map(Company::getDescriptiveName)
          .collect(Collectors.joining(", ")), e);
    }
  }

  public void saveAddresses(List<Address> addresses) {
    try {
      fileLoader.saveAddresses(addresses);
    } catch (final IOException e) {
      log.atSevere().withCause(e).log("could not save the addresses");

      throw new AddressesCouldNotBeSavedException(addresses, e);
    }
  }

  /*
   * Attention:
   * This method is decoupled from the view, so the progress indicator could become null anytime.
   * Chances are, this special "race condition" only occur in tests, nonetheless this case has to be thought of.
   */
  public void load(StatusComponent progress, LoadListener listener, DataBase dataBase) {
    if (!listenersAndLoadersRegistered) {
      listenersAndLoadersRegistered = registerListenersAndLoaders(listener, dataBase);
    }

    loadingManager.determineFileNumbers();

    setProgressMax(progress, loadingManager.getFileNumber());
    loadingManager.load(dataBase);
    setProgressMessage(progress);
  }

  private boolean registerListenersAndLoaders(LoadListener listener, DataBase dataBase) {
    registerListener(listener);
    registerLoaders(dataBase);

    return true;
  }

  private void registerListener(LoadListener listener) {
    loadingManager.addListener(listener);
  }

  private void registerLoaders(DataBase dataBase) {
    log.atFine().log("setup Loaders");

    addLoader(Company.class, c -> continueWithCompany(c, dataBase));
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

  private void continueWithCompany(Loadable loadable, DataBase dataBase) {
    Company company = (Company) loadable;

    log.atFine().log("add invoice fileLoader for %s", company.getDescriptiveName());

    String basePath = pathUtil.determinePath(Invoice.class);
    Loader loader = loaderFactory.getArchivedInvoiceLoader(company.getFolderFile(basePath));
    loader.init();
    loader.addListener(loadingManager);
    loadingManager.loadClass(ArchivedInvoice.class, loader, dataBase);
  }

  private <T extends Loadable> void addLoader(Class<T> classToLoad, LoadableCallback callback) {
    Function<File, Loader> loaderGenerator = loaderFactory
        .getLoaderFactory(classToLoad)
        .orElseThrow(() -> new LoadingException(classToLoad));

    File loadDirectory = getLoadDirectory(classToLoad);
    Loader loader = initLoader(loaderGenerator, loadDirectory);

    loadingManager.addLoader(classToLoad, loader, callback);
  }

  private <T extends Loadable> File getLoadDirectory(Class<T> classToLoad) {
    File loadDirectory = new File(PathTools.getPath(classToLoad, pathUtil));
    log.atFine().log("loadDirectory %s", loadDirectory.getAbsolutePath());
    return loadDirectory;
  }

  private Loader initLoader(Function<File, Loader> loaderClass, File loadDirectory) {
    Loader loader = loaderClass.apply(loadDirectory);

    log.atFine().log("fileLoader %s", loader.getDescriptiveName());

    return loader;
  }
}
