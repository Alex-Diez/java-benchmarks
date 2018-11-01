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

//Benchmark                      Mode  Cnt  Score    Error  Units
//CurryFunctions.baseline        avgt   15  0.003 ±  0.001  us/op
//CurryFunctions.boxedLf1        avgt   15  0.019 ±  0.001  us/op
//CurryFunctions.boxedLf2        avgt   15  0.017 ±  0.001  us/op
//CurryFunctions.boxedLf4        avgt   15  0.015 ±  0.001  us/op
//CurryFunctions.boxedLf8        avgt   15  0.005 ±  0.001  us/op
//CurryFunctions.boxedStaticLf1  avgt   15  0.018 ±  0.001  us/op
//CurryFunctions.boxedStaticLf2  avgt   15  0.017 ±  0.001  us/op
//CurryFunctions.boxedStaticLf4  avgt   15  0.015 ±  0.001  us/op
//CurryFunctions.boxedStaticLf8  avgt   15  0.005 ±  0.001  us/op
//CurryFunctions.lf1             avgt   15  0.017 ±  0.001  us/op
//CurryFunctions.lf2             avgt   15  0.015 ±  0.001  us/op
//CurryFunctions.lf4             avgt   15  0.011 ±  0.001  us/op
//CurryFunctions.lf8             avgt   15  0.003 ±  0.001  us/op
//CurryFunctions.mh1             avgt   15  0.010 ±  0.003  us/op
//CurryFunctions.mh2             avgt   15  0.009 ±  0.001  us/op
//CurryFunctions.mh4             avgt   15  0.010 ±  0.001  us/op
//CurryFunctions.mh8             avgt   15  0.014 ±  0.001  us/op
//CurryFunctions.staticLf1       avgt   15  0.015 ±  0.001  us/op
//CurryFunctions.staticLf2       avgt   15  0.014 ±  0.001  us/op
//CurryFunctions.staticLf4       avgt   15  0.011 ±  0.001  us/op
//CurryFunctions.staticLf8       avgt   15  0.003 ±  0.001  us/op
@State(Scope.Benchmark)
@Fork(value = 3)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
public class CurryFunctions {

  private MethodHandle mhAddAll;
  private MethodHandle mhAdd1;
  private MethodHandle mhAdd2;
  private MethodHandle mhAdd3;
  private MethodHandle mhAdd4;
  private MethodHandle mhAdd5;
  private MethodHandle mhAdd6;
  private MethodHandle mhAdd7;
  private BoxedFunction8 boxedLfAddAll;
  private BoxedFunction7 boxedLfAdd1;
  private BoxedFunction6 boxedLfAdd2;
  private BoxedFunction5 boxedLfAdd3;
  private BoxedFunction4 boxedLfAdd4;
  private BoxedFunction3 boxedLfAdd5;
  private BoxedFunction2 boxedLfAdd6;
  private BoxedFunction1 boxedLfAdd7;
  private Function8 lfAddAll;
  private Function7 lfAdd1;
  private Function6 lfAdd2;
  private Function5 lfAdd3;
  private Function4 lfAdd4;
  private Function3 lfAdd5;
  private Function2 lfAdd6;
  private Function1 lfAdd7;
  private static BoxedFunction8 boxedStaticLfAddAll = (i0, i1, i2, i3, i4, i5, i6, i7) -> i0 + i1 + i2 + i3 + i4 + i5 + i6 + i7;
  private static BoxedFunction7 boxedStaticLfAdd1 = boxedStaticLfAddAll.curry(1);
  private static BoxedFunction6 boxedStaticLfAdd2 = boxedStaticLfAdd1.curry(2);
  private static BoxedFunction5 boxedStaticLfAdd3 = boxedStaticLfAdd2.curry(3);
  private static BoxedFunction4 boxedStaticLfAdd4 = boxedStaticLfAdd3.curry(4);
  private static BoxedFunction3 boxedStaticLfAdd5 = boxedStaticLfAdd4.curry(5);
  private static BoxedFunction2 boxedStaticLfAdd6 = boxedStaticLfAdd5.curry(6);
  private static BoxedFunction1 boxedStaticLfAdd7 = boxedStaticLfAdd6.curry(7);
  private static Function8 staticLfAddAll = (i0, i1, i2, i3, i4, i5, i6, i7) -> i0 + i1 + i2 + i3 + i4 + i5 + i6 + i7;
  private static Function7 staticLfAdd1 = staticLfAddAll.curry(1);
  private static Function6 staticLfAdd2 = staticLfAdd1.curry(2);
  private static Function5 staticLfAdd3 = staticLfAdd2.curry(3);
  private static Function4 staticLfAdd4 = staticLfAdd3.curry(4);
  private static Function3 staticLfAdd5 = staticLfAdd4.curry(5);
  private static Function2 staticLfAdd6 = staticLfAdd5.curry(6);
  private static Function1 staticLfAdd7 = staticLfAdd6.curry(7);

