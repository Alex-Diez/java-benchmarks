package org.samples;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
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

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5, timeUnit = TimeUnit.NANOSECONDS)
public class ReflectionVsMethodHandlers {

    private static final String ARRAY_FOLDING_METHOD_NAME = "arrayFoldingExperimentCalculation";
    private static final String RETURN_CONSTANT_METHOD_NAME = "returnConstant";
    private static final String FIELD_INCREMENT_METHOD_NAME = "fieldIncrement";

    private static final MethodHandle staticFinalArrayFoldingMethodHandling;
    private static final MethodHandle staticFinalReturnConstantMethodHandling;
    private static final MethodHandle staticFinalFieldIncrementMethodHandling;

    private int[] data = new int[1024];
    private int field;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType type = MethodType.methodType(int.class);
        try {
            staticFinalArrayFoldingMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, ARRAY_FOLDING_METHOD_NAME, type);
            staticFinalReturnConstantMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, RETURN_CONSTANT_METHOD_NAME, type);
            staticFinalFieldIncrementMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, FIELD_INCREMENT_METHOD_NAME, type);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Method arrayFoldingReflection;
    private MethodHandle arrayFoldingMethodHandling;
    private final MethodHandle finalArrayFoldingMethodHandling;
    private Method returnConstantReflection;
    private MethodHandle returnConstantMethodHandling;
    private final MethodHandle finalReturnConstantMethodHandling;
    private Method fieldIncrementReflection;
    private MethodHandle fieldIncrementMethodHandling;
    private final MethodHandle finalFieldIncrementMethodHandling;

    public ReflectionVsMethodHandlers() {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType type = MethodType.methodType(int.class);
        try {
            finalArrayFoldingMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, ARRAY_FOLDING_METHOD_NAME, type);
            finalReturnConstantMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, RETURN_CONSTANT_METHOD_NAME, type);
            finalFieldIncrementMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, FIELD_INCREMENT_METHOD_NAME, type);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Setup
    public void initData() throws Exception {
        Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt();
        }
        arrayFoldingReflection = getClass().getMethod(ARRAY_FOLDING_METHOD_NAME);
        returnConstantReflection = getClass().getMethod(RETURN_CONSTANT_METHOD_NAME);
        fieldIncrementReflection = getClass().getMethod(FIELD_INCREMENT_METHOD_NAME);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType type = MethodType.methodType(int.class);
        arrayFoldingMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, ARRAY_FOLDING_METHOD_NAME, type);
        returnConstantMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, RETURN_CONSTANT_METHOD_NAME, type);
        fieldIncrementMethodHandling = lookup.findVirtual(ReflectionVsMethodHandlers.class, FIELD_INCREMENT_METHOD_NAME, type);
    }
    
    public int returnConstant() {
        return 42;
    }

    public int fieldIncrement() {
        return field++;
    }

    public int arrayFoldingExperimentCalculation() {
        int[] data = Arrays.copyOf(this.data, this.data.length);
        int sum = 0;
        for (int i : data) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int arrayFoldingBaseline() {
        return arrayFoldingExperimentCalculation();
    }

    @Benchmark
    public int arrayFoldingReflectionCall() throws Exception {
        return (int) arrayFoldingReflection.invoke(this);
    }

    @Benchmark
    public int arrayFoldingMethodHandlerCall() throws Throwable {
        return (int) arrayFoldingMethodHandling.invokeExact(this);
    }

    @Benchmark
    public int finalArrayFoldingMethodHandlerCall() throws Throwable {
        return (int) finalArrayFoldingMethodHandling.invokeExact(this);
    }

    @Benchmark
    public int staticFinalArrayFoldingMethodHandlerCall() throws Throwable {
        return (int) staticFinalArrayFoldingMethodHandling.invokeExact(this);
    }

    @Benchmark
    public int returnConstantBaseline() {
        return returnConstant();
    }

    @Benchmark
    public int returnConstantReflectionCall() throws Exception {
        return (int) returnConstantReflection.invoke(this);
    }

    @Benchmark
    public int returnConstantMethodHandlerCall() throws Throwable {
        return (int) returnConstantMethodHandling.invokeExact(this);
    }

    @Benchmark
    public int finalReturnConstantMethodHandlerCall() throws Throwable {
        return (int) finalReturnConstantMethodHandling.invokeExact(this);
    }

    @Benchmark
    public int staticFinalReturnConstantMethodHandlerCall() throws Throwable {
        return (int) staticFinalReturnConstantMethodHandling.invokeExact(this);
    }

    @Benchmark
    public int fieldIncrementBaseline() {
        return fieldIncrement();
    }

    @Benchmark
    public int fieldIncrementReflectionCall() throws Exception {
        return (int) fieldIncrementReflection.invoke(this);
    }

    @Benchmark
    public int fieldIncrementMethodHandlerCall() throws Throwable {
        return (int) fieldIncrementMethodHandling.invokeExact(this);
    }

    @Benchmark
    public int finalFieldIncrementMethodHandlerCall() throws Throwable {
        return (int) finalFieldIncrementMethodHandling.invokeExact(this);
    }

    @Benchmark
    public int staticFinalFieldIncrementMethodHandlerCall() throws Throwable {
        return (int) staticFinalFieldIncrementMethodHandling.invokeExact(this);
    }
}
