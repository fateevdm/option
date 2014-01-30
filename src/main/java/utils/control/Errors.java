package utils.control;

/**
 * User: Dmitrii Fateev
 * Date: 25.01.14
 * E-mail: wearing.fateev@gmail.com
 */
public final class Errors {

    private Errors(){}

    public static boolean isNonFatal(Throwable t){
        if (t instanceof StackOverflowError) return true;
        else return !(t instanceof VirtualMachineError || t instanceof ThreadDeath || t instanceof InterruptedException || t instanceof LinkageError);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void throwAny(Throwable e) throws E {
        throw (E)e;
    }

    public static boolean isFatal(Throwable t){
        return !isNonFatal(t);
    }

    public static void throwAsUnchecked(Throwable t){
        Errors.<RuntimeException>throwAny(t);
    }
}
