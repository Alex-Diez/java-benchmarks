package org.samples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 10)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class LinkedListGc {

  private LinkedList<Integer> parallel;
  private LinkedList<Integer> g1gc;
  private int[] sampleData;

  @Param({""+1024, ""+16*1024, ""+1024*1024, ""+16*1024*1024})
  private int size;

  @Setup(Level.Invocation)
  public void setUp() {
    parallel = new LinkedList<>();
    g1gc = new LinkedList<>();
    Random random = new Random();
    sampleData = new int[size];
    for (int i = 0; i < size; i++) {
      sampleData[i] = random.nextInt();
    }
  }

  @Benchmark
  @Fork(value = 1, jvmArgs = "-XX:+UseParallelGC")
  public int parallel() {
    for (int item : sampleData) {
      parallel.add(item);
    }
    int sum = 0;
    Integer i;
    while ((i = parallel.poll()) != null) {
      sum += i;
    }
    return sum;
  }

  @Benchmark
  @Fork(1)
  public int g1gc() {
    for (int item : sampleData) {
      g1gc.add(item);
    }
    int sum = 0;
    Integer i;
    while ((i = g1gc.poll()) != null) {
      sum += i;
    }
    return sum;
  }
}
