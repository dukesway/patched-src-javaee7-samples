package org.javaee7.cdi.alternatives.priority;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class ProducerMethodGreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Greeting.class, SimpleGreeting.class, FancyGreeting.class, PriorityGreeting.class, ProducerMethodGreeting.class)
            .addAsManifestResource("beans-empty.xml", "beans.xml");
    }

    @Inject
    Greeting bean;

    @Test
    public void should_bean_be_injected() throws Exception {
        assertThat(bean, is(notNullValue()));
    }

    @Test
    public void should_bean_be_simple() throws Exception {
        // because it has the highest priority from Priority annotated alternatives
        assertThat(bean, instanceOf(SimpleGreeting.class));
    }
}
