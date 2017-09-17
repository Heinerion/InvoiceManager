package de.heinerion.betriebe.builder;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.gui.panels.ApplicationFrame;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;

import java.util.Optional;

public class SessionPreparer {
  private Address activeAddress;
  private Company activeCompany;
  private Letter activeConveyable;

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

  private Optional<Letter> getActiveConveyable() {
    return Optional.ofNullable(activeConveyable);
  }

  public SessionPreparer withActiveConveyable(Letter activeConveyable) {
    this.activeConveyable = activeConveyable;
    return this;
  }

  public SessionPreparer withActiveConveyable(LetterBuilder letterBuilder) {
    this.activeConveyable = letterBuilder.build();
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
    Session.setActiveAddress(getActiveAddress().orElse(new AddressBuilder().withRecipient("").build()));

    Session.setActiveCompany(getActiveCompany().orElse(new CompanyBuilder().build()));

    Session.setActiveConveyable(getActiveConveyable().orElse(
        new InvoiceBuilder()
            .withCompany(Session.getActiveCompany())
            .withReceiver(Session.getActiveAddress())
            .build()
    ));

    getApplicationFrame().ifPresent(Session::setApplicationFrame);
  }
}
