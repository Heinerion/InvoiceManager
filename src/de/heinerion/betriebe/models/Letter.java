package de.heinerion.betriebe.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.models.interfaces.Storable;
import de.heinerion.betriebe.tools.PathTools;
import de.heinerion.betriebe.tools.strings.Strings;

public final class Letter implements Conveyable, Storable {
  private final Company company;
  private final LocalDate date;
  private final String closing;

  private String subject;

  private final List<String> messageLines;
  private final Address receiver;

  public Letter(LocalDate date, Company sender, Address receiver) {
    this.date = date;
    this.company = sender;
    this.receiver = receiver;

    this.closing = sender.getSigner();

    this.messageLines = new ArrayList<>();
  }

  public void addMessageLine(String messageLine) {
    this.messageLines.add(messageLine);
  }

  @Override
  public String[] getClassification() {
    return new String[] { this.company.getDescriptiveName(),
        PathTools.determineFolderName(this.getClass()) };
  }

  public String getClosing() {
    return this.closing;
  }

  @Override
  public Company getCompany() {
    return this.company;
  }

  @Override
  public LocalDate getDate() {
    return this.date;
  }

  @Override
  public String getEntryName() {
    return this.getDate().toString() + " - "
        + this.getReceiver().getRecipient();
  }

  public List<String> getMessageLines() {
    return this.messageLines;
  }

  @Override
  public Address getReceiver() {
    return this.receiver;
  }

  @Override
  public String getSubject() {
    return this.subject;
  }

  @Override
  public boolean isPrintable() {
    final boolean hasSubject = !Strings.isEmpty(subject);
    final boolean hasContent = messageLines.size() > 0;
    return hasSubject && hasContent;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }
}
