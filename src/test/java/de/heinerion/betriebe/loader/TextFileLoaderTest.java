package de.heinerion.betriebe.loader;

import de.heinerion.exceptions.HeinerionException;
import de.heinerion.betriebe.models.Address;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextFileWriter.class)
@PowerMockIgnore("javax.management.*")
public class TextFileLoaderTest {

  private TextFileLoader loader;

  @Mock
  private TextFileWriter writer;

  @Before
  public void setUp() throws Exception {
    loader = new TextFileLoader();
    loader.setWriter(writer);
  }

  @Test
  public void testSaveAddresses() throws Exception {
    List<Address> addresses = new ArrayList<>();
    addresses.add(new Address("company", "street", "number", "postCode", "location"));
    loader.saveAddresses(addresses);
  }

  @Test
  public void testSaveAddressesWithException() throws Exception {
    String exceptionText = "expected Exception";
    String resultingText = "fail";
    PowerMockito.doThrow(new IOException(exceptionText)).when(writer).write(anyString(), anyString());

    List<Address> addresses = new ArrayList<>();
    addresses.add(new Address("company", "street", "number", "postCode", "location"));
    try {
      loader.saveAddresses(addresses);
    } catch(HeinerionException e) {
      resultingText =  e.getCause().getMessage();
    }

    assertEquals(exceptionText, resultingText);
  }

  @Test
  public void testSaveCompanies() throws Exception {

  }

  @Test
  public void testSaveInvoices() throws Exception {

  }

  @Test
  public void testSaveLetters() throws Exception {

  }

  @Test
  public void testSetWriter() throws Exception {

  }
}