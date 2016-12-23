package com.mycompany.myproject;

/**
 * @author khalidov
 * @version $Id$
 */
public class DumbLogger {

    private final String name;

    public DumbLogger(String name) {
        this.name = name;
    }

    private void msg(String level, String msg) {
        System.out.println(String.format("%s [%s]: %s", level, name, msg));
    }

    public void debug(String msg) {
        msg("DEBUG", msg);
    }

    public void info(String msg) {
        msg("INFO", msg);
    }

    public void warn(String msg) {
        msg("WARN", msg);
    }

    public void error(String msg) {
        msg("ERROR", msg);
    }
}
