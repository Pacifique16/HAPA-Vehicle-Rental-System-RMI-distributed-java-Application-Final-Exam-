# ActiveMQ Setup Guide for Hospital Management System

## Step 1: Download ActiveMQ JARs

Download these JAR files and add them to both server and client `lib` folders:

### Required JARs:
1. **activemq-client-5.18.3.jar**
2. **activemq-broker-5.18.3.jar** 
3. **activemq-kahadb-store-5.18.3.jar**
4. **activemq-protobuf-1.1.jar**
5. **geronimo-j2ee-management_1.1_spec-1.0.1.jar**
6. **geronimo-jms_1.1_spec-1.1.1.jar**
7. **slf4j-api-1.7.36.jar**

### Download Links:
- Go to: https://mvnrepository.com/artifact/org.apache.activemq/activemq-client/5.18.3
- Download each JAR individually or use Maven/Gradle

## Step 2: Install ActiveMQ Broker

### Option A: Download Apache ActiveMQ
1. Download from: https://activemq.apache.org/components/classic/download/
2. Extract to a folder (e.g., `C:\apache-activemq-5.18.3`)
3. Run: `bin\activemq.bat start` (Windows) or `bin/activemq start` (Linux/Mac)
4. Web Console: http://localhost:8161/admin (admin/admin)

### Option B: Use Embedded Broker (Simpler)
Create this class in your server's util package:

```java
package util;

import org.apache.activemq.broker.BrokerService;

public class EmbeddedActiveMQBroker {
    private static BrokerService broker;
    
    public static void startBroker() {
        try {
            broker = new BrokerService();
            broker.addConnector("tcp://localhost:61616");
            broker.setPersistent(false);
            broker.start();
            System.out.println("Embedded ActiveMQ Broker started on tcp://localhost:61616");
        } catch (Exception e) {
            System.err.println("Failed to start embedded broker: " + e.getMessage());
        }
    }
    
    public static void stopBroker() {
        try {
            if (broker != null) {
                broker.stop();
            }
        } catch (Exception e) {
            System.err.println("Error stopping broker: " + e.getMessage());
        }
    }
}
```

## Step 3: Update Server.java (Already Done)

The Server.java has been updated to start the MessageConsumer automatically.

## Step 4: Test the Integration

1. Start your PostgreSQL database
2. Start ActiveMQ broker (or it will start automatically if using embedded)
3. Run the Server application
4. Run the Client application
5. Try to login - you should see OTP messages in the server console

## Step 5: Verify Message Flow

When a user requests OTP:
1. OTPServiceImpl generates OTP
2. MessageProducer sends message to "otp.notifications" queue
3. MessageConsumer receives message and sends email via GmailSender
4. User receives OTP email

## Troubleshooting

### If you get ClassNotFoundException:
- Ensure all ActiveMQ JARs are in the lib folder
- Check your IDE's classpath configuration

### If connection fails:
- Verify ActiveMQ broker is running on port 61616
- Check firewall settings

### If emails don't send:
- Verify GmailSender configuration
- Check email credentials in EmailConfig

## Current Status

✅ MessageProducer.java - Created
✅ MessageConsumer.java - Created  
✅ OTPServiceImpl.java - Updated to use message broker
✅ Server.java - Updated to start message consumer
❌ ActiveMQ JARs - Need to be added to lib folders
❌ ActiveMQ Broker - Need to install and run

## Next Steps

1. Download and add the required JAR files
2. Install and start ActiveMQ broker
3. Test the complete flow