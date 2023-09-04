package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.models.Company;

import java.util.*;

public class CompanyForm extends AbstractForm<Company> {

  private final List<FormLine<Company, ?>> properties = Arrays.asList(
      FormLine.ofString("descriptiveName", Company::setDescriptiveName),
      FormLine.ofString("officialName", Company::setOfficialName),
      FormLine.ofString("signer", Company::setSigner),
      FormLine.ofString("taxNumber", Company::setTaxNumber),
      FormLine.ofString("phoneNumber", Company::setPhoneNumber),
      FormLine.ofDouble("valueAddedTax", Company::setValueAddedTax),
      FormLine.ofDouble("wagesPerHour", Company::setWagesPerHour)
  );

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
