package de.larmic.butterfaces.test.simple;

import java.io.PrintStream;

/**
 * Created by larmic on 03.09.14.
 */
public class Greeter {

    public void greet(PrintStream to, String name) {
        to.println(createGreeting(name));
    }

    public String createGreeting(String name) {
        return "Hello, " + name + "!";
    }

}
