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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static de.heinerion.betriebe.services.ConfigurationService.PropertyKey.REVISION;

@Flogger
public final class Session {
  private static String version;

  private static Set<CompanyListener> companyListeners;
  private static Set<ConveyableListener> conveyableListeners;
  private static Set<DateListener> dateListeners;
  private static Set<Company> availableCompanies;
  private static Company activeCompany;
  private static Address activeAddress;
  private static Letter activeConveyable;

  private static LocalDate date;
  private static boolean debugMode = false;
  private static ApplicationFrame applicationFrame;

  /**
   * Hides the default public Constructor
   */
  private Session() {
  }

  public static boolean isDebugMode() {
    return debugMode;
  }

  public static void isDebugMode(boolean isDebugActivated) {
    debugMode = isDebugActivated;
  }

  private static Set<CompanyListener> getCompanyListeners() {
    if (companyListeners == null) {
      companyListeners = new HashSet<>();
    }

    return companyListeners;
  }

  private static Set<ConveyableListener> getConveyableListeners() {
    if (conveyableListeners == null) {
      conveyableListeners = new HashSet<>();
    }

    return conveyableListeners;
  }

  private static Set<DateListener> getDateListeners() {
    if (dateListeners == null) {
      dateListeners = new HashSet<>();
    }

    return dateListeners;
  }

  public static void addAvailableCompany(Company company) {
    Set<Company> companies = getAvailableCompanies();

    companies.add(company);

    if (companies.size() == 1) {
      setActiveCompany(company);
    }
  }

  public static void addCompanyListener(CompanyListener listener) {
    getCompanyListeners().add(listener);
  }

  public static void addConveyableListener(ConveyableListener listener) {
    getConveyableListeners().add(listener);
  }

  public static void addDateListener(DateListener listener) {
    getDateListeners().add(listener);
  }

  public static Address getActiveAddress() {
    return activeAddress;
  }

  public static void setActiveAddress(Address theActiveAddress) {
    Session.activeAddress = theActiveAddress;
  }

  public static Optional<Company> getActiveCompany() {
    return Optional.ofNullable(activeCompany);
  }

  public static void setActiveCompany(Company aCompany) {
    Session.activeCompany = aCompany;
    notifyCompany();
  }

  public static Letter getActiveConveyable() {
    return activeConveyable;
  }

  public static void setActiveConveyable(Letter theActiveConveyable) {
    Session.activeConveyable = theActiveConveyable;
    notifyConveyable();
  }

  public static Set<Company> getAvailableCompanies() {
    if (availableCompanies == null) {
      availableCompanies = new HashSet<>();
    }

    return availableCompanies;
  }

  public static LocalDate getDate() {
    if (Session.date == null) {
      Session.date = LocalDate.now();
    }

    return Session.date;
  }

  public static void setDate(LocalDate aDate) {
    Session.date = aDate;
    notifyDate();
  }

  public static String getVersion() {
    if (version == null) {
      version = ConfigurationService.get(REVISION);
    }

    return version;
  }

  public static void notifyCompany() {
    log.atFine().log("notifyCompany");
    getCompanyListeners().forEach(CompanyListener::notifyCompany);
  }

  private static void notifyConveyable() {
    log.atFine().log("notifyConveyable");
    getConveyableListeners().forEach(ConveyableListener::notifyConveyable);
  }

  private static void notifyDate() {
    log.atFine().log("notifyDate");
    getDateListeners().forEach(DateListener::notifyDate);
  }

  public static void setApplicationFrame(ApplicationFrame frame) {
    applicationFrame = frame;
  }

  public static ApplicationFrame getApplicationFrame() {
    return applicationFrame;
  }
}
