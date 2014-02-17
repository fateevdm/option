package utils.function.exceptional;

import java.util.Objects;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 28.01.14
 */
@FunctionalInterface
public interface FunctionEx<T, R> {

    /**
     * Applies this function to the given argument.
     * May produce exception.
     *
     * @param t the function argument
     * @return the function result
     * @throws java.lang.Exception
     */
    R apply(T t) throws Exception;

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>    the type of input to the {@code before} function, and to the
     *               composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     * @throws NullPointerException if before is null
     * @see #andThen(utils.function.exceptional.FunctionEx)
     */
    default <V> FunctionEx<V, R> compose(FunctionEx<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     * @see #compose(utils.function.exceptional.FunctionEx)
     */
    default <V> FunctionEx<T, V> andThen(FunctionEx<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> FunctionEx<T, T> identity() {
        return t -> t;
    }

    static <T, V> FunctionEx<V, T> constant(T t, V v) {
        return (V r) -> t;
    }

}
