package option;

import utils.Consumer;
import utils.Function;
import utils.Predicate;
import utils.Supplier;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static option.None.None;
import static option.Some.Some;

/**
 * This is implementation of optional value based on Optional.java from JDK 8
 * and Option.scala from Scala SDK.
 * <p/>
 * A container object which may or may not contain a non-null value.
 * If a value is present, {@code isPresent()} will return {@code true} and
 * {@code get()} will return the value.
 * <p/>
 * <p>Additional methods that depend on the presence or absence of a contained
 * value are provided, such as {@link #orElse(Object) orElse()}
 * (return a default value if value not present) and
 * {@link #foreach(utils.Consumer)} (utils.Consumer)} (execute a block
 * of code if the value is present).
 * <p/>
 * <p>This is a <a href="../lang/doc-files/ValueBased.html">value-based</a>
 * class; use of identity-sensitive operations (including reference equality
 * ({@code ==}), identity hash code, or synchronization) on instances of
 * {@code Option} may have unpredictable results and should be avoided.
 * <p/>
 * <p/>
 *
 * @author Dmitry Fateev
 *         Email: <a href="mailto:wearing.fateev@gmail.com"></a>
 * @since 06.01.14
 */
public abstract class Option<T> implements Iterable<T> {

    /**
     * If a value is present in this {@code Option}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * @return the non-null value held by this {@code Option}
     * @throws java.util.NoSuchElementException if there is no value present
     * @see Option#isPresent()
     */
    public abstract T get();

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public abstract boolean isPresent();

    /**
     * Returns true if the option is {@code None}, false otherwise.
     *
     * @return {@code true} if the option is {@code None}, otherwise {@code false}
     */
    public final boolean isEmpty() {
        return !isPresent();
    }

    /**
     * Returns a {@code None} instance.  No value is present for this
     * Option.
     * <p/>
     * Though it may be tempting to do so, avoid testing if an object
     * is none by comparing with {@code ==} against instances returned by
     * {@code Option.none()}. There is no guarantee that it is a singleton.
     * Instead, use {@link #isPresent()}.
     *
     * @param <T> Type of the non-existent value
     * @return an {@code None}
     */
    private static <T> Option<T> none() {
        @SuppressWarnings("unchecked")
        Option<T> t = None();
        return t;
    }

    /**
     * Returns an {@code Option} describing the specified value, if non-null,
     * otherwise returns a {@code None}.
     *
     * @param <T>   the class of the value
     * @param value the possibly-null value to describe
     * @return an {@code Option} with a present value if the specified value
     * is non-null, otherwise a {@code None}
     */
    public static <T> Option<T> ofNullable(T value) {
        if (value == null) return none();
        else return Some(value);
    }

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is
     *                              null
     */
    public final void foreach(Consumer<? super T> consumer) {
        if (isPresent()) consumer.accept(this.get());
    }

    /**
     * If a value is present, and the value matches the given predicate,
     * return an {@code Option} describing the value, otherwise return a
     * {@code None}.
     *
     * @param p a predicate to apply to the value, if present
     * @return an {@code Option} describing the value of this {@code Option}
     * if a value is present and the value matches the given predicate,
     * otherwise a {@code None}
     * @throws NullPointerException if the predicate is null
     */
    public final Option<T> filter(Predicate<? super T> p) {
        if (isPresent() && p.test(this.get())) return this;
        else return none();
    }

    /**
     * Returns this {@code option} if it is nonempty '''and''' applying the predicate {@code p} to
     * this {@code option}'s value returns {@code false}. Otherwise, return {@code None}.
     *
     * @param p the predicate used for testing.
     * @throws NullPointerException if the predicate is null
     */
    public final Option<T> filterNot(Predicate<? super T> p) {
        if (isEmpty() || !p.test(this.get())) return this;
        else return none();
    }

