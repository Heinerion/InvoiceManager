package de.heinerion.betriebe.data;

import de.heinerion.betriebe.listener.ConveyableListener;
import de.heinerion.betriebe.listener.DateListener;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.invoice.view.swing.CompanyListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Session {
  private static final Logger LOGGER = LogManager.getLogger(Session.class);

  private static String version;

  private static List<CompanyListener> companyListeners;
  private static List<ConveyableListener> conveyableListeners;
  private static List<DateListener> dateListeners;
  private static List<Company> availableCompanies;
  private static Company activeCompany;
  private static Address activeAddress;
  private static Letter activeConveyable;

  private static LocalDate date;
  private static boolean debugMode = false;

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

  private static List<CompanyListener> getCompanyListeners() {
    if (companyListeners == null) {
      companyListeners = new ArrayList<>();
    }

    return companyListeners;
  }

  private static List<ConveyableListener> getConveyableListeners() {
    if (conveyableListeners == null) {
      conveyableListeners = new ArrayList<>();
    }

    return conveyableListeners;
  }

  private static List<DateListener> getDateListeners() {
    if (dateListeners == null) {
      dateListeners = new ArrayList<>();
    }

    return dateListeners;
  }

  static void addAvailableCompany(Company company) {
    List<Company> companies = getAvailableCompanies();

    if (!companies.contains(company)) {
      companies.add(company);
    }
    if (companies.size() == 1) {
      setActiveCompany(company);
    }
  }

  public static void addCompanyListener(CompanyListener listener) {
    addListener(listener, getCompanyListeners());
  }

  public static void addConveyableListener(ConveyableListener listener) {
    addListener(listener, getConveyableListeners());
  }

  public static void addDateListener(DateListener listener) {
    addListener(listener, getDateListeners());
  }

  private static <T> void addListener(T listener, List<T> listeners) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  public static Address getActiveAddress() {
    return activeAddress;
  }

  public static void setActiveAddress(Address theActiveAddress) {
    Session.activeAddress = theActiveAddress;
  }

  public static Company getActiveCompany() {
    return activeCompany;
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

  public static List<Company> getAvailableCompanies() {
    if (availableCompanies == null) {
      availableCompanies = new ArrayList<>();
    }

    return availableCompanies;
  }

  public static Company getCompanyByName(String name) {
    // TODO I'm selecting companies by official name here but by descriptive name in the DB. So which to use??
    Company result = null;
    for (final Company c : getAvailableCompanies()) {
      if (c.getOfficialName().equals(name)) {
        result = c;
      }
    }
    return result;
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
      version = ConfigurationService.get("git.commit.id.describe-short");
    }

    return version;
  }

  public static void notifyCompany() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("notifyCompany");
    }
    getCompanyListeners().forEach(CompanyListener::notifyCompany);
  }

  public static void notifyConveyable() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("notifyConveyable");
    }
    getConveyableListeners().forEach(ConveyableListener::notifyConveyable);
  }

  private static void notifyDate() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("notifyDate");
    }
    getDateListeners().forEach(DateListener::notifyDate);
  }
}
