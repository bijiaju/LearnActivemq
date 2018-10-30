package com.bee.queue;


public class ConsumerTest implements Runnable {
	static Thread t1 = null;
	static Thread t2 = null;
	public static void main(String[] args) throws InterruptedException {

		t1 = new Thread(new ConsumerTest());
		t1.start();
		t2 = new Thread(new ConsumerTest());
		t2.start();
		while (true) {
			System.out.println(t1.isAlive());
			if (!t1.isAlive()) {
				t1 = new Thread(new ConsumerTest());
				t1.start();
				System.out.println("t1重新启动");
			}
			Thread.sleep(5000);
			if (!t2.isAlive()) {
				t2 = new Thread(new ConsumerTest());
				t2.start();
				System.out.println("t2重新启动");
			}
			Thread.sleep(5000);
		}
		// 延时500毫秒之后停止接受消息
		// Thread.sleep(500);
		// consumer.close();
	}

	public void run() {
		try {
			ConsumerTool consumer = new ConsumerTool();
			consumer.consumeMessage();
			while (ConsumerTool.isconnection) {	
				//System.out.println(123);
			}
		} catch (Exception e) {
		}

	}
}