  @Setup
  public void setUp() throws Throwable {
    setupMH();
    setupBoxedLambda();
    setupLambda();
  }

  private void setupMH() throws NoSuchMethodException, IllegalAccessException {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodType mt = MethodType.methodType(
        Integer.class,
        Integer.class,
        Integer.class,
        Integer.class,
        Integer.class,
        Integer.class,
        Integer.class,
        Integer.class,
        Integer.class
    );
    mhAddAll = lookup.findStatic(CurryFunctions.class, "addStatic", mt);
    mhAdd1 = mhAddAll.bindTo(1);
    mhAdd2 = mhAdd1.bindTo(2);
    mhAdd3 = mhAdd2.bindTo(3);
    mhAdd4 = mhAdd3.bindTo(4);
    mhAdd5 = mhAdd4.bindTo(5);
    mhAdd6 = mhAdd5.bindTo(6);
    mhAdd7 = mhAdd6.bindTo(7);
  }

  private void setupBoxedLambda() {
    boxedLfAddAll = (i0, i1, i2, i3, i4, i5, i6, i7) -> i0 + i1 + i2 + i3 + i4 + i5 + i6 + i7;
    boxedLfAdd1 = boxedLfAddAll.curry(1);
    boxedLfAdd2 = boxedLfAdd1.curry(2);
    boxedLfAdd3 = boxedLfAdd2.curry(3);
    boxedLfAdd4 = boxedLfAdd3.curry(4);
    boxedLfAdd5 = boxedLfAdd4.curry(5);
    boxedLfAdd6 = boxedLfAdd5.curry(6);
    boxedLfAdd7 = boxedLfAdd6.curry(7);
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
  public Integer mh1() throws Throwable {
    return (Integer) mhAdd7.invoke(8);
  }

  @Benchmark
  public Integer mh2() throws Throwable {
    return (Integer) mhAdd6.invoke(7, 8);
  }

  @Benchmark
  public Integer mh4() throws Throwable {
    return (Integer) mhAdd4.invoke(5, 6, 7, 8);
  }

  @Benchmark
  public Integer mh8() throws Throwable {
    return (Integer) mhAddAll.invoke(1, 2, 3, 4, 5, 6, 7, 8);
  }

  @Benchmark
  public int lf1() {
    return lfAdd7.apply(8);
  }

  @Benchmark
  public int lf2() {
    return lfAdd6.apply(7, 8);
  }

  @Benchmark
  public int lf4() {
    return lfAdd4.apply(5, 6, 7, 8);
  }

  @Benchmark
  public int lf8() {
    return lfAddAll.apply(1, 2, 3, 4, 5, 6, 7, 8);
  }

  @Benchmark
  public int staticLf1() {
    return staticLfAdd7.apply(8);
  }

  @Benchmark
  public int staticLf2() {
    return staticLfAdd6.apply(7, 8);
  }

  @Benchmark
  public int staticLf4() {
    return staticLfAdd4.apply(5, 6, 7, 8);
  }

  @Benchmark
  public int staticLf8() {
    return staticLfAddAll.apply(1, 2, 3, 4, 5, 6, 7, 8);
  }

  @Benchmark
  public Integer boxedLf1() {
    return boxedLfAdd7.apply(8);
  }

  @Benchmark
  public Integer boxedLf2() {
    return boxedLfAdd6.apply(7, 8);
  }

  @Benchmark
  public Integer boxedLf4() {
    return boxedLfAdd4.apply(5, 6, 7, 8);
  }

  @Benchmark
  public Integer boxedLf8() {
    return boxedLfAddAll.apply(1, 2, 3, 4, 5, 6, 7, 8);
  }

  @Benchmark
  public Integer boxedStaticLf1() {
    return boxedStaticLfAdd7.apply(8);
  }

  @Benchmark
  public Integer boxedStaticLf2() {
    return boxedStaticLfAdd6.apply(7, 8);
  }

  @Benchmark
  public Integer boxedStaticLf4() {
    return boxedStaticLfAdd4.apply(5, 6, 7, 8);
  }

  @Benchmark
  public Integer boxedStaticLf8() {
    return boxedStaticLfAddAll.apply(1, 2, 3, 4, 5, 6, 7, 8);
  }

  public static Integer addStatic(
      Integer i0,
      Integer i1,
      Integer i2,
      Integer i3,
      Integer i4,
      Integer i5,
      Integer i6,
      Integer i7) {
    return i0 + i1 + i2 + i3 + i4 + i5 + i6 + i7;
  }
}

interface BoxedFunction8 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4, Integer i5, Integer i6, Integer i7);

  default BoxedFunction7 curry(Integer i) {
    return (i1, i2, i3, i4, i5, i6, i7) -> apply(i, i1, i2, i3, i4, i5, i6, i7);
  }
}

