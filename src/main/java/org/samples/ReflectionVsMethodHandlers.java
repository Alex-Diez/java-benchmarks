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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(value = 3)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
public class ReflectionVsMethodHandlers {

  private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

  private int[] data = new int[16];

  private Method arrayFoldingReflect;
  private MethodHandle arrayFoldingMH;

  private Method substringReflect;
  private MethodHandle substringMH;
  private MethodHandle boundSubstringMH;

  @Setup
  public void initData() throws Exception {
    Random random = new Random();
    for (int i = 0; i < data.length; i++) {
      data[i] = random.nextInt();
    }
    arrayFoldingReflect = getClass().getMethod("arrayFolding");
    substringReflect = String.class.getMethod("substring", int.class, int.class);

    MethodType intReturnType = MethodType.methodType(int.class);
    arrayFoldingMH = lookup.findVirtual(ReflectionVsMethodHandlers.class, "arrayFolding", intReturnType);


    MethodType substringType = MethodType.methodType(String.class, int.class, int.class);
    substringMH = lookup.findVirtual(String.class, "substring", substringType);
    boundSubstringMH = substringMH.bindTo("Hello, World!");
  }

  public int arrayFolding() {
    int[] data = Arrays.copyOf(this.data, this.data.length);
    int sum = 0;
    for (int i : data) {
      sum += i;
    }
    return sum;
  }

  @Benchmark
  public int arrayFoldingBaseline() {
    return arrayFolding();
  }

  @Benchmark
  public int arrayFoldingReflectCall() throws Exception {
    return (int) arrayFoldingReflect.invoke(this);
  }

  @Benchmark
  public int arrayFoldingMHCall() throws Throwable {
    return (int) arrayFoldingMH.invoke(this);
  }

  @Benchmark
  public String stringConstBaseline() {
    return "Hello";
  }

  @Benchmark
  public String substringBaseline() {
    return "Hello, World!".substring(0, 5);
  }

  @Benchmark
  public String substringReflectCall() throws Throwable {
    return (String) substringReflect.invoke("Hello, World!", 0, 5);
  }

  @Benchmark
  public String substringMHCall() throws Throwable {
    return (String) substringMH.invoke("Hello, World!", 0, 5);
  }

  @Benchmark
  public String boundSubstringMHCall() throws Throwable {
    return (String) boundSubstringMH.invoke(0, 5);
  }
}
