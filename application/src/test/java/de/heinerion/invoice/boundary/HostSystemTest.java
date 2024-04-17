package de.heinerion.invoice.boundary;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HostSystemTest {

  @Mock
  SystemCall system;

  @Test
  void pdfLatex_calledTwiceForPageNumbering() {
    when(system.pdfLatex(any(Path.class))).thenReturn(true);
    HostSystem host = new HostSystem(system);

    Path rootPath = Path.of("anything");
    boolean result = host.pdfLatex(rootPath);

    ArgumentCaptor<Path> captor = ArgumentCaptor.forClass(Path.class);
    verify(system, times(2)).pdfLatex(captor.capture());

    List<Path> allValues = captor.getAllValues();
    assertAll("the same call is triggered twice",
        () -> assertEquals(rootPath, allValues.get(0)),
        () -> assertEquals(rootPath, allValues.get(1)),
        () -> assertTrue(result)
    );
  }

  @Test
  void pdfLatex_recoversIfPdflatexIsNotAvailable() {
    when(system.pdfLatex(any(Path.class))).thenReturn(false);
    HostSystem host = new HostSystem(system);

    Path rootPath = Path.of("anything");
    boolean result = host.pdfLatex(rootPath);

    ArgumentCaptor<Path> captor = ArgumentCaptor.forClass(Path.class);
    verify(system, times(1)).pdfLatex(captor.capture());

    assertEquals(rootPath, captor.getValue());
    assertFalse(result);
  }
}