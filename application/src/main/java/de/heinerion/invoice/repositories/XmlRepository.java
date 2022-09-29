package de.heinerion.invoice.repositories;

import java.util.Collection;

public interface XmlRepository<T> {
  Collection<T> findAll();

  T save(T entry);

  Collection<T> saveAll(Collection<T> entries);
}