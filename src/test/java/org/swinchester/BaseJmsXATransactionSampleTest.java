package org.swinchester;

import org.apache.activemq.broker.BrokerService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

public abstract class BaseJmsXATransactionSampleTest extends CamelSpringTestSupport {

    private BrokerService brokerOne;
    private BrokerService brokerTwo;
//    private TransactionTemplate transactionTemplate;

    private CountDownLatch latch;

    @Before
    @Override
    public void setUp() throws Exception {
        brokerOne = ActiveMQUtil.createAndStartBrokerOne();
        brokerTwo = ActiveMQUtil.createAndStartBrokerTwo();
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        ActiveMQUtil.stopBroker(brokerOne);
        ActiveMQUtil.stopBroker(brokerTwo);
    }

    @Test
    public void moneyShouldBeTransfered() {

        template.sendBody("amqSource:queue:transaction.incoming.one", new Long(100));

        Exchange exchange = consumer.receive("amqTarget:queue:transaction.outgoing.one", 5000);
        assertNotNull(exchange);
    }

    @Test
    public void moneyShouldNotTransfered() {
        template.sendBody("amqSource:queue:transaction.incoming.two", new Long(100));

        Exchange exchange = consumer.receive("amqSource:queue:ActiveMQ.DLQ", 5000);
        assertNotNull(exchange);
    }

    @Test
    public void moneyShouldNotTransfered2() throws Exception {
        template.sendBody("amqSource:queue:transaction.incoming.three", new Long(100));

        Exchange exchange = consumer.receive("amqSource:queue:ActiveMQ.DLQ", 5000);
        assertNotNull(exchange);
    }

    @Test
    public void perfTest() throws Exception {
        // warm up
        latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            template.sendBody("amqSource:queue:transaction.incoming.four", new Long(0));
        }
        latch.await();

        latch = new CountDownLatch(1000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            template.sendBody("amqSource:queue:transaction.incoming.four", new Long(1));
        }
        latch.await();
        long end = System.currentTimeMillis();

        System.out.println("duration: " + (end - start) + "ms");

//        assertEquals(0, DatabaseUtil.queryForLong(transactionTemplate, jdbc, "SELECT balance from account where name = 'foo'"));
//        assertEquals(2000, DatabaseUtil.queryForLong(transactionTemplate, jdbc, "SELECT balance from account where name = 'bar'"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("amqSourceXA:queue:transaction.incoming.one")
                    .transacted("PROPAGATION_REQUIRED")
                    .to("amqTargetXA:queue:transaction.outgoing.one");

                from("amqSourceXA:queue:transaction.incoming.two")
                    .transacted("PROPAGATION_REQUIRED")
                    .throwException(new Exception("forced exception for test"))
                    .to("amqTargetXA:queue:transaction.outgoing.two");

                from("amqSourceXA:queue:transaction.incoming.three")
                    .transacted("PROPAGATION_REQUIRED")
                    .throwException(new Exception("forced exception for test"))
                    .to("amqTargetXA:queue:transaction.outgoing.three");

                from("amqSourceXA:queue:transaction.incoming.four")
                    .transacted("PROPAGATION_REQUIRED")
                    .to("amqTargetXA:queue:transaction.outgoing.four")
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            latch.countDown();
                        }
                    });
            }
        };
    }
}