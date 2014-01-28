package utils;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 28.01.14
 */
@FunctionalInterface
public interface FunctionEx<T, R> {

    <X extends Throwable> R apply(T input) throws X;

}
