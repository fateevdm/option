package utils.function.exceptional;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 28.01.14
 */
@FunctionalInterface
public interface SupplierEx<T> {

    T get() throws Exception;
}
