package org.samples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
@Fork(1)
@Measurement(iterations = 5, timeUnit = TimeUnit.NANOSECONDS)
public class LeftRightShiftsVsMulDivOps {

    private int shift = 1;
    private int mulDiv = 2;
    private int value = 10;

    @Benchmark
    public int shiftLeft() {
        return value << shift;
    }

    @Benchmark
    public int mul() {
        return value * mulDiv;
    }

    @Benchmark
    public int shiftRight() {
        return value >> shift;
    }

    @Benchmark
    public int div() {
        return value / mulDiv;
    }

}
