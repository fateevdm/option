package try_
import spock.lang.Specification
import utils.FunctionEx
import utils.SupplierX

import static try_.Try.Failure
import static try_.Try.Success

/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 27.01.14
 */
class TrySpec extends Specification{

    def "should return Success for Supplier that produces '1'"(){
        expect: Try.asTry({1} as SupplierX) == Success(1)
    }

    def "should return Failure for Supplier that produces IOException"(){
        expect: Try.asTry({throw new IOException()} as SupplierX)  == Failure(new IOException())
    }


    def "should throw InterruptedException after chain methods call"(){
        when:
            Try.asTry({throw new FileNotFoundException()} as SupplierX).
                    recoverWith({it -> Success(505)} as FunctionEx).
                    map({it -> throw new StackOverflowError()} as FunctionEx).
                    getOrElse({throw new InterruptedException()} as SupplierX)
        then: thrown(InterruptedException)
    }

}
