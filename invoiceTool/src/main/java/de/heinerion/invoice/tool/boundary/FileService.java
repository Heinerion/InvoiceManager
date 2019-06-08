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

  public FileService(WriterFactory writerFactory, ProcessStarter processStarter) {
    this.writerFactory = writerFactory;
    this.processStarter = processStarter;
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
    try {
      try (Writer writer = writerFactory.create(new File(directory, baseName + ".tex"))) {
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
   * Determines the exact filename by appending <code>.tex</code> to the baseName.
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

    return true;
  }

  private void pdfLatex(File tex) {
    processStarter.start("pdflatex", '"' + tex.getAbsolutePath() + '"');
  }
}
