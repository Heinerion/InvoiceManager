package de.heinerion.betriebe.models;

public final class Account {
  private String bic;
  private String iban;
  private String name;

  /**
   * For persistence only
   */
  private Account(){}

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
}
