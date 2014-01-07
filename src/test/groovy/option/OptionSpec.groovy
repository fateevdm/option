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
        then: NoSuchElementException e = thrown()
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
                return true
            }
        }) == None()
    }

    def "filter Some('a') by 'string !=a' should produce None"(){
        expect: Some('a').filter(new Predicate<String>() {
            @Override
            boolean test(String s) {
                return !s.equalsIgnoreCase("a")
            }
        }) == None()
    }

    def "filter Some('a') by 'string ==a' should produce Some('a')"(){
        expect: Some('a').filter(new Predicate<String>() {
            @Override
            boolean test(String s) {
                return s.equalsIgnoreCase("a")
            }
        }) == Some("a")
    }

    def "map function from int to string on Option(1) should produce Option('1')"(){
        setup:
             Option<Integer> integerOption = Some(1)
        when:
           Option<String> stringOption = integerOption.map(new Function<Integer, String>() {
                @Override
                String apply(Integer o) {
                    return o.toString()
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
                    return (o.intValue() + 1)
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
                return o == null? None(): Some(o.toString())
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
                return Some(o.toString())
            }
        })
        then:
        opStr == Some('1')
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
            Exception e = thrown()
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
