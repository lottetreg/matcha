package com.github.lottetreg.matcha;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class FileHelpers {
  public static byte[] readFile(Path filePath) {
    try {
      Path currentDirectory = Path.of("/.");
      return Files.readAllBytes(currentDirectory.relativize(filePath));

    } catch (NoSuchFileException e) {
      throw new MissingFile(filePath.toString(), e);

    } catch (IOException e) {
      throw new FailedToReadFromFile(filePath.toString(), e);
    }
  }

  public static String getContentType(Path filePath) {
    try {
      return Files.probeContentType(filePath);

    } catch (IOException e) {
      throw new FailedToGetContentType(filePath.toString(), e);
    }
  }

  public static class MissingFile extends RuntimeException {
    public MissingFile(String filePath, Throwable cause) {
      super(filePath, cause);
    }
  }

  public static class FailedToReadFromFile extends RuntimeException {
    public FailedToReadFromFile(String filePath, Throwable cause) {
      super(filePath, cause);
    }
  }

  public static class FailedToGetContentType extends RuntimeException {
    public FailedToGetContentType(String filePath, Throwable cause) {
      super(filePath, cause);
    }
  }
}
