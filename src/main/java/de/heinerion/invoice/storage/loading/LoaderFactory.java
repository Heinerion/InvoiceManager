package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class LoaderFactory {
  private static Map<Class<?>, Function<File, Loader>> loaderMap;

  private LoaderFactory() {
  }

  private static Map<Class<?>, Function<File, Loader>> getLoaderMap() {
    if (loaderMap == null) {
      loaderMap = new HashMap<>();
      loaderMap.put(Address.class, AddressLoader::new);
      loaderMap.put(Company.class, CompanyLoader::new);
      loaderMap.put(ArchivedInvoice.class, ArchivedInvoiceLoader::new);
    }

    return loaderMap;
  }

  static <T extends Loadable> Optional<Function<File, Loader>> getLoaderFactory(Class<T> classToLoad) {
    return Optional.ofNullable(getLoaderMap().get(classToLoad));
  }

  static <T extends Loadable> Loader getArchivedInvoiceLoader(File directory){
    return getLoaderFactory(ArchivedInvoice.class)
        .map(loader -> loader.apply(directory))
        .orElseThrow(() -> new LoadingException(ArchivedInvoice.class));
  }
}
