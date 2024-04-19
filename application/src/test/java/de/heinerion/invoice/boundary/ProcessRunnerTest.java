package de.heinerion.invoice.boundary;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.util.PathUtilNG;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProcessRunnerTest {

  private final static Path workingDirectory = Path.of("workingDirectory");
  private final static Path logs = Path.of("logs");

  @Mock
  private PathUtilNG pathUtil;
  @Mock
  private Session session;
  @Mock
  private HostSystem hostSystem;
  @Mock
  private ProcessBuilder builder;
  @Mock
  private Process process;

  @Test
  void quote_putsDoubleQuotes() {
    assertEquals("\"Test\"", ProcessRunner.quote("Test"));
  }

  private void mockHappyPath() throws IOException, InterruptedException {
    when(pathUtil.getWorkingDirectory()).thenReturn(workingDirectory);
    when(pathUtil.getLogPath(anyString())).thenReturn(logs);
    when(hostSystem.createFile(any(Path.class))).thenReturn(true);

    when(builder.start()).thenReturn(process);
    when(process.waitFor(anyLong(), any(TimeUnit.class))).thenReturn(true);
  }

  @Test
  void startProcess_happyPath_startsProcess() throws IOException, InterruptedException {
    mockHappyPath();

    ProcessRunner runner = new ProcessRunner(pathUtil, session, hostSystem);
    boolean returnCode = runner.startProcess(builder, null, "prog");

    verify(builder).start();
    assertTrue(returnCode);
  }


  @Test
  void startProcess_happyPath_processWaitTime() throws IOException, InterruptedException {
    mockHappyPath();

    ProcessRunner runner = new ProcessRunner(pathUtil, session, hostSystem);
    runner.startProcess(builder, null, "prog");

    verify(process).waitFor(10, TimeUnit.SECONDS);
  }

  @Test
  void startProcess_happyPath_workingDirectory() throws IOException, InterruptedException {
    mockHappyPath();

    ProcessRunner runner = new ProcessRunner(pathUtil, session, hostSystem);
    runner.startProcess(builder, null, "prog");

    verify(builder).directory(workingDirectory.toFile());
  }

  @Test
  void startProcess_happyPath_logRedirect() throws IOException, InterruptedException {
    mockHappyPath();

    ProcessRunner runner = new ProcessRunner(pathUtil, session, hostSystem);
    runner.startProcess(builder, null, "prog");

    var files = ArgumentCaptor.forClass(File.class);
    verify(builder).redirectOutput(files.capture());
    verify(builder).redirectError(files.capture());
    List<File> arguments = files.getAllValues();
    Path logFile = arguments.get(0).toPath();
    assertAll(
        () -> assertEquals(arguments.get(0), arguments.get(1), "out and err are redirected to the same file"),
        () -> assertEquals(logs, logFile.getParent(), "Logfile is created in given log path"),
        () -> assertTrue(logFile.getFileName().toString().endsWith(".log"), "Filename ends with .log")
    );
  }
}