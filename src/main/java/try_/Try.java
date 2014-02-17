package try_;

import option.Option;
import utils.control.Errors;
import utils.function.exceptional.ConsumerEx;
import utils.function.exceptional.FunctionEx;
import utils.function.exceptional.PredicateEx;
import utils.function.exceptional.SupplierEx;

import static option.Option.None;
import static option.Option.Some;

/**
 * @author : Dmitrii Fateev
 *         E-mail: wearing.fateev@gmail.com
 * @since: 25.01.14
 */
public interface Try<T> {

    static <T> Try<T> asTry(SupplierEx<? extends T> block) {
        try {
            return success(block.get());
        } catch (Throwable t) {
            if (Errors.isFatal(t)) Errors.throwAsUnchecked(t);
            return failure(t);
        }
    }

    @SuppressWarnings("unchecked")
    static <T, X extends Throwable> Try<T> success(T value) {
        if (value instanceof Throwable) Errors.throwAsUnchecked((X) value);
        return new Success<>(value);
    }

    static <T> Try<T> failure(Throwable exception) {
        return new Failure<>(exception);
    }

    boolean isFailure();

    boolean isSuccess();

    T get();

    Try<Throwable> failed();

    Try<T> filter(PredicateEx<? super T> p);

    <U> Try<U> flatMap(FunctionEx<? super T, ? extends Try<U>> f);

    void foreach(ConsumerEx<? super T> f);

    <U> Try<U> map(FunctionEx<? super T, ? extends U> f);

    Try<T> recover(FunctionEx<Throwable, ? extends T> f);

    Try<T> recoverWith(FunctionEx<Throwable, ? extends Try<T>> rescueException);

    default T getOrElse(SupplierEx<? extends T> def) {
        if (isFailure()) try {
            return def.get();
        } catch (Exception e) {
            Errors.throwAsUnchecked(e);
        }
        return get();
    }

    default Try<T> orElse(SupplierEx<? extends Try<T>> def) {
        if (isSuccess()) return this;
        else try {
            return def.get();
        } catch (Throwable e) {
            if (Errors.isFatal(e)) Errors.throwAsUnchecked(e);
            return failure(e);
        }
    }

    default Option<T> toOption() {
        if (isFailure()) return None();
        else return Some(get());
    }

    default <U> Try<U> transform(FunctionEx<? super T, ? extends Try<U>> s, FunctionEx<Throwable, ? extends Try<U>> f) {
        try {
            if (isSuccess()) return s.apply(get());
            else return f.apply(failed().get());
        } catch (Throwable t) {
            if (Errors.isFatal(t)) Errors.throwAsUnchecked(t);
            return failure(t);
        }

    }

}
