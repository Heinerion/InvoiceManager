package de.heinerion.invoice.repositories;

import de.heinerion.invoice.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
