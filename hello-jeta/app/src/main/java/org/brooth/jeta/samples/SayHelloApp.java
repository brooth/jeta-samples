package org.brooth.jeta.samples;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
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
