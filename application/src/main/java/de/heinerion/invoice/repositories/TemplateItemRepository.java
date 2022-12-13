package de.heinerion.invoice.repositories;

import de.heinerion.invoice.models.TemplateItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateItemRepository extends JpaRepository<TemplateItem, Long> {
}
