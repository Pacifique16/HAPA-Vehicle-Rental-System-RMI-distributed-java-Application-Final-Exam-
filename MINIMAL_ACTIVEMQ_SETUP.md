# Minimal ActiveMQ Setup for Embedded Broker

## Essential JARs (Download from Maven Central)

For **SERVER** lib folder only:
1. **activemq-broker-5.18.3.jar** - Core broker functionality
2. **activemq-client-5.18.3.jar** - Client connectivity  
3. **geronimo-jms_1.1_spec-1.1.1.jar** - JMS specification
4. **slf4j-api-1.7.36.jar** - Logging API

## Download Links:
- https://repo1.maven.org/maven2/org/apache/activemq/activemq-broker/5.18.3/activemq-broker-5.18.3.jar
- https://repo1.maven.org/maven2/org/apache/activemq/activemq-client/5.18.3/activemq-client-5.18.3.jar
- https://repo1.maven.org/maven2/org/apache/geronimo/specs/geronimo-jms_1.1_spec/1.1.1/geronimo-jms_1.1_spec-1.1.1.jar
- https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar

## Quick Setup:
1. Download the 4 JAR files above
2. Copy them to: `HAPAVehicleRentalServer26937\lib\`
3. Run your server - it will start embedded ActiveMQ automatically
4. No external ActiveMQ installation needed!

## Verification:
When server starts, you should see:
```
✅ Embedded ActiveMQ Broker started on tcp://localhost:61616
✅ OTP Message Consumer started...
```