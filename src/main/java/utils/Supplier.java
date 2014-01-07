package utils;

/**
 * There is Supplier from JDK 8. Made for independence from JDK 8
 * <p/>
 * Represents a supplier of results.
 * <p/>
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * @param <T> the type of results supplied by this supplier
 */
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