interface BoxedFunction7 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4, Integer i5, Integer i6);

  default BoxedFunction6 curry(Integer i) {
    return (i1, i2, i3, i4, i5, i6) -> apply(i, i1, i2, i3, i4, i5, i6);
  }
}

interface BoxedFunction6 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4, Integer i5);

  default BoxedFunction5 curry(Integer i) {
    return (i1, i2, i3, i4, i5) -> apply(i, i1, i2, i3, i4, i5);
  }
}

interface BoxedFunction5 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3, Integer i4);

  default BoxedFunction4 curry(Integer i) {
    return (i1, i2, i3, i4) -> apply(i, i1, i2, i3, i4);
  }
}

interface BoxedFunction4 {
  Integer apply(Integer i0, Integer i1, Integer i2, Integer i3);

  default BoxedFunction3 curry(Integer i) {
    return (i1, i2, i3) -> apply(i, i1, i2, i3);
  }
}

interface BoxedFunction3 {
  Integer apply(Integer i0, Integer i1, Integer i2);

  default BoxedFunction2 curry(Integer i) {
    return (i1, i2) -> apply(i, i1, i2);
  }
}

interface BoxedFunction2 {
  Integer apply(Integer i0, Integer i1);

  default BoxedFunction1 curry(Integer i) {
    return (i1) -> apply(i, i1);
  }
}

interface BoxedFunction1 {
  Integer apply(Integer i0);
}

interface Function8 {
  int apply(int i0, int i1, int i2, int i3, int i4, int i5, int i6, int i7);

  default Function7 curry(int i) {
    return (i1, i2, i3, i4, i5, i6, i7) -> apply(i, i1, i2, i3, i4, i5, i6, i7);
  }
}

interface Function7 {
  int apply(int i0, int i1, int i2, int i3, int i4, int i5, int i6);

  default Function6 curry(int i) {
    return (i1, i2, i3, i4, i5, i6) -> apply(i, i1, i2, i3, i4, i5, i6);
  }
}

interface Function6 {
  int apply(int i0, int i1, int i2, int i3, int i4, int i5);

  default Function5 curry(int i) {
    return (i1, i2, i3, i4, i5) -> apply(i, i1, i2, i3, i4, i5);
  }
}

interface Function5 {
  int apply(int i0, int i1, int i2, int i3, int i4);

  default Function4 curry(int i) {
    return (i1, i2, i3, i4) -> apply(i, i1, i2, i3, i4);
  }
}

interface Function4 {
  int apply(int i0, int i1, int i2, int i3);

  default Function3 curry(int i) {
    return (i1, i2, i3) -> apply(i, i1, i2, i3);
  }
}

interface Function3 {
  int apply(int i0, int i1, int i2);

  default Function2 curry(int i) {
    return (i1, i2) -> apply(i, i1, i2);
  }
}

interface Function2 {
  int apply(int i0, int i1);

  default Function1 curry(int i) {
    return (i1) -> apply(i, i1);
  }
}

interface Function1 {
  int apply(int i0);
}
