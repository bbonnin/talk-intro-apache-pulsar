
import java.util.function.Function;

public class HiFunction
        implements Function<String, String> {

    @Override
    public String apply(String input) {
        return String.format("Hi %s!", input);
    }
}