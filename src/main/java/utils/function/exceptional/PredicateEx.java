package utils.function.exceptional;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 02.02.14
 */
@FunctionalInterface
public interface PredicateEx<T> {

    boolean test(T input) throws Exception;
}
