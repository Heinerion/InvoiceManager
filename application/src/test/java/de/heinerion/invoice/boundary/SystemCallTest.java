package de.heinerion.invoice.boundary;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SystemCallTest {
  
  @Test
  void pdfLatex_calledTwiceForPageNumbering() {
    SystemCall system = mock(SystemCall.class);
    when(system.pdfLatex(any(Path.class))).thenCallRealMethod();
    when(system.callPdfLatex(any(Path.class))).thenReturn(true);

    Path rootPath = Path.of("anything");
    boolean result = system.pdfLatex(rootPath);

    ArgumentCaptor<Path> captor = ArgumentCaptor.forClass(Path.class);
    verify(system, times(2)).callPdfLatex(captor.capture());

    List<Path> allValues = captor.getAllValues();
    assertAll("the same call is triggered twice",
        () -> assertEquals(rootPath, allValues.get(0)),
        () -> assertEquals(rootPath, allValues.get(1)),
        () -> assertTrue(result)
    );
  }

  @Test
  void pdfLatex_recoversIfPdflatexIsNotAvailable() {
    SystemCall system = mock(SystemCall.class);
    when(system.pdfLatex(any(Path.class))).thenCallRealMethod();
    when(system.callPdfLatex(any(Path.class))).thenReturn(false);

    Path rootPath = Path.of("anything");
    boolean result = system.pdfLatex(rootPath);

    ArgumentCaptor<Path> captor = ArgumentCaptor.forClass(Path.class);
    verify(system, times(1)).callPdfLatex(captor.capture());

    assertEquals(rootPath, captor.getValue());
    assertFalse(result);
  }
}