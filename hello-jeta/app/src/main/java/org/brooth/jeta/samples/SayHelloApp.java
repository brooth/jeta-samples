package org.brooth.jeta.samples;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class SayHelloApp {

    @Hello
    String text;

    public void sayHello() {
        System.out.print(text);
    }

    public static void main(String[] args) {
        new SayHelloApp().sayHello();
    }
}
