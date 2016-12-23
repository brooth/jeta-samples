package com.mycompany.myproject;

import org.brooth.jeta.Provider;
import org.brooth.jeta.collector.ObjectCollectorController;
import org.brooth.jeta.collector.TypeCollectorController;
import org.brooth.jeta.eventbus.BaseEventBus;
import org.brooth.jeta.eventbus.EventBus;
import org.brooth.jeta.eventbus.SubscriberController;
import org.brooth.jeta.eventbus.SubscriptionHandler;
import org.brooth.jeta.inject.*;
import org.brooth.jeta.log.LogController;
import org.brooth.jeta.log.NamedLoggerProvider;
import org.brooth.jeta.metasitory.MapMetasitory;
import org.brooth.jeta.metasitory.Metasitory;
import org.brooth.jeta.observer.ObservableController;
import org.brooth.jeta.observer.ObserverController;
import org.brooth.jeta.observer.ObserverHandler;
import org.brooth.jeta.proxy.ProxyController;
import org.brooth.jeta.util.MultitonController;
import org.brooth.jeta.util.MultitonMetacode;
import org.brooth.jeta.util.SingletonController;
import org.brooth.jeta.util.SingletonMetacode;
import org.brooth.jeta.validate.ValidationController;
import org.brooth.jeta.validate.ValidationException;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author khalidov
 * @version $Id$
 */
public class MetaHelper {

    public static MetaHelper instance = new MetaHelper();

    private final Metasitory metasitory;
    private final NamedLoggerProvider<DumbLogger> loggerProvider;
    private final MetaScope<AppScope> applicationScope;
    private final EventBus bus;

    public MetaHelper() {
        loggerProvider = new NamedLoggerProvider<DumbLogger>() {
            @Override
            public DumbLogger get(String name) {
                return new DumbLogger(name);
            }
        };

        metasitory = new MapMetasitory("com.mycompany.myproject");
        applicationScope = new MetaScopeController<>(metasitory, new AppScope())
            .get();
        bus = new BaseEventBus();
    }

    public static MetaHelper getInstance() {
        return instance;
    }

    public static void createLoggers(Object master) {
        new LogController(getInstance().metasitory, master)
            .createLoggers(getInstance().loggerProvider);
    }

    public static void inject(Object master) {
        inject(getInstance().applicationScope, master);
    }

    public static void inject(MetaScope<?> scope, Object master) {
        new InjectController(getInstance().metasitory, master).inject(scope);
    }

    public static void injectStatic(Class<?> masterClass) {
        injectStatic(getInstance().applicationScope, masterClass);
    }

    public static void injectStatic(MetaScope<?> scope, Class<?> masterClass) {
        new StaticInjectController(getInstance().metasitory, masterClass)
            .inject(scope);
    }

    public static SubscriptionHandler registerSubscriber(Object master) {
        return new SubscriberController<>(getInstance().metasitory, master)
                .registerSubscriber(getInstance().bus);
    }

    public static void createObservable(Object master) {
        new ObservableController<>(getInstance().metasitory, master)
            .createObservable();
    }

    public static ObserverHandler registerObserver(Object observer, Object observable) {
        return new ObserverController<>(getInstance().metasitory, observer)
                .registerObserver(observable);
    }

    public static void validate(Object master) throws ValidationException {
        new ValidationController(getInstance().metasitory, master).validate();
    }

    public static List<String> validateSafe(Object master) {
        return new ValidationController(getInstance().metasitory, master)
            .validateSafe();
    }

    public static void createProxy(Object master, Object real) {
        new ProxyController(getInstance().metasitory, master).createProxy(real);
    }

    public static List<Class<?>> collectTypes(Class<?> masterClass, Class<? extends Annotation> annotationClass) {
        return new TypeCollectorController(getInstance().metasitory, masterClass)
                .getTypes(annotationClass);
    }

    public static List<Provider<?>> collectObjects(Class<?> masterClass, Class<? extends Annotation> annotationClass) {
        return new ObjectCollectorController(getInstance().metasitory, masterClass)
            .getObjects(annotationClass);
    }

    public static <M> SingletonMetacode<M> getSingleton(Class<M> masterClass) {
        return new SingletonController<>(getInstance().metasitory, masterClass)
            .getMetacode();
    }

    public static <M> MultitonMetacode<M> getMultiton(Class<M> masterClass) {
        return new MultitonController<>(getInstance().metasitory, masterClass)
            .getMetacode();
    }
}
