package de.heinerion.invoice.tool.boundary;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Handles file reading and writing
 */
public class FileService {
  private final WriterFactory writerFactory;
  private final ProcessStarter processStarter;
  private final FileSystem fileSystem;

  public FileService(WriterFactory writerFactory, ProcessStarter processStarter, FileSystem fileSystem) {
    this.writerFactory = writerFactory;
    this.processStarter = processStarter;
    this.fileSystem = fileSystem;
  }

  /**
   * Writes latex markup to a file in the given directory.
   * <p>
   * The files name is derived by the baseName, followed by <code>.tex</code>
   * </p>
   *
   * @param directory   in which the file will be created
   * @param baseName    of the file to write. Will be enhanced by <code>.tex</code>
   * @param latexMarkup the content to be written
   *
   * @return <code>true</code> on success
   */
  public boolean writeTex(String directory, String baseName, String latexMarkup) {
    File texDestination = new File(directory, baseName + ".tex");

    try {
      fileSystem.ensureFileExists(texDestination);

      try (Writer writer = writerFactory.create(texDestination)) {
        writer.write(latexMarkup);
        writer.flush();

        return true;
      }
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  /**
   * Generates a pdf from a latex source file
   * <p>
   * Determines the exact filename by appending <code>.tex</code> to the baseName.<br>
   * Deletes temp files created during the process of generating the pdf.
   * </p>
   *
   * @param directory in which the source file resides
   * @param baseName  of the source (internally extended by <code>.tex</code>)
   *
   * @return <code>true</code> on success
   */
  public boolean texToPdf(String directory, String baseName) {
    File tex = new File(directory, baseName + ".tex");

    pdfLatex(tex);
    // twice, for page numbering
    pdfLatex(tex);
    cleanup(directory, baseName);

    return true;
  }

  private void pdfLatex(File tex) {
    processStarter.start(tex.getParentFile(), "pdflatex", '"' + tex.getAbsolutePath() + '"');
  }

  private void cleanup(String directory, String baseName) {
    String[] endings = {"aux", "log", "out"};
    for (String ending : endings) {
      fileSystem.deleteFile(directory, baseName, ending);
    }
  }
}
