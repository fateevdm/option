package option;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This object represents non-existent values.
 *
 * @author Dmitrii Fateev
 *         Email: <a href="mailto:wearing.fateev@gmail.com"></a>
 * @since 06.01.14
 */
final class None<T> extends Option<T> {
    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    /**
     * One per JVM
     */
    static final None<?> EMPTY = of();

    private static <T> None<?> of(){
        return new None<T>();
    }

    private None() {
    }

    @Override
    public T get() {
        throw new NoSuchElementException("Called get on None");
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    /**
     * Returns a {@code None} instance.  No value is present for this
     * Option.
     * <p/>
     * Though it may be tempting to do so, avoid testing if an object
     * is none by comparing with {@code ==} against instances returned by
     * {@code Option.none()}. There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     * @return an {@code None}
     */
    @SuppressWarnings("unchecked")
    protected static <T> Option<T> empty() {
        return (Option<T>)EMPTY;
    }

    @Override
    public String toString() {
        return "None";
    }

    @Override
    public int hashCode() {
        return 0x598df91c;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
