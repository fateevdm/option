package utils.function.exceptional;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 02.02.14
 */
@FunctionalInterface
public interface ConsumerEx<T> {

    void accept(T in) throws Exception;
}
