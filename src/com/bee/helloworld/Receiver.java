package com.bee.helloworld;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {
	public static void main(String[] args) throws JMSException, InterruptedException {
		//第一步：建立链接工厂对象，输入配置信息
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER, 
				ActiveMQConnectionFactory.DEFAULT_PASSWORD, 
				"tcp://localhost:61616");
		
		//第二部：通过工厂对象创建Connection链接，并开启链接，默认是关闭的
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		//第三部：通过Connection对象创建Session回话（上下文环境），用于接收消息；
	            //参数1是否启用事物,参数2 签收模式，一般为自动签收
		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		
		/*第四部： 通过session创建Destination，指的是一个客户端用来指定生产消息和消费消息的对象，
		 * ptp模式中，Destination是Queue队列,   
		 * 发布订阅模式中，Destination是topic主题
		 * 他们的父类是Destination  Queue createQueue = session.createQueue("first");
		 * */
		Destination destination = session.createQueue("first");
		
		//第五部： 通过Session对象创建消息发送和接受对象（生产者和消费者）MessageProducer/MessageConsumer
		MessageConsumer consumer = session.createConsumer(destination);//先创建好producer，到发送的时候再指定Destination
		
		//第六部：接受数据
		while(true){
			TextMessage message = (TextMessage) consumer.receive();
			//如果发送端设置为手动签收，就需要手动签收   Session.CLIENT_ACKNOWLEDGE
			//message.acknowledge();
			
			System.out.println("消费数据："+message.getText());
		}
		
	}
}
