package de.heinerion.betriebe.models;

import de.heinerion.invoice.StringUtil;
import de.heinerion.invoice.storage.PathTools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Letter implements Storable {
  private final Company company;
  private final LocalDate date;

  String subject;

  private final List<String> messageLines;
  final Address receiver;

  public Letter(LocalDate date, Company sender, Address receiver) {
    this.date = date;
    this.company = sender;
    this.receiver = receiver;

    this.messageLines = new ArrayList<>();
  }

  public void addMessageLine(String messageLine) {
    this.messageLines.add(messageLine);
  }

  @Override
  public String[] getClassification() {
    return new String[]{this.company.getDescriptiveName(),
        PathTools.determineFolderName(this.getClass())};
  }

  public Company getCompany() {
    return this.company;
  }

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

  public Address getReceiver() {
    return this.receiver;
  }

  public String getSubject() {
    return this.subject;
  }

  public boolean isPrintable() {
    final boolean hasSubject = !StringUtil.isEmpty(subject);
    final boolean hasContent = !messageLines.isEmpty();
    return hasSubject && hasContent;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }
}
