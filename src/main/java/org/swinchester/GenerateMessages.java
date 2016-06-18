package org.swinchester;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;

/**
 * Created by swinchester on 18/06/16.
 */
public class GenerateMessages implements Processor {

    private int messsageCount = 10;


    @Override
    public void process(Exchange exchange) throws Exception {

        ProducerTemplate pt = exchange.getContext().createProducerTemplate();

        for(int i=0; i < messsageCount; i++){
            pt.sendBody("amqSource:queue:jms-xa-demo.in", "hello world " + (i+1));
        }

    }
}
