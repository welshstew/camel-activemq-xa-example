# Camel AMQ XA Example

Using atomikos and ActiveMQ in a standalone Spring XML.
Inspired from [GitHub](https://github.com/muellerc/camel-in-transaction/blob/master/src/test/resources/META-INF/spring/JmsAndJdbcXATransactionSampleWithAtomikosTest-context.xml)

By default with the redelivery policy of 0 the transaction management is pushed back to the JMS implementation (ActiveMQ in this case).  2 of the 10 messages will always fail and be added to the ActiveMQ.DLQ

