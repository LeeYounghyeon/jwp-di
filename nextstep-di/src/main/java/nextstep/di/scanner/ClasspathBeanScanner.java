package nextstep.di.scanner;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.BeanFactoryUtils;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

public class ClasspathBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);
    private static final Class[] COMPONENTS = {Controller.class, Service.class, Repository.class};


    private BeanFactory beanFactory;
    private Reflections reflections;

    public ClasspathBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void doScan(Object... basePackage) {
        reflections = new Reflections(basePackage);
        beanFactory.addPreInstanticateClazz(getBeans());
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, Constructor> getBeans() {
        Set<Class<?>> typesAnnotatedWith = getTypesAnnotatedWith(COMPONENTS);
        Map<Class<?>, Constructor> beans = Maps.newHashMap();
        for (Class<?> clazz : typesAnnotatedWith) {
            beans.put(clazz, BeanFactoryUtils.getInjectedConstructor(clazz));
        }
        return beans;
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
