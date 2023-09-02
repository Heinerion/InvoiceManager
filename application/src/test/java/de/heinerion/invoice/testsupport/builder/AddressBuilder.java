package de.heinerion.invoice.testsupport.builder;

import de.heinerion.invoice.models.Address;
import lombok.Getter;

@Getter
public class AddressBuilder implements TestDataBuilder<Address> {
  private String name;
  private String block;

  public AddressBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public AddressBuilder withBlock(String block) {
    this.block = block;
    return this;
  }

  @Override
  public Address build() {
    return new Address()
        .setName(getName())
        .setBlock(getBlock());
  }
}
