package com.mycompany.myproject;

import org.brooth.jeta.log.Log;

/**
 * @author khalidov
 * @version $Id$
 */
public class SayHello {

    @Log
    DumbLogger logger;

    public SayHello() {
        MetaHelper.createLoggers(this);
    }

    public void sayHello() {
        logger.info("Hello!");
    }

    /**
     * !!! Run in console: "./gradlew assemble" in order to create metacode !!!
     */
    public static void main(String[] args) {
        new SayHello().sayHello();
    }
}
