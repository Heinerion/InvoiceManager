package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
// public for AccountForm
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "account")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String bic;
  private String iban;
  private String name;

  public Account(String aName, String aBic, String anIban) {
    this.name = aName;
    this.bic = aBic;
    this.iban = anIban;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(bic, account.bic) &&
        Objects.equals(iban, account.iban) &&
        Objects.equals(name, account.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bic, iban, name);
  }

  @Override
  public String toString() {
    return name;
  }
}
