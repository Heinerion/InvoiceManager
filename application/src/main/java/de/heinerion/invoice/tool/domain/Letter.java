package de.heinerion.invoice.tool.domain;

/**
 * Represents an informal letter
 */
public class Letter extends Document {
  private String text;

  public Letter(Company company, String subject) {
    super(company, subject);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return getSubject() + " from " + getCompany() + " to " + getCustomer();
  }

  @Override
  public Document copy() {
    Letter result = new Letter(getCompany(), getSubject());
    copyDocumentPropertiesTo(result);
    result.setText(text);
    return result;
  }
}
