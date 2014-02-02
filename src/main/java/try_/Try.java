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
public abstract class Try<T> {

    protected Try(){}

    @SuppressWarnings("unchecked")
    public static <T> Try<T> asTry(SupplierEx<? extends T> block) {
          try {
              return Success(block.get());
          } catch (Throwable t){
              if (Errors.isFatal(t)) Errors.throwAsUnchecked(t);
              return Failure(t);
          }
    }

    @SuppressWarnings("unchecked")
    public static <T, X extends Throwable> Try<T> Success(T value){
        if (value instanceof Throwable) Errors.throwAsUnchecked((X)value);
        return new Success<>(value);
    }

    public static <T> Try<T> Failure(Throwable exception){
        return new Failure<>(exception);
    }

    public abstract boolean isFailure();

    public abstract boolean isSuccess();

    public abstract T get();

    public abstract Try<Throwable> failed();

    public abstract Try<T> filter(PredicateEx<? super T> p);

    public abstract<U> Try<U> flatMap(FunctionEx<? super T, ? extends Try<U>> f);

    public abstract void foreach(ConsumerEx<? super T> f);

    public abstract <U> Try<U> map(FunctionEx<? super T,? extends U> f);

    public abstract Try<T> recover(FunctionEx<Throwable, ? extends T> f);

    public abstract Try<T> recoverWith(FunctionEx<Throwable, ? extends Try<T>> rescueException);

    @SuppressWarnings("unchecked")
    public T getOrElse(SupplierEx<? extends T> def) {
        if (isFailure()) try {
            return def.get();
        } catch (Exception e) {
            Errors.throwAsUnchecked(e);
        }
        return get();
    }

    public Try<T> orElse(SupplierEx<? extends Try<T>> def) {
        if (isSuccess()) return this;
        else try {
            return def.get();
        } catch (Throwable e) {
            if (Errors.isFatal(e)) Errors.throwAsUnchecked(e);
            return Failure(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Option<T> toOption(){
        if (isFailure()) return None();
        else return Some(get());
    }

    public <U> Try<U> transform(FunctionEx<? super T, ? extends Try<U>> s, FunctionEx<Throwable, ? extends Try<U>> f) {
        try{
            if (isSuccess()) return s.apply(get());
            else return f.apply(failed().get());
        } catch (Throwable t){
            if (Errors.isFatal(t)) Errors.throwAsUnchecked(t);
            return Failure(t);
        }

    }

}
