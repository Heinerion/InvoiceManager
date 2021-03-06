package de.heinerion.invoice.tool.boundary;

import org.easymock.Capture;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(EasyMockRunner.class)
public class FileServiceTest {

  @Mock
  private WriterFactory writerFactory;

  @Mock
  private ProcessStarter processStarter;

  @Mock
  private FileSystem fileSystem;

  private FileService service;

  @Before
  public void setUp() {
    service = new FileService(writerFactory, processStarter, fileSystem);
  }

  @Test
  public void writeTex() throws IOException {
    Writer writer = new StringWriter();
    Capture<File> fileCapture = newCapture();
    expect(writerFactory.create(capture(fileCapture))).andReturn(writer);
    replay(writerFactory);

    service.writeTex("any", "file", "tex");

    File texFile = fileCapture.getValue();
    assertNotNull(texFile);
    assertEquals("any", texFile.getParent());
    assertEquals("file.tex", texFile.getName());
    assertEquals("tex", writer.toString());
  }

  @Test
  public void texToPdf() {
    Collection<String> arguments = new ArrayList<>();

    expect(processStarter.start(isA(File.class), isA(String.class), isA(String.class))).andAnswer(() -> {
      Object[] currentArguments = getCurrentArguments();
      arguments.clear();
      arguments.add((String) currentArguments[1]);
      arguments.add((String) currentArguments[2]);
      return true;
    }).times(2);

    replay(writerFactory, processStarter);

    service.texToPdf("any", "file");

    String absolutePath = new File("any", "file.tex").getAbsolutePath();
    assertEquals(String.format("pdflatex \"%s\"", absolutePath), String.join(" ", arguments));
  }
}