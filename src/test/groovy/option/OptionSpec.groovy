package option

import spock.lang.Specification
import utils.function.Function

import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.Supplier

import static option.Option.None
import static option.Option.Some

/**
 * User: Dmitry Fateev
 * Date: 06.01.14
 * Email: <a href="mailto:wearing.fateev@gmail.com"></a>
 */
class OptionSpec extends Specification{

    def "check type variance"(){
        when:
            List<Option<? extends String>> options = new ArrayList<>()
            options.add(Some("a")); options.add(None())
        then: options == [Some("a"), None()]
    }

    def "isPresent on Some('s') should return 'true'"(){
        expect: Some("s").isPresent()
    }

    def "isPresent on None should return 'false'"(){
        expect: !None().isPresent()
    }

    def "get from Some(2) should return 2"(){
        expect: Some(2).get() == 2
    }

    def "get from None should throw NoSuchElementException"(){
        when: None().get()
        then: thrown(NoSuchElementException)
    }

    def "isEmpty on Some should be false"(){
        expect: !Some(1).isEmpty()
    }

    def "isEmpty on None should be true"(){
        expect: None().isEmpty()
    }

    def "filter on None should produce None"(){
        expect: None().filter({Integer i -> true} as Predicate) == None()
    }

    def "filter Some('a') by 'string !=a' should produce None"(){
        expect: Some('a').filter({String s -> !s.equalsIgnoreCase('a')} as Predicate) == None()
    }

    def "filter Some('a') by 'string ==a' should produce Some('a')"(){
        expect: Some('a').filter({String s -> s.equalsIgnoreCase('a')} as Predicate) == Some("a")
    }

    def "filterNot Some('b') by 'string == a' should return Some('b')"(){
        expect: Some("b").filterNot({String s -> s.equalsIgnoreCase("a")} as Predicate) == Some("b")
    }

    def "filterNot Some('b') by 'string == b' should return None()"(){
        expect: Some("b").filterNot({String s -> s.equalsIgnoreCase("b")} as Predicate) == None()
    }

    def "filterNot on None should produce None"(){
        expect: None().filterNot({it -> true} as Predicate) == None()
    }

    def "map function from int to string on Option(1) should produce Option('1')"(){
        setup: def integerOption = Some(1)
        when:  def stringOption = integerOption.map({it -> it.toString()} as Function)
        then:  stringOption.equals(Some("1"))
    }

    def "map function on None should produce None"(){
        when:
            Option<Integer> opInt = None()
            Option<Integer> opStr = opInt.map({it -> it + 1} as Function)
        then:
            opStr == None()
    }

    def "flatMap function on None should produce None"(){
        when:
        def opInt = None()
        def opStr = opInt.flatMap({it -> Some(it.toString())} as Function)
        then:
        opStr == None()
    }

    def "flatMap function from int to Option<String> on Some(1) should produce Some('1')"(){
        when:
        def opInt = Some(1)
        def opStr = opInt.flatMap({i -> Some(i.toString())} as Function)
        then:
        opStr == Some('1')
    }

    def "Some(2).contains(1) should return 'false'"(){
        expect: !Some(2).contains(1)
    }

    def "Some(2).contains(2) should return 'true'"(){
        expect: Some(2).contains(2)
    }

    def "call contains with any argument on None() should return 'false'"(){
        expect: !None().contains("a") && !None().contains(1) && !None().contains(Some(2))
    }

    def "Some(2).exist(x -> x ==2) should return 'true'"(){
        expect: Some(2).exist({int i -> i.equals(2)} as Predicate)
    }

    def "Some(2).exist(x -> x =='a') should return 'false'"(){
        expect: !Some(2).exist({Integer i -> i.equals("a")} as Predicate<Integer>)
    }

    def "None().exist(x -> true) should retutn 'false'"(){
        expect: !None().exist({s -> true} as Predicate)
    }

    def "Some('2').forall(x -> false) should retutn 'false'"(){
        expect: !Some("2").forall({s -> false} as Predicate)
    }

    def "None().forall(x -> false) should retutn 'true'"(){
        expect: None().forall({s -> false} as Predicate)
    }

    def "Some('2').forall(x -> x == '2') should retutn 'false'"(){
        expect: Some("2").forall({String s -> s.equals("2")} as Predicate)
    }

    def "toList on Some(2) should produce List<Integer>(2)"(){
        expect: Some(2).toList() == [2]
    }

    def "toList on None should produce Empty list"(){
        expect: None().toList() == []
    }

    def "orNull on None() should produce 'null'"(){
        expect: None().orNull() == null
    }

    def "orNull on Some('a') should return 'a'"(){
        expect: Some("2").orNull()== "2"
    }

    def "orElse(2) on Some(1) should return 1"(){
        expect: Some(1).orElse(2) == 1
    }

    def "orElse(1) on None should return 1"(){
        expect: None().orElse(1) == 1
    }

    def "orElseGet(2) on Some(1) should return 1"(){
        expect: Some(1).orElseGet({2} as Supplier) == 1
    }

    def "orElseGet(1) on None should return 1"(){
        expect: None().orElseGet({1} as Supplier) == 1
    }

    def "orElseThrow(NoSuchElementException) on Some(1) should return 1"(){
        expect: Some(1).orElseThrow({throw new NoSuchElementException()} as Supplier) == 1
    }

    def "orElseThrow(Exception) on None should throw NoSuchElementException"(){
        when: None().orElseThrow({throw new Exception()} as Supplier)
        then: thrown(Exception)
    }

    def "two different None should be Equal"(){
        when:
            Option<?> op1 = None()
            Option<?> op2 = None()
        then: op1.equals(op2) && op1.hashCode() == op2.hashCode()
    }

    def "checking that equals and hashCode works fine for Some"(){
        when:
            Option<String> op1 = Some("1")
            Option<String> op2 = Some("1")
            HashMap<Option<String>, Integer> map = new HashMap<>()
            map.put(op1, 1)
        then: map.get(op2) == 1

        when:
            map.put(op2, 2)
        then: map.get(op1) == 2
        then: op1.equals(op2) && op1.hashCode() == op2.hashCode()
    }

    def "For Some(a) or None call method 'foreach' should not throws any exception"(){
        when: Some(1).foreach({it -> println(it)} as Consumer)
        then: noExceptionThrown()
    }
}
