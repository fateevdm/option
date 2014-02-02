import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Test;
import try_.Try;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 31.01.14
 */
public class TryTest {

    @Test(expected = InterruptedException.class)
    public void testAsExampleForTry() {
        System.out.println(Try.asTry(() -> {
            throw new IOException();
        }).recover(Throwable::toString));

        Try.asTry(this::loadFile).
                recoverWith(t -> Try.asTry(this::getDefaultFile)).
                flatMap(f -> Try.asTry(()-> Files.readLines(f,Charsets.UTF_8))).<List<Integer>>
                map(xs -> Lists.transform(xs, Integer::valueOf)).
                map(ns -> ns.stream().reduce(0, (f, s) -> f + s )).
                foreach(System.out::println);
        Try.asTry(() -> {
            throw new InterruptedException();
        });

    }

    private File loadFile() throws FileNotFoundException {
        throw new FileNotFoundException();
    }

    private File getDefaultFile() {
        return new File("src/test/resources/numbers.txt");
    }
}
