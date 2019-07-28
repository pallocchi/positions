package com.github.pallocchi.positions.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * Extended version of the {@link org.springframework.beans.BeanUtils} class,
 * to support copying non-null properties from one bean to another.
 *
 * @author Pablo Pallocchi
 */
public class BeanUtils {

    /**
     * Copy the property values of the given source bean into the given target bean,
     * ignoring all the null properties.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     *
     * @param source the source bean
     * @param target the target bean
     * @return the target bean
     * @throws BeansException if the copying failed
     */
    public static <T> T copyNonNullProperties(T source, T target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        return target;
    }

    /**
     * Retrieves all the properties of the given bean which are not null.
     *
     * @param bean the bean
     * @return the not null properties
     */
    private static String[] getNullPropertyNames (Object bean) {

        final Set<String> names = new HashSet<>();
        final BeanWrapper bw = new BeanWrapperImpl(bean);

        for(PropertyDescriptor pd : bw.getPropertyDescriptors()) {

            if (bw.getPropertyValue(pd.getName()) == null) {
                names.add(pd.getName());
            }
        }

        return names.toArray(new String[]{});
    }

}
