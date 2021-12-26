package de.heinerion.invoice.storage.xml.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

abstract class JaxbManager<T> {
  private boolean beautify;

  public void setBeautify(boolean beautify) {
    this.beautify = beautify;
  }

  public JaxbManager<T> withFormattedOutput() {
    setBeautify(true);
    return this;
  }

  public void marshal(List<T> items, File destination) {
    try {
      JAXBContext context = getContext();
      Marshaller m = context.createMarshaller();

      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, beautify);

      Object wrapper = getRootObject();
      setContent(wrapper, items);

      m.marshal(wrapper, destination);
    } catch (JAXBException e) {
      throw new MarshallingException(e);
    }
  }

  public List<T> unmarshal(File source) {
    try {
      JAXBContext context = getContext();
      Unmarshaller um = context.createUnmarshaller();

      Object rootObject = um.unmarshal(source);
      return getContent(rootObject);
    } catch (JAXBException e) {
      throw new MarshallingException(e);
    }
  }

  protected abstract Object getRootObject();

  protected abstract void setContent(Object wrapper, List<T> items);

  protected abstract List<T> getContent(Object rootObject);

  protected abstract JAXBContext getContext() throws JAXBException;

  private static class MarshallingException extends RuntimeException {
    MarshallingException(Throwable cause) {
      super(cause);
    }
  }
}
