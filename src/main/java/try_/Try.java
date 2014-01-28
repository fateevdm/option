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
    public static <T, X extends Throwable> Try<T> asTry(SupplierX<? extends T> block) throws X{
          try {
              return Success(block.get());
          } catch (Throwable t){
              if (Errors.isNonFatal(t)) return Failure(t);
              else throw (X)t;
          }
    }

    @SuppressWarnings("unchecked")
    public static <T, X extends Throwable> Try<T> Success(T value) throws X{
        if (value instanceof Throwable) throw (X)value;
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

    public abstract <U, X extends Throwable> Try<U> map(FunctionEx<? super T,? extends U> f) throws X;

    public abstract Try<? super T> recover(Function<Throwable, ? extends T> f);

    public abstract < X extends Throwable> Try<? super T> recoverWith(FunctionEx<Throwable, ? extends Try<T>> rescueException) throws X;

    @SuppressWarnings("unchecked")
    public <X extends Throwable> T getOrElse(SupplierX<? extends T> def) throws X {
        if (isFailure()) try {
            return def.get();
        } catch (Throwable e) {
            throw (X)e;
        }
        else return get();
    }

    public Try<? super T> orElse(SupplierX<Try<? super T>> def){
        if (isSuccess()) return this;
        else try {
            return def.get();
        } catch (Throwable e) {
            if (Errors.isNonFatal(e)) return Failure(e);
            else throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public Option<T> toOption(){
        if (isFailure()) return None();
        else return Some(get());
    }

    public <U> Try<U> transform(Function<? super T, ? extends Try<U>> s, FunctionEx<Throwable, ? extends Try<U>> f){
        if (isSuccess()) return flatMap(s);
        else return f.apply(failed().get());
    }

}
