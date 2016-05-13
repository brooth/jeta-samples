/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class SayHelloApp {

    String text = "Hello, World!";

    public void sayHello() {
        System.out.print(text);
    }

    public static void main(String[] args) {
        new SayHelloApp().sayHello();
    }
}
