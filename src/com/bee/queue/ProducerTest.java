package com.bee.queue;

import javax.jms.JMSException;      

public class ProducerTest {      
     
    /**    
     * @param args    
     * @throws Exception 
     * @throws JMSException 
     */     
    public static void main(String[] args) throws JMSException, Exception{      
        ProducerTool producer = new ProducerTool();     
        for(int i=0;i<100;i++){
        	 producer.produceMessage("Hello, world!   "+i);      
        	 producer.close();
        }
       
    }      
}      

