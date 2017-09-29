package org.samples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(value = 1, jvmArgsAppend = {"-XX:BiasedLockingStartupDelay=0"})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
@Measurement(iterations = 5, timeUnit = TimeUnit.MILLISECONDS)
public class VectorVsArrayList {

  private static final int SIZE = 50 * 1000 * 1000;

  private ArrayList<Integer> list;
  private Vector<Integer> vector;
  private int[] sampleData;

  @Setup(Level.Invocation)
  public void setUp() throws Exception {
    list = new ArrayList<>();
    vector = new Vector<>();
    Random random = new Random();
    sampleData = new int[SIZE];
    for (int i = 0; i < SIZE; i++) {
      sampleData[i] = random.nextInt();
    }
  }

  @Benchmark
  @OperationsPerInvocation(SIZE)
  public ArrayList<Integer> list() {
    for (int sample : sampleData) {
      list.add(sample);
    }
    return list;
  }

  @Benchmark
  @OperationsPerInvocation(SIZE)
  public Vector<Integer> vector() {
    for (int sample : sampleData) {
      vector.add(sample);
    }
    return vector;
  }
}
