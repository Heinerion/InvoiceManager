package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.data.DataBase;
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
import java.util.List;
import java.util.function.Function;

@Flogger
@Service
@RequiredArgsConstructor
public class XmlLoader {
  private static final LoadingManager loadingManager = new LoadingManager();
  private boolean listenersAndLoadersRegistered;

  private final LoaderFactory loaderFactory;
  private final PathUtilNG pathUtil;

  /*
   * Attention:
   * This method is decoupled from the view, so the progress indicator could become null anytime.
   * Chances are, this special "race condition" only occur in tests, nonetheless this case has to be thought of.
   */
  public void load(StatusComponent progress, LoadListener listener, DataBase dataBase) {
    if (!listenersAndLoadersRegistered) {
      loadingManager.addListener(listener);
      log.atFine().log("setup Loaders");
      addLoader(Company.class, c -> continueWithCompany(c, dataBase));
      addLoader(Address.class, nothingToDoAfterwards -> {
      });

      listenersAndLoadersRegistered = true;
    }

    int filesToLoad = loadingManager.countFilesToLoad();
    if (progress != null) {
      progress.setProgressMax(filesToLoad);
    }
    loadingManager.load(dataBase);
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

    log.atFine().log("load invoice templates for %s", company.getDescriptiveName());
    dataBase.addTemplates(company, loadInvoiceTemplates(company));
  }

  /**
   * Lädt Vorlage von der Festplatte
   *
   * @param company Der Betrieb
   * @return Die Betriebsgebundene Vorlagenliste
   */
  private List<InvoiceTemplate> loadInvoiceTemplates(Company company) {
    final List<InvoiceTemplate> result = FileHandler.load(new InvoiceTemplate(),
        pathUtil.getTemplateFileName(company.getDescriptiveName()));

    result.stream()
        .filter(template -> template.getInhalt() == null)
        .forEach(template -> template.setInhalt(new String[0][0]));

    return result;
  }

  private <T extends Loadable> void addLoader(Class<T> classToLoad, LoadableCallback callback) {
    Function<File, Loader> loaderGenerator = loaderFactory
        .getLoaderFactory(classToLoad)
        .orElseThrow(() -> new LoadingException(classToLoad));

    File loadDirectory = new File(PathTools.getPath(classToLoad, pathUtil));
    log.atFine().log("loadDirectory %s", loadDirectory.getAbsolutePath());
    Loader loader = loaderGenerator.apply(loadDirectory);
    log.atFine().log("fileLoader %s", loader.getDescriptiveName());
    loadingManager.addLoader(classToLoad, loader, callback);
  }

}
