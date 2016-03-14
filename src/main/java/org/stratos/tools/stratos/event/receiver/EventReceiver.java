package org.stratos.tools.stratos.event.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.subscribe.EventSubscriber;

/**
 * Event receiver
 */
public class EventReceiver implements Runnable {
    private static final Log log = LogFactory.getLog(EventReceiver.class);

    private String id;
    private String topicName;
    private EventSubscriber topicSubscriber;

    public EventReceiver(String id, String topicName) {
        this.id = id;
        this.topicName = topicName;
    }

    @Override
    public void run() {
        topicSubscriber = new EventSubscriber(topicName, new org.apache.stratos.messaging.broker.subscribe.MessageListener() {

            @Override
            public void messageReceived(org.apache.stratos.messaging.domain.Message message) {
                try {
                    String messageText = new String(message.getText());
//                    TextMessageEvent event = (TextMessageEvent) (new JsonMessage(messageText, TextMessageEvent.class)).getObject();
//                    System.out.println(event.getMessage());
                    log.info("Message received: " + messageText.toString());
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
        topicSubscriber.run();

        log.info(topicName + " event message receiver thread started");

    }

    public String getId() {
        return id;
    }

    public void terminate() {
        topicSubscriber.terminate();
    }
}
