package try_;

import utils.*;
import utils.control.Errors;

import java.util.NoSuchElementException;

/**
 * User: Dmitrii Fateev
 * Date: 25.01.14
 * E-mail: wearing.fateev@gmail.com
 */
public final class Success<T> extends Try<T> {

    private final T value;

    protected Success(T value){
        this.value = value;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Try<Throwable> failed() {
        return Failure(new UnsupportedOperationException("Success.failed"));
    }

    @Override
    public Try<T> filter(Predicate<? super T> p) {
        try {
            if (p.test(value)) return this;
            else return Failure(new NoSuchElementException("Predicate does not hold for " + value));
        } catch (Throwable t){
            if (Errors.isNonFatal(t)) return Failure(t);
            else throw t;
        }

    }

    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> f) {
        try {
            return f.apply(value);
        }catch (Throwable t){
            if (Errors.isNonFatal(t)) return Failure(t);
            else throw t;
        }
    }

    @Override
    public void foreach(Consumer<? super T> f) {
        f.accept(value);
    }

    @Override
    public <U, X extends Throwable> Try<U> map(FunctionEx<? super T, ? extends U> f) throws X{
        return Try.asTry(new SupplierX<U>() {
            @Override
            public <X extends Throwable> U get() throws X {
                return f.apply(value);
            }
        });
    }

    @Override
    public Try<? super T> recover(Function<Throwable, ? extends T> f) {
        return this;
    }

    @Override
    public <X extends Throwable> Try<? super T> recoverWith(FunctionEx<Throwable, ? extends Try<T>> rescueException) throws X{
        return this;
    }

    @Override
    public String toString() {
        return "Success{" +
                value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Success success = (Success) o;

        return value.equals(success.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
