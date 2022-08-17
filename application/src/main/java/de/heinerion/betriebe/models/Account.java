package de.heinerion.betriebe.models;

import java.util.Objects;

public final class Account {
  private String bic;
  private String iban;
  private String name;

  /**
   * For persistence only
   */
  public Account() {
  }

  public Account(String aName, String aBic, String anIban) {
    this.name = aName;
    this.bic = aBic;
    this.iban = anIban;
  }

  public String getBic() {
    return bic;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
