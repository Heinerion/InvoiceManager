package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "template_item")
public class TemplateItem {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "template_id")
  private InvoiceTemplate template;

  private Integer position;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private Double quantity;

  public Optional<Item> asItem() {
    if (product == null) {
      return Optional.empty();
    }

    return Optional.of(
        Item.of(position,
            product.getName(),
            product.getUnit(),
            product.getPricePerUnit(),
            quantity));
  }
}
