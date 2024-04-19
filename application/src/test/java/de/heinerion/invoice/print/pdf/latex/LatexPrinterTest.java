package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.boundary.*;
import de.heinerion.invoice.models.Conveyable;
import de.heinerion.invoice.util.PathUtilNG;
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
class LatexPrinterTest {

  public static final String CONTENT = "LaTeX";
  @Mock
  private HostSystem host;
  @Mock
  private PathUtilNG pathUtil;
  @Mock
  private SystemCall systemCall;

  private final LatexGenerator generator = conveyable -> CONTENT;
  private final Path workDir = Path.of("work");
  private final Path targetDir = Path.of("target");
  private static final Path system = Path.of("system");

  private void setUpHappyPath() {
    when(pathUtil.getWorkingDirectory()).thenReturn(workDir);
    when(host.exists(any(Path.class))).thenReturn(true);
    when(systemCall.pdfLatex(any(Path.class))).thenReturn(true);
    when(pathUtil.switchToSystem(any(Path.class))).thenAnswer(i -> system);
  }

  @Test
  void writeFile_checksForExistence() {
    setUpHappyPath();

    LatexPrinter printer = new LatexPrinter(host, generator, pathUtil, systemCall);
    printer.writeFile(mock(Conveyable.class), targetDir, "title");

    ArgumentCaptor<Path> existsArgs = ArgumentCaptor.forClass(Path.class);
    verify(host, times(4)).exists(existsArgs.capture());

    List<Path> checkedPaths = existsArgs.getAllValues();
    List<Path> expected = List.of(
        targetDir.toAbsolutePath(),
        workDir
    );
    assertTrue(checkedPaths.containsAll(expected), () -> "%s contains all of %s".formatted(checkedPaths, expected));
  }

  @Test
  void writeFile_createsTexSource() {
    setUpHappyPath();

    LatexPrinter printer = new LatexPrinter(host, generator, pathUtil, systemCall);
    printer.writeFile(mock(Conveyable.class), targetDir, "title");

    ArgumentCaptor<Path> path = ArgumentCaptor.forClass(Path.class);
    ArgumentCaptor<String> content = ArgumentCaptor.forClass(String.class);

    verify(host).writeToFile(path.capture(), content.capture());

    assertEquals(workDir.resolve("title.tex"), path.getValue());
    assertEquals(CONTENT, content.getValue());
  }

  @Test
  void writeFile_returnsIfPdfLatexDoesNotExist() {
    when(pathUtil.getWorkingDirectory()).thenReturn(workDir);
    when(systemCall.pdfLatex(any(Path.class))).thenReturn(false);

    LatexPrinter printer = new LatexPrinter(host, generator, pathUtil, systemCall);
    printer.writeFile(mock(Conveyable.class), targetDir, "title");

    ArgumentCaptor<Path> argument = ArgumentCaptor.forClass(Path.class);
    verify(systemCall).pdfLatex(argument.capture());

    // as pdfLatex returns `false`, no files should be moved around or be deleted
    verify(host, times(0)).moveFile(any(Path.class), any(Path.class));
    verify(host, times(0)).deleteFile(any(Path.class));
  }

  @Test
  void writeFile_attemptsPdfLatex() {
    setUpHappyPath();

    LatexPrinter printer = new LatexPrinter(host, generator, pathUtil, systemCall);
    printer.writeFile(mock(Conveyable.class), targetDir, "title");

    ArgumentCaptor<Path> argument = ArgumentCaptor.forClass(Path.class);
    verify(systemCall).pdfLatex(argument.capture());

    assertEquals(workDir.resolve("title.tex"), argument.getValue());
  }

  @Test
  void writeFile_movesArtifacts() {
    setUpHappyPath();

    LatexPrinter printer = new LatexPrinter(host, generator, pathUtil, systemCall);
    printer.writeFile(mock(Conveyable.class), targetDir, "title");

    ArgumentCaptor<Path> source = ArgumentCaptor.forClass(Path.class);
    ArgumentCaptor<Path> target = ArgumentCaptor.forClass(Path.class);
    verify(host, times(2)).moveFile(source.capture(), target.capture());

    List<Path> sources = source.getAllValues();
    List<Path> targets = target.getAllValues();

    System.out.printf("sources: %s%n", sources);
    System.out.printf("targets: %s%n", targets);

    assertAll(
        () -> assertEquals(workDir.resolve("title.tex"), sources.get(0), "first source"),
        () -> assertEquals(system.resolve("title.tex"), targets.get(0), "first target"),

        () -> assertEquals(workDir.resolve("title.pdf"), sources.get(1), "second source"),
        () -> assertEquals(targetDir.resolve("title.pdf").toAbsolutePath(), targets.get(1), "second target")
    );
  }

  @Test
  void writeFile_deletesAuxFiles() {
    setUpHappyPath();

    LatexPrinter printer = new LatexPrinter(host, generator, pathUtil, systemCall);
    printer.writeFile(mock(Conveyable.class), targetDir, "title");

    ArgumentCaptor<Path> deleteArgs = ArgumentCaptor.forClass(Path.class);
    verify(host, times(3)).deleteFile(deleteArgs.capture());

    assertTrue(deleteArgs.getAllValues().containsAll(List.of(
        workDir.resolve("title.aux"),
        workDir.resolve("title.log"),
        workDir.resolve("title.out")
    )));
  }
}