package de.heinerion.betriebe.repositories;

import lombok.extern.flogger.Flogger;

import java.util.Collection;
import java.util.HashSet;

@Flogger
abstract class AbstractXmlRepository<T> implements Repository<T> {
  @Override
  public T save(T entry) {
    saveInMemory(entry);
    saveOnDisk();
    log.atInfo().log("save %s", entry);
    return entry;
  }

  protected abstract T saveInMemory(T entry);

  protected abstract void saveOnDisk();

  public Collection<T> saveAll(Collection<T> entries) {
    Collection<T> savedEntries = new HashSet<>();
    for (T entry : entries) {
      try {
        savedEntries.add(saveInMemory(entry));
      } catch (Exception e) {
        log.atWarning()
            .withCause(e)
            .log("Entry could not be saved: %s", entry);
      }
    }

    saveOnDisk();
    return savedEntries;
  }
}
