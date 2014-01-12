package option;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This object represents non-existent values.
 *
 * @author Dmitrii Fateev
 *         Email: <a href="mailto:dsfateev@luxoft.com"></a>
 * @since 06.01.14
 */
public final class None extends Option {
    @Override
    public Iterator iterator() {
        return Collections.emptyIterator();
    }

    /**
     * One per JVM
     */
    private static final None EMPTY = new None();

    private None() {
    }

    @Override
    public Object get() {
        throw new NoSuchElementException("Called get on None");
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    public static Option None() {
        return EMPTY;
    }

    @Override
    public String toString() {
        return "None";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
