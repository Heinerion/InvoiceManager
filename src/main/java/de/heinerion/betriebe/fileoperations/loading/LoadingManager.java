package de.heinerion.betriebe.fileoperations.loading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LoadingManager implements LoadListener, LoadListenable {
  // TODO Priorität: Dokumente laden (alte Rechnungen)
  private static final Logger logger = LogManager.getLogger(LoadingManager.class);
  private final Map<Class<? extends Loadable>, List<Loader<? extends Loadable>>> loaders;
  private final Map<Class<? extends Loadable>, List<Loadable>> results;
  private final List<Class<? extends Loadable>> loadOrder;
  private int fileNumber;
  private final List<LoadListener> listeners;

  public LoadingManager() {
    this.loaders = new HashMap<>();
    this.results = new HashMap<>();
    this.listeners = new ArrayList<>();
    this.loadOrder = new ArrayList<>();
  }

  @Override
  public void addListener(LoadListener listener) {
    this.listeners.add(listener);
  }

  public void addLoader(Class<? extends Loadable> clazz,
                        Loader<? extends Loadable> loader) {
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

  public void init() {
    this.fileNumber = 0;

    for (List<Loader<? extends Loadable>> loaderList : this.loaders
        .values()) {
      for (Loader<? extends Loadable> loader : loaderList) {
        loader.init();
        this.fileNumber += loader.getFileNumber();
      }
    }
  }

  public void load() {
    for (Class<? extends Loadable> clazz : this.loadOrder) {
      this.load(clazz);
    }
  }

  /**
   * @param clazz
   */
  public void load(Class<? extends Loadable> clazz) {
    List<Loader<? extends Loadable>> loaderList = this.loaders.get(clazz);
    for (Loader<? extends Loadable> loader : loaderList) {
      if (loader != null) {
        List<Loadable> result = loader.load();

        List<Loadable> oldResults = this.results.get(clazz);
        if (oldResults == null) {
          this.results.put(clazz, result);
        } else {
          oldResults.addAll(result);
        }
      }
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
