package org.brooth.jeta.samples;

import org.brooth.jeta.metasitory.MapMetasitory;
import org.brooth.jeta.metasitory.Metasitory;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
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
