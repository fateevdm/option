package try_

import spock.lang.Specification
import utils.function.exceptional.FunctionEx
import utils.function.exceptional.SupplierEx

import static try_.Try.failure
import static try_.Try.success

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 27.01.14
 */
class TrySpec extends Specification {

    def "should return Success for Supplier that produces '1'"() {
        expect: Try.asTry({ 1 } as SupplierEx) == success(1)
    }

    def "should return Failure for Supplier that produces IOException"() {
        expect: Try.asTry(
                { throw new IOException()} as SupplierEx
       ) == failure(new IOException())
    }


    def "should throw InterruptedException after chain methods call"(){
        when:
            Try.asTry({throw new FileNotFoundException()} as SupplierEx).
                    recoverWith({it -> 5} as FunctionEx).
                    map({it -> throw new StackOverflowError()} as FunctionEx).
                    getOrElse({throw new InterruptedException()} as SupplierEx)
        then: thrown(InterruptedException)
    }

}
