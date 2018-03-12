package de.heinerion.invoice.view.formatter;

class FormatterException extends RuntimeException {
  FormatterException() {
    super("formatter is not configured properly");
  }
}
