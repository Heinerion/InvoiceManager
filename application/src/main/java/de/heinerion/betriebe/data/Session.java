package de.heinerion.betriebe.data;

import de.heinerion.betriebe.listener.CompanyListener;
import de.heinerion.betriebe.listener.ConveyableListener;
import de.heinerion.betriebe.listener.DateListener;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import lombok.extern.flogger.Flogger;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static de.heinerion.betriebe.services.ConfigurationService.PropertyKey.REVISION;

@Flogger
public final class Session {
  private final String version = ConfigurationService.get(REVISION);

  private final Set<CompanyListener> companyListeners = new HashSet<>();
  private final Set<ConveyableListener> conveyableListeners = new HashSet<>();
  private final Set<DateListener> dateListeners = new HashSet<>();
  private final Set<Company> availableCompanies = new HashSet<>();
  private Company activeCompany;
  private Address activeAddress;
  private Letter activeConveyable;

  private LocalDate date = LocalDate.now();
  private boolean debugMode = false;
  private ApplicationFrame applicationFrame;

  /**
   * Hides the default public Constructor
   */
  private Session() {
  }

  public boolean isDebugMode() {
    return debugMode;
  }

  public void isDebugMode(boolean isDebugActivated) {
    debugMode = isDebugActivated;
  }

  public void addAvailableCompany(Company company) {
    availableCompanies.add(company);

    if (availableCompanies.size() == 1) {
      setActiveCompany(company);
    }
  }

  public void addCompanyListener(CompanyListener listener) {
    companyListeners.add(listener);
  }

  public void addConveyableListener(ConveyableListener listener) {
    conveyableListeners.add(listener);
  }

  public void addDateListener(DateListener listener) {
    dateListeners.add(listener);
  }

  public Address getActiveAddress() {
    return activeAddress;
  }

  public void setActiveAddress(Address theActiveAddress) {
    activeAddress = theActiveAddress;
    if (activeConveyable != null) {
      activeConveyable.setReceiver(theActiveAddress);
    }
  }

  public Optional<Company> getActiveCompany() {
    return Optional.ofNullable(activeCompany);
  }

  public void setActiveCompany(Company aCompany) {
    activeCompany = aCompany;
    notifyCompany();
  }

  public Letter getActiveConveyable() {
    return activeConveyable;
  }

  public void setActiveConveyable(Letter theActiveConveyable) {
    activeConveyable = theActiveConveyable;
    notifyConveyable();
  }

  public Set<Company> getAvailableCompanies() {
    return Collections.unmodifiableSet(availableCompanies);
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate aDate) {
    date = aDate == null
        ? LocalDate.now()
        : aDate;
    notifyDate();
  }

  public String getVersion() {
    return version;
  }

  public void notifyCompany() {
    log.atFine().log("notifyCompany %s", getActiveCompany().map(String::valueOf).orElse("-none-"));
    companyListeners.forEach(CompanyListener::notifyCompany);
  }

  private void notifyConveyable() {
    log.atFine().log("notifyConveyable %s", getActiveConveyable());
    conveyableListeners.forEach(ConveyableListener::notifyConveyable);
  }

  private void notifyDate() {
    log.atFine().log("notifyDate");
    dateListeners.forEach(DateListener::notifyDate);
  }

  public void setApplicationFrame(ApplicationFrame frame) {
    applicationFrame = frame;
  }

  public ApplicationFrame getApplicationFrame() {
    return applicationFrame;
  }

  public static Session getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {
    private static final Session INSTANCE = new Session();
  }
}
