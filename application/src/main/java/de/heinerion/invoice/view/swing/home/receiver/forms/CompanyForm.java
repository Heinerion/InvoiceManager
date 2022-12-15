package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.models.Company;

import java.util.*;

public class CompanyForm extends AbstractForm<Company> {

  private final List<FormLine<Company, ?>> properties = Arrays.asList(
      FormLine.of(Company.class, String.class).name("descriptiveName").setter(Company::setDescriptiveName).valid(s -> !s.isEmpty()).build(),
      FormLine.of(Company.class, String.class).name("officialName").setter(Company::setOfficialName).valid(s -> !s.isEmpty()).build(),
      FormLine.of(Company.class, String.class).name("signer").setter(Company::setSigner).valid(s -> !s.isEmpty()).build(),
      FormLine.of(Company.class, String.class).name("taxNumber").setter(Company::setTaxNumber).valid(s -> !s.isEmpty()).build(),
      FormLine.of(Company.class, String.class).name("phoneNumber").setter(Company::setPhoneNumber).valid(s -> !s.isEmpty()).build(),
      FormLine.of(Company.class, Double.class).name("valueAddedTax").setter(Company::setValueAddedTax).valid(s -> s > 0d).build(),
      FormLine.of(Company.class, Double.class).name("wagesPerHour").setter(Company::setWagesPerHour).valid(s -> s > 0d).build()

  );

  public CompanyForm(AddressForm addressForm, AccountForm accountForm) {
  }

  @Override
  protected List<FormLine<Company, ?>> getProperties() {
    return properties;
  }

  @Override
  protected String getTitle() {
    return "Company";
  }

  @Override
  protected Company createInstance() {
    return new Company();
  }
}
