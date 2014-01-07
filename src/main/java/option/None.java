package option;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * User: Dmitrii Fateev
 * Date: 06.01.14
 * Email: <a href="mailto:dsfateev@luxoft.com"></a>
 */
public class None extends Option {
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

    public static None None() {
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
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof None)) return false;

        None other = (None) obj;
        return Objects.equals(EMPTY, other);

    }
}
