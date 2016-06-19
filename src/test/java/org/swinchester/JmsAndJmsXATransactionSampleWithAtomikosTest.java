package org.swinchester;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JmsAndJmsXATransactionSampleWithAtomikosTest extends BaseJmsXATransactionSampleTest {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/JmsAndJmsXATransactionSampleWithAtomikosTest-context.xml");
    }
}