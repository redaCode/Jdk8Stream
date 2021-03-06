package com.reda.introduction;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {
    private static String queueName = "hello";
    public static void main(String[] args) {
        //在consumer同样声明queue，因为消费者可能先与生产着运行，必须保证有queue
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        try {
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            channel.queueDeclare(queueName,true,false,false,null);
            System.out.println("Waiting Message");
            //consumer对象提供一个回调函数，他的作用是缓存到接受到的结果，知道我们调用到他。
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            //等待接受信息
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
