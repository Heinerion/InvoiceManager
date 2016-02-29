package de.heinerion.betriebe.classes.fileOperations.loading;

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
    if (!this.loadOrder.contains(clazz)) {
      this.loadOrder.add(clazz);
    }

    if (this.loaders.get(clazz) == null) {
      this.loaders.put(clazz, new ArrayList<>());
    }

    final List<Loader<? extends Loadable>> loadersForClass = this.loaders
        .get(clazz);
    loadersForClass.add(loader);
    loader.addListener(this);

    final int numberOfLoaders = loadersForClass.size();

    logger.info("Loader '{}' für '{}' registriert ({} gesamt)", loader
        .getClass().getSimpleName(), clazz.getSimpleName(), numberOfLoaders);
  }

  public int getFileNumber() {
    if (logger.isDebugEnabled()) {
      logger.debug("{} Dateien zu laden", this.fileNumber);
    }
    return this.fileNumber;
  }

  public List<Loadable> getResults(Class<? extends Loadable> clazz) {
    return this.results.get(clazz);
  }

  public void init() {
    this.fileNumber = 0;

    for (final List<Loader<? extends Loadable>> loaderList : this.loaders
        .values()) {
      for (final Loader<? extends Loadable> loader : loaderList) {
        loader.init();
        this.fileNumber += loader.getFileNumber();
      }
    }
  }

  public void load() {
    for (final Class<? extends Loadable> clazz : this.loadOrder) {
      this.load(clazz);
    }
  }

  /**
   * @param clazz
   */
  public void load(Class<? extends Loadable> clazz) {
    final List<Loader<? extends Loadable>> loaderList = this.loaders.get(clazz);
    for (final Loader<? extends Loadable> loader : loaderList) {
      if (loader != null) {
        final List<Loadable> result = loader.load();

        final List<Loadable> oldResults = this.results.get(clazz);
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
    for (final LoadListener listener : this.listeners) {
      listener.notifyLoading(message, loadable);
    }
  }
}
