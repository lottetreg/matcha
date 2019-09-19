package com.github.lottetreg.matcha;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class FileHelpersTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  private Path pathFor(String fileName) {
    String supportDirectoryPath = "/src/test/java/com/github/lottetreg/matcha/support";
    return Path.of(supportDirectoryPath + fileName);
  }

  @Test
  public void readFileReadsAFileInTheCurrentDirectoryIntoAByteArray() {
    Path filePath = pathFor("/index.html");

    byte[] fileContents = FileHelpers.readFile(filePath);

    assertEquals("<h1>Hello, World!</h1>\n", new String(fileContents));
  }

  @Test
  public void readFileThrowsAnExceptionIfTheFileIsMissing() {
    Path filePath = pathFor("/missing.html");

    exceptionRule.expect(FileHelpers.MissingFile.class);
    exceptionRule.expectMessage("/missing.html");

    FileHelpers.readFile(filePath);
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAPlainTextLFile() {
    assertEquals("text/plain", FileHelpers.getContentType(Path.of("/index.txt")));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAnHTMLFile() {
    assertEquals("text/html", FileHelpers.getContentType(Path.of("/index.html")));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAJPEGFile() {
    assertEquals("image/jpeg", FileHelpers.getContentType(Path.of("/pickles.jpg")));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAPNGFile() {
    assertEquals("image/png", FileHelpers.getContentType(Path.of("/pickles.png")));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAGIFFile() {
    assertEquals("image/gif", FileHelpers.getContentType(Path.of("/pickles.gif")));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAnMP3File() {
    assertEquals("audio/mpeg", FileHelpers.getContentType(Path.of("/pickles.mp3")));
  }

  @Test
  public void getContentTypeGetsTheContentTypeOfAnMP4File() {
    assertEquals("video/mp4", FileHelpers.getContentType(Path.of("/pickles.mp4")));
  }
}
