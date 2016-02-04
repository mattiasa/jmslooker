package com.cantemo.mattiasa.jmslooker;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;

import javax.naming.InitialContext;
import com.sun.messaging.jmq.jmsclient.MessageImpl;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.QueueSession;
import javax.jms.QueueReceiver;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueBrowser;
import javax.jms.Connection;
import javax.jms.MessageConsumer;

public class Receiver
{

    /*
    This code is heavily based on the example code for a jms client
     */
    public static void main(String[] args) throws Exception
    {
        ConnectionFactory cf = null;
        Connection connection = null;

        Scanner sc = new Scanner(System.in);

        String serverHost = "localhost";
        String serverPort = "7676";
        String serverUser = "admin";
        String serverPassword = "admin";


        cf = new com.sun.messaging.ConnectionFactory();

        ((com.sun.messaging.ConnectionFactory) cf).setProperty(
                                                               com.sun.messaging.ConnectionConfiguration.imqBrokerHostName,
                                                               serverHost);
        ((com.sun.messaging.ConnectionFactory) cf).setProperty(
                                                               com.sun.messaging.ConnectionConfiguration.imqBrokerHostPort,
                                                               String.valueOf(serverPort));


        connection = cf.createConnection(serverUser, serverPassword);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("IndexQueue");

        QueueBrowser browser = session.createBrowser(queue);

        Enumeration e = browser.getEnumeration();


        int numMsgs = 0;

        // count number of messages
        while (e.hasMoreElements()) {
            System.out.println("Getting the next message off the queue. Press return to write to file or ^C to abort.");
            sc.nextLine();

            Message message = (Message) e.nextElement();
            String messageId = message.getJMSMessageID();
            System.out.println(message);

            //            message.acknowledge();
            MessageImpl m2 = (MessageImpl)message;
            // m2.dump(System.out);
            byte [] body = m2.getMessageBody();
            FileOutputStream fos = null;
            String filename = messageId + ".txt";
            try {
                fos = new FileOutputStream(filename);
                fos.write(body);

            }
            catch (IOException ex)
            {
                System.err.println("Error: " + ex.getMessage());
            }
            finally
            {
                if(fos != null) {
                    fos.close();
                }
            }

            System.out.println("Wrote the message to " + filename);
            System.out.println();

            // System.out.write(body);
            //message.acknowledge();
            numMsgs++;
        }
        System.out.println(queue + " has " + numMsgs + " messages");

        browser.close();
        session.close();
        connection.close();
    }
}
