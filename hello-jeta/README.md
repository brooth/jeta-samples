How to create custom processors, step-by-step tutorial
------

### Step 1: `Hello, World` project

For this tutorial let's create a simple `Gradle` project with one module `app` and with a single class `SayHelloApp`. This class writes `Hello, World!` to standard output.

![SayHelloApp](http://i.imgur.com/pMyJ0Cx.png)

For the illustration we are going to create `Hello` annotation that sets `Hello, Jeta!` string to the annotated fields.

### Step 2: `common` module

First, we need a module that will be accessible in `app` and  `apt` (will create shortly) modules. In `common` module we need two classes - `Hello` annotation and `HelloMetacode` interface:

![common module](http://i.imgur.com/Yxn9bZn.png)

### Step 3: `apt` module

`apt` - is a module in which we'll create all the required for code generation classes. For this tutorial we need a processor that will be handling our `Hello` annotation.

![apt module](http://i.imgur.com/NWTCdq2.png)

Note that this module depends on `common` module so we used `Hello` annotation as a parameter for the super constructor. By doing that we're saying to `Jeta` that we need all elements annotated with given type. The module also depends on `jeta-apt` in order to get access to `Jeta` classes.

### Step 4: Processor

We created `SayHelloProcessor` but now it does nothing. Let's add some logic in it. The idea here is to generate java code that sets `Hello, Jeta` string to the fields annotated with `Hello`.

Note that `Jeta` uses `JavaPoet` to create java source code. It's really great framework by `Square`.  Please, check it out on [GitHub](https://github.com/square/javapoet).

First, we need that our [metacode](http://jeta.brooth.org/guide/at-runtime.html) has implemented `HelloMetacode`. To do that we'll add super interface to the `builder`:

```java
MetacodeContext context = roundContext.metacodeContext();
ClassName masterClassName = ClassName.get(context.masterElement());
builder.addSuperinterface(ParameterizedTypeName.get(
        ClassName.get(HelloMetacode.class), masterClassName));
```

Next, implement `HelloMetacode` by creating `void setHello(M master)` method:

```java
MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("setHello")
    .addAnnotation(Override.class)
    .addModifiers(Modifier.PUBLIC)
    .returns(void.class)
    .addParameter(masterClassName, "master");
```

Finally, we'll create the statement for each element annotated with `Hello`, that `Jeta` passes in `process` method via `roundContext` parameter:

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

All the required for code generating classes are created and we're ready to try. But first, we need to add `jeta.properties` file in order to configurate `Jeta`. You can find more details about this file [on this page](http://jeta.brooth.org/guide/config.html). The file should be located in the root package. For our tutorial its content would be:

```properties
metasitory.package=org.brooth.jeta.samples
processors.add=org.brooth.jeta.samples.apt.SayHelloProcessor
```

Next, modify `SayHelloApp`. Instead of initializing `text` field we'll put `Hello` annotation on it:

 ```java
public class SayHelloApp {
     @Hello
     String text;
}
 ```

 Now we're ready to generate metacode. Run next command in your console:

 ```
 ./gradlew assemble
 ```

 If there is no problems so far, we'll see `SayHelloApp_Metacode` file under `app/build` directory:

 ![SayHelloApp_Metacode](http://i.imgur.com/29RFLyL.png)

 ### Step 5: Controller

 [Controllers](http://jeta.brooth.org/guide/at-runtime.html) are the classes that applies metacode to the [masters](http://jeta.brooth.org/guide/at-runtime.html). Let's create one  for `HelloMetacode` in `app` module:

 ```java
 package org.brooth.jeta.samples;

 import org.brooth.jeta.MasterController;
 import org.brooth.jeta.metasitory.Metasitory;

 public class SayHelloController<M> extends MasterController<M, HelloMetacode<M>> {

     public SayHelloController(Metasitory metasitory, M master) {
         super(metasitory, master, Hello.class, false);
     }

     public void setHello() {
         for (HelloMetacode<M> metacode : metacodes)
             metacode.setHello(master);
     }
 }
 ```

### Step 6: MetaHelper

`MetaHelper` is a simple static-helper class. You shouldn't use it in your project if you are not comfortable with static helpers. You can read more details about this class on [this page](http://jeta.brooth.org/guide/meta-helper.html).

Anyway, let's create `MetaHelper` in `app` module:

```java
package org.brooth.jeta.samples;

import org.brooth.jeta.metasitory.MapMetasitory;
import org.brooth.jeta.metasitory.Metasitory;

public class MetaHelper {

    private static MetaHelper instance;
    private final Metasitory metasitory;

    public static MetaHelper getInstance() {
        if (instance == null)
            instance = new MetaHelper("org.brooth.jeta.samples");
        return instance;
    }

    private MetaHelper(String metaPackage) {
        metasitory = new MapMetasitory(metaPackage);
    }

    public static void setHello(Object master) {
        new SayHelloController<>(getInstance().metasitory, master).setHello();
    }
}
```

### Step 7: Usage

The last step - we need to invoke our `MetaHelper`. Here is the complete listing of `SayHelloApp`:

```java
package org.brooth.jeta.samples;

public class SayHelloApp {

    @Hello
    String text;

    public SayHelloApp() {
        MetaHelper.setHello(this);
    }

    public void sayHello() {
        System.out.print(text);
    }

    public static void main(String[] args) {
        new SayHelloApp().sayHello();
    }
}
```

Finally, we can run `main` method of `SayHelloApp`. In the console we'll see

```
Hello, Jeta
```

Happy code-generating! :)
