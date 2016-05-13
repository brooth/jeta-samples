import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.brooth.jeta.apt.MetacodeContext;
import org.brooth.jeta.apt.RoundContext;
import org.brooth.jeta.apt.processors.AbstractProcessor;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class SayHelloProcessor extends AbstractProcessor {

    public SayHelloProcessor() {
        super(Hello.class);
    }

    @Override
    public boolean process(TypeSpec.Builder builder, RoundContext roundContext) {
        MetacodeContext context = roundContext.metacodeContext();
        ClassName masterClassName = ClassName.get(context.masterElement());
        builder.addSuperinterface(ParameterizedTypeName.get(
                ClassName.get(HelloMetacode.class), masterClassName));

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("applyHello")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(masterClassName, "master");

        for (Element element : roundContext.elements()) {
            String fieldName = element.getSimpleName().toString();
            methodBuilder.addStatement("master.$L = \"Hello, Jeta\"", fieldName);
        }

        builder.addMethod(methodBuilder.build());
        return false;
    }
}
