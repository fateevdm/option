package option

import spock.lang.Specification
import utils.Function
import utils.Predicate
import utils.Supplier

import static option.None.None
import static option.Some.Some
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
        when: Option<String> stringOption = Some("s")
        then: stringOption.isPresent()
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
        expect: None().filter(new Predicate<Integer>() {
            @Override
            boolean test(Integer o) {
                true
            }
        }) == None()
    }

    def "filter Some('a') by 'string !=a' should produce None"(){
        expect: Some('a').filter(new Predicate<String>() {
            @Override
            boolean test(String s) {
                !s.equalsIgnoreCase("a")
            }
        }) == None()
    }

    def "filter Some('a') by 'string ==a' should produce Some('a')"(){
        expect: Some('a').filter(new Predicate<String>() {
            @Override
            boolean test(String s) {
                s.equalsIgnoreCase("a")
            }
        }) == Some("a")
    }

    def "filterNot Some('b') by 'string == a' should return Some('b')"(){
        expect: Some("b").filterNot(new Predicate<String>() {
            @Override
            boolean test(String s) {
                s.equalsIgnoreCase("a")
            }
        }) == Some("b")
    }

    def "filterNot Some('b') by 'string == b' should return None()"(){
        expect: Some("b").filterNot(new Predicate<String>() {
            @Override
            boolean test(String s) {
                s.equalsIgnoreCase("b")
            }
        }) == None()
    }

    def "filterNot on None should produce None"(){
        expect: None().filterNot(new Predicate<Integer>() {
            @Override
            boolean test(Integer o) {
                true
            }
        }) == None()
    }

    def "map function from int to string on Option(1) should produce Option('1')"(){
        setup:
             Option<Integer> integerOption = Some(1)
        when:
           Option<String> stringOption = integerOption.map(new Function<Integer, String>() {
                @Override
                String apply(Integer o) {
                    o.toString()
                }
            })
        then:
            stringOption.equals(Some("1"))
    }

    def "map function on None should produce None"(){
        when:
            Option<Integer> opInt = None()
            Option<Integer> opStr = opInt.map(new Function<Integer,Integer>() {
                @Override
                Integer apply(Integer o) {
                    (o.intValue() + 1)
                }
            })
        then:
            opStr == None()
    }

    def "flatMap function on None should produce None"(){
        when:
        Option<Integer> opInt = None()
        Option<String> opStr = opInt.flatMap(new Function<Integer,Option<String>>() {
            @Override
            Option<String> apply(Integer o) {
                o == null? None(): Some(o.toString())
            }
        })
        then:
        opStr == None()
    }

    def "flatMap function from int to Option<String> on Some(1) should produce Some('1')"(){
        when:
        Option<Integer> opInt = Some(1)
        Option<String> opStr = opInt.flatMap(new Function<Integer,Option<String>>() {
            @Override
            Option<String> apply(Integer o) {
                Some(o.toString())
            }
        })
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
        expect: Some(2).exist(new Predicate<Integer>() {
            @Override
            boolean test(Integer integer) {
                integer.equals(2)
            }
        })
    }

    def "Some(2).exist(x -> x =='a') should return 'false'"(){
        expect: !Some(2).exist(new Predicate<Integer>() {
            @Override
            boolean test(Integer integer) {
                integer.equals("a")
            }
        })
    }

    def "None().exist(x -> true) should retutn 'false'"(){
        expect: !None().exist(new Predicate<String> (){
            @Override
            public boolean test(String s){
                true
            }
        })
    }

    def "Some('2').forall(x -> false) should retutn 'false'"(){
        expect: !Some("2").forall(new Predicate<String> (){
            @Override
            public boolean test(String s){
                false
            }
        })
    }

    def "None().forall(x -> false) should retutn 'true'"(){
        expect: None().forall(new Predicate<String> (){
            @Override
            public boolean test(String s){
                false
            }
        })
    }

    def "Some('2').forall(x -> x == '2') should retutn 'false'"(){
        expect: Some("2").forall(new Predicate<String> (){
            @Override
            public boolean test(String s){
                s.equals("2")
            }
        })
    }

    def "toList on Some(2) should produce List<Integer>(2)"(){
        when:
            List<Integer> list = Some(2).toList()
        then:
            list == [2]
    }

    def "toList on None should produce Empty list"(){
        when: List<Integer> empty = None().toList()
        then: empty == []
    }

    def "orNull on None() should produce 'null'"(){
        expect: None().orNull() == null
    }

    def "orNull on Some('a') should return 'a'"(){
        expect: Some("2").orNull()== "2"
    }

    def "orElse(2) on Some(1) should return 1"(){
        when:
            Option<Integer> op1 = Some(1)
        then:
            op1.orElse(2) == 1
    }

    def "orElse(1) on None should return 1"(){
        when:
            Option<Integer> op1 = None()
        then:
            op1.orElse(1) == 1
    }

    def "orElseGet(2) on Some(1) should return 1"(){
        when:
        Option<Integer> op1 = Some(1)
        then:
        op1.orElseGet(new Supplier<Integer>() {
            @Override
            Integer get() {
                return 2
            }
        }) == 1
    }

    def "orElseGet(1) on None should return 1"(){
        when:
        Option<Integer> op1 = None()
        then:
        op1.orElseGet(new Supplier<Integer>() {
            @Override
            Integer get() {
                return 1
            }
        }) == 1
    }

    def "orElseThrow(NoSuchElementException) on Some(1) should return 1"(){
        when:
        Option<Integer> op1 = Some(1)
        then:
        op1.orElseThrow(new Supplier<Integer>() {
            @Override
            Integer get() {
                throw new NoSuchElementException()
            }
        }) == 1
    }

    def "orElseThrow(Exception) on None should throw NoSuchElementException"(){
        setup:
        Option<Integer> op1 = None()
        when:
        op1.orElseThrow(new Supplier<Integer>() {
            @Override
            Integer get() {
                throw new Exception()
            }
        })
        then:
            thrown(Exception)
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
}
