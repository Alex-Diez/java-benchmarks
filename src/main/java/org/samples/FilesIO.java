package org.samples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(value = 3)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
@Measurement(iterations = 5, timeUnit = TimeUnit.MICROSECONDS)
public class FilesIO {

  private File exist;
  private File notExist;
  private String pathToExistedFile;
  private String pathToNotExistedFile;

  @Setup
  public void setUp() throws Exception {
    exist = new File(System.getProperty("user.dir"), "temp");
    assert exist.createNewFile();
    pathToExistedFile = exist.getAbsolutePath();
    notExist = new File("not-exist");
    pathToNotExistedFile = notExist.getAbsolutePath();
  }

  @Benchmark
  public boolean baseline() {
    return true;
  }

  @Benchmark
  public boolean not_escaped_existed() {
    return new File(pathToExistedFile).exists();
  }

  @Benchmark
  public boolean not_escaped_not_existed() {
    return new File(pathToNotExistedFile).exists();
  }

  @Benchmark
  public boolean escaped_existed() {
    return exist.exists();
  }

  @Benchmark
  public boolean escaped_not_existed() {
    return notExist.exists();
  }

  @Benchmark
  public boolean nio_existed() {
    return Files.exists(Paths.get(pathToExistedFile));
  }

  @Benchmark
  public boolean nio_not_existed() {
    return Files.exists(Paths.get(pathToNotExistedFile));
  }
}
