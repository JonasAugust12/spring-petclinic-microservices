package org.springframework.samples.petclinic.visits.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MetricConfigTest {

    @Test
    void testMetricsCommonTagsBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(MetricConfig.class);
        MeterRegistryCustomizer<MeterRegistry> bean = context.getBean(MeterRegistryCustomizer.class);
        assertNotNull(bean);
    }

    @Test
    void testTimedAspectBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(MetricConfig.class);
        TimedAspect bean = context.getBean(TimedAspect.class);
        assertNotNull(bean);
    }
}
