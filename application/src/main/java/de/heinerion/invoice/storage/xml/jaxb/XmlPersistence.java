package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.contract.Contract;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class XmlPersistence {
  public List<Address> readAddresses(File source) {
    if (source.exists()) {
      return new AddressManager().unmarshal(source);
    }

    return Collections.emptyList();
  }

  public void writeAddresses(File destination, List<Address> addresses) {
    Contract.require(destination.getName().endsWith(".xml"), "destination ends with '.xml'");
    new AddressManager()
        .withFormattedOutput()
        .marshal(addresses, destination);
  }

  public List<Company> readCompanies(File source) {
    if (source.exists()) {
      return new CompanyManager().unmarshal(source);
    }

    return Collections.emptyList();
  }
  
  public void writeCompanies(File destination, List<Company> companies) {
    Contract.require(destination.getName().endsWith(".xml"), "destination ends with '.xml'");
    new CompanyManager()
        .withFormattedOutput()
        .marshal(companies, destination);
  }
}
