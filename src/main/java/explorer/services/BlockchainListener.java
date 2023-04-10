package explorer.services;

import explorer.events.AddressesIndexedEvent;
import explorer.events.BlockHashReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.logging.Logger;

@Service
public class BlockchainListener implements ApplicationListener<AddressesIndexedEvent> {

    @Override
    public void onApplicationEvent(AddressesIndexedEvent event) {
        startListener();
    }

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    Logger logger = Logger.getLogger(BlockchainListener.class.getName());

    @Value("${bitcoin.zmq.url}")
    String bitcoin_zmq_rul;

    public void startListener() {

        ZMQ.Context context = ZMQ.context(1);
        // Connect our subscriber socket
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        // Synchronize with the publisher
        subscriber.connect(bitcoin_zmq_rul);
        //subscriber.subscribe("rawblock".getBytes());
        //subscriber.subscribe("rawtx".getBytes());
        //subscriber.subscribe("hashblock".getBytes());
        subscriber.subscribe("".getBytes());

        logger.info("SUBSCRIBED TO BITCOIN NODE ZMQ LISTENING FOR MESSAGES");
        while (true) {

            ZMsg zMsg = ZMsg.recvMsg(subscriber);
            int messageNumber = 0;
            String messageType = "";
            for (ZFrame f : zMsg) {
                byte[] bytes = f.getData();
                if (messageNumber == 0) {
                    messageType = new String(bytes);
                } else if (messageNumber == 1) {

                    String message = Converter.bin2hex(bytes);

                    logger.info("MESSAGE RECEIVED :" + messageType + " : " + message);

                    if (messageType.equals("hashblock")) {
                        applicationEventPublisher.publishEvent( new BlockHashReceivedEvent(this, message));
                    }
                }
                messageNumber++;
            }
        }

    }
}


