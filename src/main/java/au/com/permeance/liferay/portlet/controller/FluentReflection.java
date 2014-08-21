package au.com.permeance.liferay.portlet.controller;

import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.util.ReflectionUtil;

/**
 * Copyright (C) 2014 Permeance Technologies
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * @author tim.myerscough
 *
 */
public class FluentReflection {
    public static BeanLocatorReflection within(final BeanLocator b) {
        return new BeanLocatorReflection() {

            @Override
            public FluentReflectionInvokation on(final String beanName) {
                Object bean = b.locate(beanName);
                return new ReflectionInvokation(bean);
            }

        };
    }

    public static FluentReflectionInvokation on(final Object object) {
        return new ReflectionInvokation(object);
    }

    private static final class ReflectionInvokation implements FluentReflectionInvokation {
        private final Object target;

        private ReflectionInvokation(final Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(final String methodName, final Object... args) {

            Class<?>[] parameterTypes = ReflectionUtil.getParameterTypes(args);

            Method method;
            try {

                method = ReflectionUtils.findMethod(target.getClass(), methodName, parameterTypes);
                return method.invoke(target, args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public interface BeanLocatorReflection {

        FluentReflectionInvokation on(final String className);
    }

    public interface FluentReflectionInvokation {

        Object invoke(final String string, final Object... args);

    }

}
