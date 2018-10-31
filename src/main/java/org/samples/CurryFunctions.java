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
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(value = 3)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
@Measurement(iterations = 5, timeUnit = TimeUnit.MICROSECONDS)
public class CurryFunctions {

  private MethodHandle mhAddAll;
  private MethodHandle mhAdd1;
  private MethodHandle mhAdd2;
  private MethodHandle mhAdd3;
  private MethodHandle mhAdd4;
  private MethodHandle mhAdd5;
  private MethodHandle mhAdd6;
  private MethodHandle mhAdd7;
  private Function8 lfAddAll;
  private Function7 lfAdd1;
  private Function6 lfAdd2;
  private Function5 lfAdd3;
  private Function4 lfAdd4;
  private Function3 lfAdd5;
  private Function2 lfAdd6;
  private Function1 lfAdd7;
  private Integer eight;

  @Setup
  public void setUp() throws Throwable{
    setupMH();
    setupLambda();
    eight = Integer.valueOf(8);
  }

  private void setupMH() throws NoSuchMethodException, IllegalAccessException {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodType mt = MethodType.methodType(Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class);
    mhAddAll = lookup.findStatic(CurryFunctions.class, "addStatic", mt);
    mhAdd1 = mhAddAll.bindTo(1);
    mhAdd2 = mhAdd1.bindTo(2);
    mhAdd3 = mhAdd2.bindTo(3);
    mhAdd4 = mhAdd3.bindTo(4);
    mhAdd5 = mhAdd4.bindTo(5);
    mhAdd6 = mhAdd5.bindTo(6);
    mhAdd7 = mhAdd6.bindTo(7);
  }

  private void setupLambda() {
    lfAddAll = (i0, i1, i2, i3, i4, i5, i6, i7) -> i0 + i1 + i2 + i3 + i4 + i5 + i6 + i7;
    lfAdd1 = lfAddAll.curry(1);
    lfAdd2 = lfAdd1.curry(2);
    lfAdd3 = lfAdd2.curry(3);
    lfAdd4 = lfAdd3.curry(4);
    lfAdd5 = lfAdd4.curry(5);
    lfAdd6 = lfAdd5.curry(6);
    lfAdd7 = lfAdd6.curry(7);
  }

  @Benchmark
  public Integer baseline() {
    return 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8;
  }

  @Benchmark
  public Integer mh() throws Throwable {
    return (Integer) mhAdd7.invoke(8);
  }

  @Benchmark
  public Integer lf() {
    return lfAdd7.apply(eight);
  }

  public static Integer addStatic(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4, Integer i5, Integer i6, Integer i7) {
    return i0 + i1 + i2 + i3 + i4 + i5 + i6 + i7;
  }
}

interface Function8 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4, Integer i5, Integer i6, Integer i7);

  default Function7 curry(Integer i) {
    return (i1, i2, i3, i4, i5, i6, i7) -> apply(i, i1, i2, i3, i4, i5, i6, i7);
  }
}

interface Function7 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4, Integer i5, Integer i6);

  default Function6 curry(Integer i) {
    return (i1, i2, i3, i4, i5, i6) -> apply(i, i1, i2, i3, i4, i5, i6);
  }
}

interface Function6 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4, Integer i5);

  default Function5 curry(Integer i) {
    return (i1, i2, i3, i4, i5) -> apply(i, i1, i2, i3, i4, i5);
  }
}

interface Function5 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4);

  default Function4 curry(Integer i) {
    return (i1, i2, i3, i4) -> apply(i, i1, i2, i3, i4);
  }
}

interface Function4 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3);

  default Function3 curry(Integer i) {
    return (i1, i2, i3) -> apply(i, i1, i2, i3);
  }
}

interface Function3 {
  Integer apply(Integer i0, Integer i1, Integer i2);

  default Function2 curry(Integer i) {
    return (i1, i2) -> apply(i, i1, i2);
  }
}

interface Function2 {
  Integer apply(Integer i0, Integer i1);

  default Function1 curry(Integer i) {
    return (i1) -> apply(i, i1);
  }
}

interface Function1 {
  Integer apply(Integer i0);
}
