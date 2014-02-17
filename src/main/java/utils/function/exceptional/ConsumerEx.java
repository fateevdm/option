package utils.function.exceptional;

import java.util.Objects;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 02.02.14
 */
@FunctionalInterface
public interface ConsumerEx<T> {

    /**
     * Performs this operation on the given argument.
     * May produce exception.
     *
     * @param in the input argument
     * @throws java.lang.Exception
     */
    void accept(T in) throws Exception;

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default ConsumerEx<T> andThen(ConsumerEx<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
