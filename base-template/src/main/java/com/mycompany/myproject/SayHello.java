package com.mycompany.myproject;

import org.brooth.jeta.log.Log;

public class SayHello {

    @Log
    DumbLogger logger;

    public SayHello() {
        MetaHelper.createLoggers(this);
    }

    public void sayHello() {
        logger.info("Hello!");
    }

    public static void main(String[] args) {
        new SayHello().sayHello();
    }
}
