package option;

import java.util.Iterator;
import java.util.Objects;

public class Some<T> extends Option<T>{

    private final T value;

    private Some(T value) {
        this.value = Objects.requireNonNull(value);
    }

    public static <T> Some<T> Some(T value){
        return new Some<T>(Objects.requireNonNull(value));
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
        return "Some{ " +
                 value +
                " }";
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
        return Objects.hashCode(value);
    }

    private class OptionIter<T> implements Iterator<T>{
        private boolean hasNext = true;
        private final T elem;

        private OptionIter(T elem){
           this.elem = elem;
        }
        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public T next() {
            hasNext = false;
            return this.elem;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }
    }
}
