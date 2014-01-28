package try_
import spock.lang.Specification
import utils.SupplierX
/**
 * @author: Dmitrii Fateev
 * E-mail: wearing.fateev@gmail.com
 * @since: 27.01.14
 */
class TrySpec extends Specification{

    def "should return Success for Supplier that produces '1'"(){
        expect: Try.asTry(new SupplierX<Integer>() {
            @Override
            Integer get() {
                return 1
            }
        }) == Try.Success(1)
    }

    def "should return Failure for Supplier that produces IOException"(){
        expect: Try.asTry(new SupplierX<String>() {
            @Override
            String get() {
                throw new IOException()
            }
        })  == Try.Failure(new IOException())
    }
}
