package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.repositories.AddressRepository;
import lombok.extern.flogger.Flogger;

import java.util.*;

@Flogger
class LoadingManager implements LoadListener, LoadListenable {
  private final Map<Class<? extends Loadable>, List<Loader>> loaders;
  private final Map<Class<? extends Loadable>, List<Loadable>> results;
  private final Map<Class<? extends Loadable>, LoadableCallback> callbacks;
  private final List<Class<? extends Loadable>> loadOrder;
  private final List<LoadListener> listeners;

  LoadingManager() {
    this.loaders = new HashMap<>();
    this.results = new HashMap<>();
    this.listeners = new ArrayList<>();
    this.loadOrder = new ArrayList<>();
    this.callbacks = new HashMap<>();
  }

  @Override
  public void addListener(LoadListener listener) {
    this.listeners.add(listener);
  }

  <T extends Loadable> void addLoader(Class<T> clazz, Loader loader, LoadableCallback callback) {
    log.atFine().log("add " + clazz.getSimpleName());
    if (!this.loadOrder.contains(clazz)) {
      log.atFine().log("add %s to loadOrder", clazz.getSimpleName());
      this.loadOrder.add(clazz);
    }

    if (this.loaders.get(clazz) == null) {
      log.atFine().log("add empty loaderList for %s", clazz.getSimpleName());
      this.loaders.put(clazz, new ArrayList<>());
    }

    log.atFine().log("add %s to loaderList for %s", loader.getDescriptiveName(), clazz.getSimpleName());
    List<Loader> loadersForClass = loaders.get(clazz);
    loadersForClass.add(loader);
    loader.addListener(this);

    if (callback != null) {
      callbacks.put(clazz, callback);
      log.atFine().log("add callback for %s", clazz.getSimpleName());
    }

    int numberOfLoaders = loadersForClass.size();

    log.atInfo().log("loader '%s' registered for '%s' (%d in total)",
        loader.getClass().getSimpleName(),
        clazz.getSimpleName(),
        numberOfLoaders);
  }

  int countFilesToLoad() {
    int number = 0;
    for (List<Loader> loaderList : this.loaders
        .values()) {
      for (Loader loader : loaderList) {
        loader.init();
        number += loader.getFileNumber();
      }
    }

    return number;
  }

  void load(AddressRepository addressRepository) {
    loadOrder.forEach(clazz -> load(clazz, addressRepository));
  }

  private void load(Class<? extends Loadable> clazz, AddressRepository addressRepository) {
    List<Loader> loaderList = this.loaders.get(clazz);
    loaderList.stream()
        .filter(Objects::nonNull)
        .forEach(loader -> loadClass(clazz, loader, addressRepository));
  }

  void loadClass(Class<? extends Loadable> clazz, Loader loader, AddressRepository addressRepository) {
    List<Loadable> result = loader.load(addressRepository);

    List<Loadable> oldResults = this.results.get(clazz);
    if (oldResults == null) {
      this.results.put(clazz, result);
    } else {
      oldResults.addAll(result);
    }

    LoadableCallback loadableCallback = this.callbacks.get(clazz);
    if (loadableCallback != null) {
      result.forEach(loadableCallback::continueWithResult);
    }
  }

  @Override
  public void notifyLoading(String message, Loadable loadable) {
    this.notifyLoadListeners(message, loadable);
  }

  @Override
  public void notifyLoadListeners(String message, Loadable loadable) {
    for (LoadListener listener : this.listeners) {
      listener.notifyLoading(message, loadable);
    }
  }
}
