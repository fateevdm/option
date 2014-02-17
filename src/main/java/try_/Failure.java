package try_;

import utils.control.Errors;
import utils.function.exceptional.ConsumerEx;
import utils.function.exceptional.FunctionEx;
import utils.function.exceptional.PredicateEx;

/**
 * User: Dmitrii Fateev
 * Date: 25.01.14
 * E-mail: wearing.fateev@gmail.com
 */
final class Failure<T> implements Try<T> {

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
        return Try.success(exception);
    }

    @Override
    public Try<T> filter(PredicateEx<? super T> p) {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Try<U> flatMap(FunctionEx<? super T, ? extends Try<U>> f) {
        return (Try<U>) this;
    }

    @Override
    public void foreach(ConsumerEx<? super T> f) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> Try<U> map(FunctionEx<? super T, ? extends U> f) {
        return (Try<U>) this;
    }

    @Override
    public Try<T> recover(final FunctionEx<Throwable,? extends T> rescueException) {
        return Try.asTry(() -> rescueException.apply(exception));
    }

    @Override
    public Try<T> recoverWith(FunctionEx<Throwable, ? extends Try<T>> rescueException){
        try {
            return rescueException.apply(exception);
        } catch (Throwable t) {
            if (Errors.isFatal(t)) Errors.throwAsUnchecked(t);
            return Try.failure(t);
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
