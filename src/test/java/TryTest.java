import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;
import try_.Try;
import utils.function.exceptional.FunctionEx;
import utils.function.exceptional.SupplierEx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static try_.Try.Success;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 31.01.14
 */
public class TryTest {

    @Test(expected = InterruptedException.class)
    public void testAsExampleForTry() {
        System.out.println(Try.asTry(new SupplierEx<String>() {
            @Override
            public String get() throws IOException {
                throw new IOException();
            }
        }).recover(new FunctionEx<Throwable, String>() {
            @Override
            public String apply(Throwable input) throws Exception {
                return input.toString();
            }
        }));

        Success(5);

        Assert.assertSame(55, Try.asTry(new SupplierEx<File>() {
            @Override
            public File get() throws FileNotFoundException {
                throw new FileNotFoundException();
            }
        }).recoverWith(new FunctionEx<Throwable, Try<File>>() {
            @Override
            public Try<File> apply(Throwable input) {
                return Success(getDefaultFile());
            }
        }).flatMap(new FunctionEx<File, Try<List<String>>>() {
            @Override
            public Try<List<String>> apply(File input) throws IOException {
                return Success(Files.readLines(input, Charsets.UTF_8));
            }
        }).map(new FunctionEx<List<String>, List<Integer>>() {
            @Override
            public List<Integer> apply(List<String> lines) throws Exception {
                return Lists.transform(lines, new Function<String, Integer>() {
                    @Override
                    public Integer apply(String input) {
                        return Integer.valueOf(input);
                    }
                });
            }
        }).map(new FunctionEx<List<Integer>, Integer>() {
            @Override
            public Integer apply(List<Integer> input) throws Exception {
                int sum = 0;
                for (int x : input) sum += x;
                return sum;
            }
        }).getOrElse(new SupplierEx<Integer>() {
            @Override
            public Integer get() throws Exception{
                return 505;
            }
        }));

        Try.asTry(new SupplierEx<Object>() {
            @Override
            public Object get() throws InterruptedException {
                throw new InterruptedException();
            }
        });
    }

    private File getDefaultFile() {
        return new File("src/test/resources/numbers.txt");
    }
}
