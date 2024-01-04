package de.heinerion.invoice.data;

import de.heinerion.invoice.listener.*;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.services.ConfigurationService;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import lombok.*;
import lombok.extern.flogger.Flogger;
import org.springframework.context.annotation.*;

import java.time.LocalDate;
import java.util.*;

import static de.heinerion.invoice.services.ConfigurationService.PropertyKey.REVISION;

@Flogger
@Configuration
public class Session {
  @Getter
  private final String version = ConfigurationService.get(REVISION);

  private final Set<CompanyListener> companyListeners = new HashSet<>();
  private final Set<ConveyableListener> conveyableListeners = new HashSet<>();
  private final Set<DateListener> dateListeners = new HashSet<>();
  private Company activeCompany;
  @Getter
  private Address activeAddress;
  @Getter
  private Conveyable activeConveyable;
  @Getter
  private LocalDate date = LocalDate.now();
  @Getter
  private boolean debugMode = false;
  @Getter
  @Setter
  private ApplicationFrame applicationFrame;

  Session() {
  }

  public void isDebugMode(boolean isDebugActivated) {
    debugMode = isDebugActivated;
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

  public void setActiveAddress(Address theActiveAddress) {
    activeAddress = theActiveAddress;
    if (activeConveyable != null) {
      activeConveyable.setReceiver(theActiveAddress);
    }
  }

  public Optional<Company> getActiveCompany() {
    return Optional.ofNullable(activeCompany);
  }

  public boolean isActiveCompany(Company company) {
    return company != null
        && company.equals(activeCompany);
  }

  public void setActiveCompany(Company aCompany) {
    activeCompany = aCompany;
    notifyCompany();
  }

  public void setActiveConveyable(Conveyable theActiveConveyable) {
    activeConveyable = theActiveConveyable;
    notifyConveyable();
  }

  public void setDate(LocalDate aDate) {
    date = aDate == null
        ? LocalDate.now()
        : aDate;
    notifyDate();
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

  @Bean
  public static Session getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {
    private static final Session INSTANCE = new Session();
  }
}
