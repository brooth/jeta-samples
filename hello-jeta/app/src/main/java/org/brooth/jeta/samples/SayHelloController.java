package org.brooth.jeta.samples;

import org.brooth.jeta.MasterController;
import org.brooth.jeta.metasitory.Metasitory;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class SayHelloController<M> extends MasterController<M, HelloMetacode<M>> {

    public SayHelloController(Metasitory metasitory, M master) {
        super(metasitory, master, Hello.class, false);
    }

    public void setHello() {
        for (HelloMetacode<M> metacode : metacodes)
            metacode.setHello(master);
    }
}
