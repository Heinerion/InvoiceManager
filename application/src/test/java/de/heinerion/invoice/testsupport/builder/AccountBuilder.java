package de.heinerion.invoice.testsupport.builder;

import de.heinerion.invoice.models.Account;

import java.util.Optional;

public class AccountBuilder implements TestDataBuilder<Account> {
  private String bic;
  private String iban;
  private String name;

  protected Optional<String> getBic() {
    return Optional.ofNullable(bic);
  }

  public AccountBuilder withBic(String bic) {
    this.bic = bic;
    return this;
  }

  protected Optional<String> getIban() {
    return Optional.ofNullable(iban);
  }

  public AccountBuilder withIban(String iban) {
    this.iban = iban;
    return this;
  }

  protected Optional<String> getName() {
    return Optional.ofNullable(name);
  }

  public AccountBuilder withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public Account build() {
    return new Account(
        getName().orElse("institute"),
        getBic().orElse("bic"),
        getIban().orElse("iban")
    );
  }
}
