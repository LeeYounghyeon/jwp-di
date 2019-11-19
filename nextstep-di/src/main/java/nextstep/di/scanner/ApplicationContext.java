package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;

import java.util.Map;

public class ApplicationContext {
    private ClasspathBeanScanner cpbs;
    private ConfigurationBeanScanner cbs;
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?> configClazz) {
        this.beanFactory = new BeanFactory();
        this.cpbs = new ClasspathBeanScanner(beanFactory);
        this.cbs = new ConfigurationBeanScanner(beanFactory);
        init(configClazz);
    }

    private void init(Class<?> configClazz) {
        cbs.register(configClazz);
        beanFactory.initialize();
    }

    public void register(Object... basePackage) {
        cpbs.register(basePackage);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Map<Class<?>, Object> getController() {
        return beanFactory.getController();
    }
}
