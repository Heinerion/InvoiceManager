package de.heinerion.betriebe.formatter;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Item;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.models.interfaces.Conveyable;

import java.time.LocalDate;

public class PlainFormatter extends AbstractFormatter {
  private static final String BRACE_CL = ")";

  @Override
  public final void formatAddress(Address address) {
    this.out(address.getRecipient());
    this.optionalOut(address.getCompany());
    this.optionalOut(address.getDistrict());
    this.out(address.getStreet() + Constants.SPACE + address.getNumber());
    this.optionalOut(address.getApartment());
    this.out(address.getPostalCode() + Constants.SPACE + address.getLocation());
  }

  @Override
  public final void formatDate(LocalDate date) {
    this.out(date.toString());
  }

  @Override
  public final void formatInvoice(Invoice invoice) {
    this.writeAddress(invoice);
    this.out("Rechnungsnummer " + invoice.getNumber());

    this.out("\nRECHNUNG");

    for (final Item item : invoice.getItems()) {
      this.out(item.getName() + " zu " + item.getPricePerUnit() + " je "
          + item.getUnit() + ".");
      this.out("\t" + item.getQuantity() + Constants.SPACE + item.getUnit()
          + " (" + item.getTotal() + Constants.SPACE + Constants.SYMBOL_EURO
          + BRACE_CL);
    }

    this.out("\nGesamtpreis NETTO");
    this.out(invoice.getNet() + Constants.SPACE + Constants.SYMBOL_EURO);
    this.out(" + " + invoice.getVat() + "% MwSt (" + invoice.getTax()
        + Constants.SPACE + Constants.SYMBOL_EURO + BRACE_CL);
    this.out("GESAMTPREIS BRUTTO ");
    this.out(invoice.getGross() + Constants.SPACE + Constants.SYMBOL_EURO);
  }

  @Override
  public final void formatLetter(Letter letter) {
    this.writeAddress(letter);

    this.out(Constants.NEWLINE + letter.getSubject() + Constants.NEWLINE);

    // out(letter.getSalutation() + ",\n");

    letter.getMessageLines().forEach(this::out);

    this.out(Constants.NEWLINE + letter.getClosing());
  }

  private void writeAddress(Conveyable conveyable) {
    this.out("--VON--");
    this.formatAddress(conveyable.getCompany().getAddress());
    this.out("--AN--");
    this.formatAddress(conveyable.getReceiver());
    this.out("--AM--");
    this.formatDate(conveyable.getDate());
  }
}
