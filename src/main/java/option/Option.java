package option;

import utils.Consumer;
import utils.Function;
import utils.Predicate;
import utils.Supplier;

import java.util.Objects;

import static option.None.None;
import static option.Some.Some;

/**
 * User: Dmitrii Fateev
 * Date: 06.01.14
 * Email: <a href="mailto:dsfateev@luxoft.com"></a>
 */
public abstract class Option<T> implements Iterable<T> {
    public abstract T get();

    public abstract boolean isPresent();

    public boolean isEmpty() {
        return !isPresent();
    }

    public static <T> Option<T> empty() {
        @SuppressWarnings("unchecked")
        None t = None();
        return t;
    }

    public static <T> Option<T> ofNullable(T val) {
        if (val == null) return empty();
        else return Some(val);
    }

    public void foreach(Consumer<? super T> consumer) {
        if (isPresent()) consumer.accept(get());
    }

    public Option<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (isPresent()) {
            if (predicate.test(this.get())) return this;
            else return empty();
        } else return empty();
    }

    public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (isEmpty()) return empty();
        else return ofNullable(mapper.apply(get()));
    }

    public <U> Option<U> flatMap(Function<? super T, ? extends Option<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (this.isEmpty()) return empty();
        else return Objects.requireNonNull(mapper.apply(this.get()));
    }

    public T orElse(T other) {
        return isEmpty() ? other : this.get();
    }

    public T orElseGet(Supplier<? extends T> other) {
        return isPresent() ? this.get() : other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) return this.get();
        else throw exceptionSupplier.get();
    }


}

