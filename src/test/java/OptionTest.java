import option.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static option.Option.None;
import static option.Option.Some;

/**
 * User: Dmitrii Fateev
 * Date: 13.01.14
 * E-mail: wearing.fateev@gmail.com
 */
public class OptionTest {

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
           Option<Integer> filtered =  elem.map(Integer::valueOf)
            .filter(s -> s > 4);
            for(int num : filtered) {
                System.out.println("here should print only Some(5), but print: "+filtered);
                Assert.assertEquals(5, num);
            }

        }

        options.stream().map(el -> el.map(Integer::valueOf)).forEach(System.out::println);

    }

}