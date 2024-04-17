package de.heinerion.invoice.models;

import de.heinerion.util.Strings;
import jakarta.persistence.*;
import lombok.*;

import java.text.Collator;
import java.util.*;

@Getter
@Setter
// public because of AddressForm, AddressLoader & CompanyLoader
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "address")
public class Address implements Comparable<Address> {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private Company owner;

  private String block;
  private String name;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Transient
  private List<String> lines = new ArrayList<>();

  public static Optional<Address> parse(String address) {
    if (Strings.isBlank(address)) {
      return Optional.empty();
    }

    Address parsedAddress = new Address();
    parsedAddress.setName(address.split("\\n")[0]);
    parsedAddress.block = address;
    return Optional.of(parsedAddress);
//    TODO Adresse im Absender muss komplett aus adresse-block bestehen.
    // derzeit company name + block
  }

  private void fillLines() {
    lines.clear();
    if (!Strings.isBlank(block)) {
      lines.addAll(Arrays.asList(block.split("\\n")));
    }
  }

  public List<String> getLines() {
    if (lines.isEmpty()) {
      fillLines();
    }
    return lines;
  }

  public List<String> getLinesNonEmpty() {
    return getLines().stream()
        .filter(s -> !s.isBlank())
        .toList();
  }

  public List<String> getLinesNoNameNonEmpty() {
    return getLinesNonEmpty().stream()
        .skip(1)
        .toList();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Address address = (Address) o;
    return id != null
        && id.equals(address.id);
  }

  // TODO create domain-equals

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int compareTo(Address o) {
    return Collator.getInstance().compare(name, o.name);
  }
}
