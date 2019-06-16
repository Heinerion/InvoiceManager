package de.heinerion.invoice.tool.domain;

/**
 * Represents an informal letter
 */
public class Letter extends Document {
  private String text;

  public Letter(Company company) {
    super(company);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