    /**
     * If a value is present, apply the provided mapping function to it,
     * and if the result is non-null, return an {@code Option} describing the
     * result.  Otherwise return a {@code None}.
     *
     * @param <U>    The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code Option} describing the result of applying a mapping
     * function to the value of this {@code Option}, if a value is present,
     * otherwise a {@code None}
     * @throws NullPointerException if the mapping function is null
     */
    public final <U> Option<U> map(Function<? super T, ? extends U> mapper) {
        if (isEmpty()) return none();
        else return ofNullable(mapper.apply(this.get()));
    }

    /**
     * If a value is present, apply the provided {@code Option}-bearing
     * mapping function to it, return that result, otherwise return a
     * {@code None}.  This method is similar to {@link #map(Function)},
     * but the provided mapper is one whose result is already an {@code Option},
     * and if invoked, {@code flatMap} does not wrap it with an additional
     * {@code Option}.
     *
     * @param <U>    The type parameter to the {@code Option} returned by
     * @param mapper a mapping function to apply to the value, if present
     *               the mapping function
     * @return the result of applying an {@code Option}-bearing mapping
     * function to the value of this {@code Option}, if a value is present,
     * otherwise a {@code None}
     * @throws NullPointerException if the mapping function is null or returns
     *                              a null result
     */
    public final <U> Option<U> flatMap(Function<? super T, ? extends Option<U>> mapper) {
        if (isEmpty()) return none();
        else return Objects.requireNonNull(mapper.apply(this.get()));
    }

    /**
     * Test whether the option contains a given value as an element.
     *
     * @param elem the element to test
     * @return {@code true} if the option has an element that equal (as determined by {@link #equals(Object)}
     * to {@code elem}, {@code false} otherwise.
     */
    public final boolean contains(T elem) {
        return !isEmpty() && this.get().equals(elem);
    }

    /**
     * Returns {@code true} if this option is nonempty '''and''' the predicate
     * {@code p} returns true when applied to this {@code option}'s value.
     * Otherwise, returns {@code false}.
     *
     * @param p the predicate to test
     * @throws NullPointerException if the predicate is null
     */
    public final boolean exist(Predicate<? super T> p) {
        return !isEmpty() && p.test(this.get());
    }

    /**
     * Returns {@code true} if this option is None '''or''' the predicate
     * {@code p} returns {@code true} when applied to this {@code option}'s value.
     *
     * @param p the predicate to test
     * @throws NullPointerException if the predicate is null
     */
    public final boolean forall(Predicate<? super T> p) {
        return isEmpty() || p.test(this.get());
    }

    /**
     * Returns a singleton list containing the {@code option}'s value
     * if it is nonempty, or the None list if the {@code option} is none.
     */
    public final List<T> toList() {
        if (isEmpty()) return Collections.emptyList();
        else return Collections.singletonList(this.get());
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present, may
     *              be null
     * @return the value, if present, otherwise {@code other}
     */
    public final T orElse(T other) {
        return isEmpty() ? other : this.get();
    }

    /**
     * Returns the option's value if it is nonempty,
     * or {@code null} if it is None.
     * Although the use of null is discouraged, code written to use
     * {@code option} must often interface with code that expects and returns nulls.
     * Example:
     * <pre>{@code
     *     Option<String> initialText = getInitialText();
     *     JComponent textField = new JComponent(initialText.orNull,20)
     * }</pre>
     */
    public final T orNull() {
        return orElse(null);
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return
     * the result of that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if no value
     *              is present
     * @return the value if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if value is not present and {@code other} is
     *                              null
     */
    public final T orElseGet(Supplier<? extends T> other) {
        return isPresent() ? this.get() : other.get();
    }

    /**
     * Return the contained value, if present, otherwise throw an exception
     * to be created by the provided supplier.
     *
     * @param <X>               Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to
     *                          be thrown
     * @return the present value
     * @throws X                    if there is no value present
     * @throws NullPointerException if no value is present and
     *                              {@code exceptionSupplier} is null
     *                              A method reference to the exception constructor with an empty
     *                              argument list can be used as the supplier.
     */
    public final <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) return this.get();
        else throw exceptionSupplier.get();
    }

}