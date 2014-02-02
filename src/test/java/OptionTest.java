import option.Option;
import org.junit.Assert;
import org.junit.Test;
import utils.function.Function;
import utils.function.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static option.Option.None;
import static option.Option.Some;

/**
 * User: Dmitrii Fateev
 * Date: 13.01.14
 * E-mail: wearing.fateev@gmail.com
 */
public class OptionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void littleExamplesForOption() {
        Option<String> stringOption = Some("2");
        Option<String> option = None();


        List<Option<String>> options = new ArrayList<Option<String>>() {{
            add(Some("5"));
            add(None());
        }};

        for (Option<String> elem : options) {
            for (String str : elem) {
                System.out.println(str);
            }
        }

        options.add(stringOption);
        options.add(option);

        for (Option<String> elem : options) {
           Option<Integer> filtered =  elem.map(new Function<String, Integer>() {
                @Override
                public Integer apply(String s) {
                    return Integer.valueOf(s);
                }
            })
            .filter(new Predicate<Integer>() {
                @Override
                public boolean test(Integer s) {
                    return s > 4;
                }
            });
            for(int num : filtered) {
                System.out.println("here should print only Some(5), but print: "+filtered);
                Assert.assertEquals(5, num);
            }

        }
        new Function<Option<String>, Option<Integer>>() {
            @Override
            public Option<Integer> apply(Option<String> strings) {
                return strings.map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) {
                        return Integer.valueOf(s);
                    }
                });
            }
        };
        options.stream().map(new java.util.function.Function<Option<String>, Option<Integer>>() {
            @Override
            public Option<Integer> apply(Option<String> strings) {
                return strings.map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) {
                        return Integer.valueOf(s);
                    }
                });
            }
        }).forEach(new Consumer<Option<Integer>>() {
            @Override
            public void accept(Option<Integer> integers) {System.out.println(integers);}
        });

    }

}