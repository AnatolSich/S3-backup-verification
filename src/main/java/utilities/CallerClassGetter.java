package utilities;

public final class CallerClassGetter extends SecurityManager {
    private static final CallerClassGetter INSTANCE = new CallerClassGetter();

    private CallerClassGetter() {
    }

    public static Class<?> getCallerClass() {
        return INSTANCE.getClassContext()[1];
    }
}
