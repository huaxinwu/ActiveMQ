/**
 * wxh Inc.
 * Copyright (c) 2006-2017 All Rights Reserved.
 */
package com.wxh.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * activemq ----sender发送者(生产者)
 * @author wxh
 * @version $Id: Sender.java, v 0.1 2017年7月4日 下午4:15:26 wxh Exp $
 */
public class Sender {

    /** 发送消息数量 */
    private static final int SEND_NUMBER = 3;

    public static void main(String[] args) {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁
        Destination destination;
        // MessageProducer：消息发送者(生产者)
        MessageProducer product;
        // TextMessage message;
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
            ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 获取session主要参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue("FirstQueue");
            // 得到消息生成者【发送者】
            product = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            product.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
            sendMessage(session, product);
            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据一个会话和消息发送者，发送消息
     * @param session
     * @param messageProducer
     * @throws JMSException 
     */
    public static void sendMessage(Session session, MessageProducer product) throws JMSException {
        for (int i = 0; i < SEND_NUMBER; i++) {
            TextMessage textMessage = session.createTextMessage("ActiveMQ的消息" + i);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMQ的消息" + i);
            product.send(textMessage);
        }
    }
}
