package com.bee.helloworld;

import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * 发送者是短链接，发送完就结束
 * 接受者是长连接，一直能接受数据
 * @author bee
 *
 */
public class Sender {

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
		MessageProducer producer = session.createProducer(null);//先创建好producer，到发送的时候再指定Destination
		
		//第六部：使用MessageProducer的setDeliveryMode来设置持久化特性和非持久化特性   
		//用来保证断电保护的，如果持久化的话，掉电的话，没有消费的数据会保存，通电继续处理
		//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		//第七部：创建msg数据，通过producer的send方法发送数据，同理客户端使用receive方法接受
		for(int i=0;i<100;i++){
			TextMessage msg = session.createTextMessage("我是消息内容："+i);
			producer.send(destination, msg);
			TimeUnit.SECONDS.sleep(1);
		}
		
		//session.commit();//如果支持事物的话，需要commit
		
		//发送map格式的数据
		/*for(int i=0;i<100;i++){
			MapMessage message = session.createMapMessage();
			message.setString("name", "毕洋强");
			message.setString("age", "20");
			producer.send(destination, message,DeliveryMode.NON_PERSISTENT, 0 , 1000L*1000);
		}*/
		
		if(connection!= null){
			connection.close();
		}
	}
}
