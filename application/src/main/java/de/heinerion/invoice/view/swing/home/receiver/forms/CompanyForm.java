package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.models.Company;
import de.heinerion.util.*;

import java.util.*;

import static de.heinerion.invoice.view.swing.home.receiver.forms.ComponentFactory.*;

public class CompanyForm extends AbstractForm<Company> {

  private final List<FormLine<Company, ?>> properties = Arrays.asList(
      FormLine.of("descriptiveName", String.class, Company::setDescriptiveName, Strings::isNotBlank, createStringComponent()),
      FormLine.of("officialName", String.class, Company::setOfficialName, Strings::isNotBlank, createStringComponent()),
      FormLine.of("signer", String.class, Company::setSigner, Strings::isNotBlank, createStringComponent()),
      FormLine.of("taxNumber", String.class, Company::setTaxNumber, Strings::isNotBlank, createStringComponent()),
      FormLine.of("phoneNumber", String.class, Company::setPhoneNumber, Strings::isNotBlank, createStringComponent()),
      FormLine.of("valueAddedTax", Double.class, Company::setValueAddedTax, Doubles::isGreaterZero, createDoubleComponent()),
      FormLine.of("wagesPerHour", Double.class, Company::setWagesPerHour, Doubles::isGreaterZero, createDoubleComponent())

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
