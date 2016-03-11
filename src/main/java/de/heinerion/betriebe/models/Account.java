package de.heinerion.betriebe.models;

public final class Account {
  private final String bic;
  private final String iban;
  private final String name;

  public Account(String aName, String aBic, String anIban) {
    this.name = aName;
    this.bic = aBic;
    this.iban = anIban;
  }

  public String getBic() {
    return bic;
  }

  public String getIban() {
    return iban;
  }

  public String getName() {
    return name;
  }
}
