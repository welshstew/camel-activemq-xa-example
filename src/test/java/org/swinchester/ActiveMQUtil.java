package org.swinchester;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.usage.SystemUsage;
import org.apache.activemq.usage.TempUsage;

public class ActiveMQUtil {

    public static BrokerService createAndStartBrokerOne() throws Exception {
        BrokerService brokerOne = new BrokerService();
        brokerOne.setPersistent(false);
        brokerOne.setUseJmx(true);
        brokerOne.setBrokerName("brokerOne");
        brokerOne.addConnector("tcp://localhost:61616");
        ManagementContext mgmtCtx = new ManagementContext();
        mgmtCtx.setConnectorPort(1077);
        brokerOne.setManagementContext(mgmtCtx);

        SystemUsage systemUsage = new SystemUsage();
        TempUsage tempUsage = new TempUsage();
        tempUsage.setLimit(52428800L);
        systemUsage.setTempUsage(tempUsage);
        brokerOne.setSystemUsage(systemUsage);
        brokerOne.start();

        return brokerOne;
    }

    public static BrokerService createAndStartBrokerTwo() throws Exception {
        BrokerService brokerTwo = new BrokerService();
        brokerTwo.setPersistent(false);
        brokerTwo.setUseJmx(true);
        brokerTwo.setBrokerName("brokerTwo");
        brokerTwo.addConnector("tcp://localhost:62616");
        ManagementContext mgmtCtx = new ManagementContext();
        mgmtCtx.setConnectorPort(1078);
        brokerTwo.setManagementContext(mgmtCtx);
        SystemUsage systemUsage = new SystemUsage();
        TempUsage tempUsage = new TempUsage();
        tempUsage.setLimit(52428800L);
        systemUsage.setTempUsage(tempUsage);
        brokerTwo.setSystemUsage(systemUsage);
        brokerTwo.start();

        return brokerTwo;
    }
    
    public static void stopBroker(BrokerService broker) throws Exception {
        if (broker != null) {
            broker.stop();
        }
    }

}