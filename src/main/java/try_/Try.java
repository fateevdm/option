package try_;

import option.Option;
import utils.*;
import utils.control.Errors;

import static option.Option.None;
import static option.Option.Some;

/**
 * @author : Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 25.01.14
 */
public abstract class Try<T> {

    protected Try(){}

    @SuppressWarnings("unchecked")
    public static <T> Try<T> asTry(SupplierX<? extends T> block) {
          try {
              return Success(block.get());
          } catch (Throwable t){
              if (Errors.isFatal(t)) Errors.<RuntimeException>throwAny(t);
              return Failure(t);
          }
    }

    @SuppressWarnings("unchecked")
    public static <T, X extends RuntimeException> Try<T> Success(T value){
        if (value instanceof Throwable) Errors.<X>throwAny((Throwable)value);
        return new Success<T>(value);
    }

    public static <T> Try<T> Failure(Throwable exception){
        return new Failure<T>(exception);
    }

    public abstract boolean isFailure();

    public abstract boolean isSuccess();

    public abstract <X extends Throwable> T get() throws X;

    public abstract Try<Throwable> failed();

    public abstract Try<T> filter(Predicate<? super T> p);

    public abstract<U> Try<U> flatMap(Function<? super T, ? extends Try<U>> f);

    public abstract void foreach(Consumer<? super T> f);

    public abstract <U> Try<U> map(FunctionEx<? super T,? extends U> f);

    public abstract Try<T> recover(FunctionEx<Throwable, ? extends T> f);

    public abstract Try<T> recoverWith(FunctionEx<Throwable, ? extends Try<T>> rescueException);

    @SuppressWarnings("unchecked")
    public T getOrElse(SupplierX<? extends T> def){
        if (isSuccess()) return ((Success<T>)this).get();
        else try {
            return def.get();
        } catch (Throwable throwable) {
            Errors.throwAsUnchecked(throwable);
        }
        return null;
    }

    public Try<? super T> orElse(SupplierX<Try<? super T>> def){
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
        else return Some(((Success<T>)this).get());
    }

    public <U> Try<U> transform(Function<? super T, ? extends Try<U>> s, Function<Throwable, ? extends Try<U>> f){
        if (isSuccess()) return flatMap(s);
        else {
            Try<Throwable> t =this.failed();
            return f.apply(((Success<Throwable>) t).get());
        }
    }

}
