package utils.function;

/**
 * This is simple version of Java 8 Function. Made for independence from JDK 8
 * <p>
 * Represents a function that accepts one argument and produces a result.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface Function<T, R> extends java.util.function.Function<T, R> {

    static <T, V> Function<V, T> constant(T t, V v) {
        return (V r) -> t;
    }
}
