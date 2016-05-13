How to create custom processors, step-by-step tutorial
------

### Step 1: `Hello, World` project

Let's create a simple `gradle` project with one module `app` and a single class `SayHelloApp`. This class writes `Hello, World!` to standard output.

![SayHelloApp](http://i.imgur.com/pMyJ0Cx.png)

In this tutorial we'll create `Hello` annotation and will be providing `Hello, Jeta!` string to fields with this annotation.

### Step 2: `common` module

Now we need a module which will be accessible in `app` module and in `apt` module which we'll create shortly. In `common` module we'll create two classes - `Hello` annotation and `HelloMetacode` interface:

![common module](http://i.imgur.com/Yxn9bZn.png)

### Step 3: `apt` module

`apt` - is a module in which we create all the required for code generation classes. For this tutorial we only need to create a processor that handles our `Hello` annotation.

![apt module](http://i.imgur.com/NWTCdq2.png)

Note that this module depends on `common` so we used `Hello` annotation as a parameter for super constructor. By doing that we're saying to `Jeta` that we need all elements annotated with this annotation. It also depends on `jeta-apt` in order to be used by `Jeta` for code generating.

### Step 4: Processor

We created `SayHelloProcessor` but now it does nothing. Let's put some logic in it. The idea here is to generate java code that will be setting `Hello, Jeta` string to the fields annotated with `Hello`.

Note that `Jeta` uses `JavaPoet` to create java source code. It's really great framework by `Square`.  Please, check it out [here](https://github.com/square/javapoet).

First, we need our [metacode](http://jeta.brooth.org/guide/at-runtime.html) to implement `HelloMetacode`. To do this we'll add super interface to the `builder`:

```java
MetacodeContext context = roundContext.metacodeContext();
ClassName masterClassName = ClassName.get(context.masterElement());
builder.addSuperinterface(ParameterizedTypeName.get(
        ClassName.get(HelloMetacode.class), masterClassName));
```

Secondly, implement `HelloMetacode` by creating `void setHello(M master)` method:

```java
MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("setHello")
    .addAnnotation(Override.class)
    .addModifiers(Modifier.PUBLIC)
    .returns(void.class)
    .addParameter(masterClassName, "master");
```

Finally, we'll handle all elements annotated with `Hello` that `Jeta` passes in `process` method via `roundContext` parameter:

```java
for (Element element : roundContext.elements()) {
    String fieldName = element.getSimpleName().toString();
    methodBuilder.addStatement("master.$L = \"Hello, Jeta\"", fieldName);
}
```

Here is the complete `SayHelloProcessor` listing:

```java
package org.brooth.jeta.samples.apt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.brooth.jeta.apt.MetacodeContext;
import org.brooth.jeta.apt.RoundContext;
import org.brooth.jeta.apt.processors.AbstractProcessor;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

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

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("setHello")
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
```

### Step 5: Metacode

All the required for code generating classes are created and we're ready to try. But first let's add `jeta.properties` file in order to config `Jeta`. You can find more details about this file [on this page](http://jeta.brooth.org/guide/config.html). The file should be located in the root package. For out tutorial its content would be:

```properties
metasitory.package=org.brooth.jeta.samples
processors.add=org.brooth.jeta.samples.apt.SayHelloProcessor
```

Next, let's modify `SayHelloApp`, instead of initializing `text` field we'll put `Hello` annotation on it:

 ```java
public class SayHelloApp {
     @Hello
     String text;
}
 ```

 Now we ready to give it a try. so run this `gradle` command in your console:

 ```
 gradlew assemble
 ```

 If there no problems so far we'll see `SayHelloApp_Metacode` file under `app/build` directory:

 ![SayHelloApp_Metacode](http://i.imgur.com/29RFLyL.png)
