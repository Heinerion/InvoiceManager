package de.heinerion.betriebe.data;

import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.listener.CompanyListener;
import de.heinerion.betriebe.listener.ConveyableListener;
import de.heinerion.betriebe.listener.DateListener;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Session {
  private static final Logger LOGGER = LogManager.getLogger(Session.class);

  // TODO solche Daten per configuration.properties lösen.
  private static final String VERSION = "1.0.0-0";

  private static List<CompanyListener> companyListeners = new ArrayList<>();
  private static List<ConveyableListener> conveyableListeners = new ArrayList<>();
  private static List<DateListener> dateListeners = new ArrayList<>();

  private static List<Company> availableCompanies = new ArrayList<>();
  private static Company activeCompany = null;
  private static Address activeAddress;
  private static Conveyable activeConveyable;
  private static ApplicationFrame activeFrame;

  private static LocalDate date = LocalDate.now();

  private Session() {
  }

  public static void addAvailableCompany(Company company) {
    if (!availableCompanies.contains(company)) {
      availableCompanies.add(company);
    }
    if (availableCompanies.size() == 1) {
      setActiveCompany(company);
    }
  }

  public static void addCompanyListener(CompanyListener listener) {
    if (!companyListeners.contains(listener)) {
      companyListeners.add(listener);
    }
  }

  public static void addConveyableListener(ConveyableListener listener) {
    if (!conveyableListeners.contains(listener)) {
      conveyableListeners.add(listener);
    }
  }

  public static void addDateListener(DateListener listener) {
    if (!dateListeners.contains(listener)) {
      dateListeners.add(listener);
    }
  }

  public static Address getActiveAddress() {
    return activeAddress;
  }

  public static Company getActiveCompany() {
    return activeCompany;
  }

  public static Conveyable getActiveConveyable() {
    return activeConveyable;
  }

  public static ApplicationFrame getActiveFrame() {
    return activeFrame;
  }

  public static List<Company> getAvailableCompanies() {
    return availableCompanies;
  }

  public static Company getCompanyByName(String name) {
    // TODO I'm selecting companies by official name here but by descriptive name in the DB. So which to use??
    Company result = null;
    for (final Company c : availableCompanies) {
      if (c.getOfficialName().equals(name)) {
        result = c;
      }
    }
    return result;
  }

  public static LocalDate getDate() {
    return Session.date;
  }

  public static String getVersion() {
    return VERSION;
  }

  public static void notifyCompany() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("notifyCompany");
    }
    companyListeners.forEach(CompanyListener::notifyCompany);
  }

  public static void notifyConveyable() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("notifyConveyable");
    }
    conveyableListeners.forEach(ConveyableListener::notifyConveyable);
  }

  private static void notifyDate() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("notifyDate");
    }
    dateListeners.forEach(DateListener::notifyDate);
  }

  public static void setActiveAddress(Address theActiveAddress) {
    Session.activeAddress = theActiveAddress;
  }

  public static void setActiveCompany(Company aCompany) {
    Session.activeCompany = aCompany;
    notifyCompany();
  }

  public static void setActiveConveyable(Conveyable theActiveConveyable) {
    Session.activeConveyable = theActiveConveyable;
    notifyConveyable();
  }

  public static void setActiveFrame(ApplicationFrame aFrame) {
    Session.activeFrame = aFrame;
  }

  public static void setDate(LocalDate aDate) {
    Session.date = aDate;
    notifyDate();
  }
}
