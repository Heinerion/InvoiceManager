package de.heinerion.betriebe.fileoperations.loading;

import de.heinerion.aspects.annotations.LogMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public final class LoadingManager implements LoadListener, LoadListenable {
  private static final Logger logger = LogManager.getLogger(LoadingManager.class);
  private final Map<Class<? extends Loadable>, List<Loader<? extends Loadable>>> loaders;
  private final Map<Class<? extends Loadable>, List<Loadable>> results;
  private final Map<Class<? extends Loadable>, LoadableCallback> callbacks;
  private final List<Class<? extends Loadable>> loadOrder;
  private int fileNumber;
  private final List<LoadListener> listeners;

  public LoadingManager() {
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

  public <T extends Loadable> void addLoader(Class<T> clazz, Loader<T> loader) {
    addLoader(clazz, loader, null);
  }

  @LogMethod
  public <T extends Loadable> void addLoader(Class<T> clazz, Loader<T> loader, LoadableCallback callback) {
    if (logger.isDebugEnabled()) {
      logger.debug("add " + clazz.getSimpleName());
    }
    if (!this.loadOrder.contains(clazz)) {
      if (logger.isDebugEnabled()) {
        logger.debug("add {} to loadOrder", clazz.getSimpleName());
      }
      this.loadOrder.add(clazz);
    }

    if (this.loaders.get(clazz) == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("add empty loaderList for {}", clazz.getSimpleName());
      }
      this.loaders.put(clazz, new ArrayList<>());
    }

    if (logger.isDebugEnabled()) {
      logger.debug("add {} to loaderList for {}", loader.getDescriptiveName(), clazz.getSimpleName());
    }
    List<Loader<? extends Loadable>> loadersForClass = loaders.get(clazz);
    loadersForClass.add(loader);
    loader.addListener(this);

    if (callback != null) {
      callbacks.put(clazz, callback);
      if (logger.isDebugEnabled()) {
        logger.debug("add callback for {}", clazz.getSimpleName());
      }
    }

    int numberOfLoaders = loadersForClass.size();

    if (logger.isInfoEnabled()) {
      logger.info("Loader '{}' für '{}' registriert ({} gesamt)", loader
          .getClass().getSimpleName(), clazz.getSimpleName(), numberOfLoaders);
    }
  }

  public int getFileNumber() {
    return this.fileNumber;
  }

  public List<Loadable> getResults(Class<? extends Loadable> clazz) {
    return this.results.get(clazz);
  }

  public void determineFileNumbers() {
    resetFileNumber();

    for (List<Loader<? extends Loadable>> loaderList : this.loaders
        .values()) {
      for (Loader<? extends Loadable> loader : loaderList) {
        loader.init();
        appendToFileNumber(loader.getFileNumber());
      }
    }
  }

  private void resetFileNumber() {
    fileNumber = 0;
  }

  private void appendToFileNumber(int number) {
    fileNumber += number;
  }

  public void load() {
    loadOrder.forEach(this::load);
  }

  /**
   * @param clazz
   */
  public void load(Class<? extends Loadable> clazz) {
    List<Loader<? extends Loadable>> loaderList = this.loaders.get(clazz);
    loaderList.stream()
        .filter(Objects::nonNull)
        .forEach(loader -> loadClass(clazz, loader));
  }

  public void loadClass(Class<? extends Loadable> clazz, Loader<? extends Loadable> loader) {
    List<Loadable> result = loader.load();

    List<Loadable> oldResults = this.results.get(clazz);
    if (oldResults == null) {
      this.results.put(clazz, result);
    } else {
      oldResults.addAll(result);
    }

    LoadableCallback loadableCallback = this.callbacks.get(clazz);
    if (loadableCallback != null)
    {
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
