package try_;

import utils.*;
import utils.control.Errors;

/**
 * User: Dmitrii Fateev
 * Date: 25.01.14
 * E-mail: wearing.fateev@gmail.com
 */
final class Failure<T> extends Try<T> {

    private final Throwable exception;

    protected Failure(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T get(){
        throw (RuntimeException)exception;
    }

    @Override
    public Try<Throwable> failed() {
        return Success(exception);
    }

    @Override
    public Try<T> filter(Predicate<? super T> p) {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Try<U> flatMap(FunctionEx<? super T, ? extends Try<U>> f) {
        return (Try<U>) this;
    }

    @Override
    public void foreach(Consumer<? super T> f) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Try<U> map(FunctionEx<? super T, ? extends U> f) {
        return (Try<U>) this;
    }

    @Override
    public Try<T> recover(final FunctionEx<Throwable, ? extends T> rescueException) {
        return Try.asTry(new SupplierX<T>() {
            @Override
            public <X extends Throwable> T get() throws X {
                return rescueException.apply(exception);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Try<T> recoverWith(FunctionEx<Throwable, ? extends Try<T>> rescueException){
        try {
            return rescueException.apply(exception);
        } catch (Throwable t) {
            if (Errors.isFatal(t)) Errors.throwAsUnchecked(t);
            return Failure(t);
        }
    }

    @Override
    public String toString() {
        return "Failure{" +
                exception.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Failure failure = (Failure) o;

        return exception.getClass().equals(failure.exception.getClass());

    }

    @Override
    public int hashCode() {
        return exception.hashCode();
    }

}
