package utils;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 28.01.14
 */
@FunctionalInterface
public interface SupplierX<T>  {

      <X extends Throwable> T get() throws X;
}
