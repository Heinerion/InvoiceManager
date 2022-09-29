package de.heinerion.invoice.repositories;

import jakarta.xml.bind.*;

import java.nio.file.Path;
import java.util.*;

public abstract class JaxbManager<T> {
  private boolean beautify;

  public void setBeautify(boolean beautify) {
    this.beautify = beautify;
  }

  public JaxbManager<T> withFormattedOutput() {
    setBeautify(true);
    return this;
  }

  public void marshal(List<T> items, Path destination) {
    try {
      JAXBContext context = getContext();
      Marshaller m = context.createMarshaller();

      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, beautify);

      Object wrapper = getRootObject();
      setContent(wrapper, items);

      m.marshal(wrapper, destination.toFile());
    } catch (JAXBException e) {
      throw new MarshallingException(e);
    }
  }

  public List<T> unmarshal(Path source) {
    try {
      JAXBContext context = getContext();
      Unmarshaller um = context.createUnmarshaller();

      Object rootObject = um.unmarshal(source.toFile());
      return Optional
          .ofNullable(getContent(rootObject))
          .orElse(Collections.emptyList());
    } catch (JAXBException e) {
      throw new MarshallingException(e);
    }
  }

  protected abstract Object getRootObject();

  protected abstract void setContent(Object wrapper, List<T> items);

  protected abstract List<T> getContent(Object rootObject);

  private JAXBContext getContext() throws JAXBException {
    return JAXBContext.newInstance(getWrapper());
  }

  protected abstract Class<?> getWrapper();

  private static class MarshallingException extends RuntimeException {
    MarshallingException(Throwable cause) {
      super(cause);
    }
  }
}
