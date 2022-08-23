package de.heinerion.invoice.tool.boundary;

import java.io.File;
import java.io.IOException;

/**
 * Helps to call system commands
 */
public class ProcessStarter {

  /**
   * starts a process for calling the given system command.<br> No return values or outputs are provided, but the system
   * calls output is redirected to stdout
   * <p>
   * Arguments need to be separated. If quotes are needed, they have to be provided by the caller.
   * </p>
   * <p>
   * <b>Examples</b>
   * <ul>
   * <li>
   * <code>pdflatex "\\home\\user\\test.tex"</code><br>
   * would require<br>
   * <code>start("pdflatex", '"' + "\\home\\user\\test.tex" + '"');</code>
   * </li>
   * </ul>
   * </p>
   *
   * @param workingDirectory
   *     {@link File} which will be used as working directory for the command
   * @param command
   *     to be executed on the system
   *
   * @return <code>true</code> on success
   */
  public boolean start(File workingDirectory, String... command) {
    ProcessBuilder pb = new ProcessBuilder(command);
    pb.directory(workingDirectory);
    // map process IO to stdin / stdout
    pb.inheritIO();

    try {
      Process p = pb.start();
      p.waitFor();
      return true;
    } catch (IOException e) {
      String message = String.format("command could not be executed.\nIs %s installed?", command[0]);
      throw new RuntimeException(message, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return false;
    }
  }
}