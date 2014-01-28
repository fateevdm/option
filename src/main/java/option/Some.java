package option;

import java.util.Iterator;

/**
 * Class Some represents existing values of type T.
 *
 * @author Dmitrii Fateev
 *         Email: <a href="mailto:wearing.fateev@gmail.com"></a>
 * @since 06.01.14
 */

final class Some<T> extends Option<T> {

    private final T value;

    Some(T value) {
        this.value = value;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Iterator<T> iterator() {
        return new OptionIter<T>(value);
    }

    @Override
    public String toString() {
        return "Some{" +
                value +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Some some = (Some) o;

        return value.equals(some.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    private static class OptionIter<T> implements Iterator<T> {
        private boolean hasNext = true;
        private final T elem;

        private OptionIter(T elem) {
            this.elem = elem;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public T next() {
            if (hasNext) {
                hasNext = false;
                return this.elem;
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }
    }
}
