package de.heinerion.invoice.testsupport.builder;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Address;
import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.models.Conveyable;
import de.heinerion.invoice.view.swing.ApplicationFrame;

import java.util.Optional;

public class SessionPreparer {
  private Address activeAddress;
  private Company activeCompany;
  private Conveyable activeConveyable;

  private ApplicationFrame applicationFrame;

  private Optional<Address> getActiveAddress() {
    return Optional.ofNullable(activeAddress);
  }

  public SessionPreparer withActiveAddress(Address activeAddress) {
    this.activeAddress = activeAddress;
    return this;
  }

  private Optional<Company> getActiveCompany() {
    return Optional.ofNullable(activeCompany);
  }

  public SessionPreparer withActiveCompany(Company activeCompany) {
    this.activeCompany = activeCompany;
    return this;
  }

  private Optional<Conveyable> getActiveConveyable() {
    return Optional.ofNullable(activeConveyable);
  }

  public SessionPreparer withActiveConveyable(Conveyable activeConveyable) {
    this.activeConveyable = activeConveyable;
    return this;
  }

  public SessionPreparer withActiveConveyable(LetterBuilder letterBuilder) {
    this.activeConveyable = letterBuilder.build();
    return this;
  }

  public SessionPreparer withActiveConveyable(InvoiceBuilder invoiceBuilder) {
    this.activeConveyable = invoiceBuilder.build();
    return this;
  }

  private Optional<ApplicationFrame> getApplicationFrame() {
    return Optional.ofNullable(applicationFrame);
  }

  public SessionPreparer withApplicationFrame(ApplicationFrame applicationFrame) {
    this.applicationFrame = applicationFrame;
    return this;
  }

  public void prepare() {
    Session session = Session.getInstance();
    session.setActiveAddress(getActiveAddress().orElseGet(() -> new AddressBuilder().withName("Receiver").build()));

    session.setActiveCompany(getActiveCompany().orElseGet(() -> new CompanyBuilder().build()));

    session.setActiveConveyable(getActiveConveyable().orElseGet(() ->
        session.getActiveCompany()
            .map(company -> new InvoiceBuilder()
                .withCompany(company)
                .withReceiver(session.getActiveAddress())
                .build())
            .orElse(null)
    ));
  }
}
