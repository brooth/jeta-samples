import com.squareup.javapoet.TypeSpec;
import org.brooth.jeta.apt.RoundContext;
import org.brooth.jeta.apt.processors.AbstractProcessor;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class SayHelloProcessor extends AbstractProcessor {

    public SayHelloProcessor() {
        super(Hello.class);
    }

    @Override
    public boolean process(TypeSpec.Builder builder, RoundContext roundContext) {
        return false;
    }
}
