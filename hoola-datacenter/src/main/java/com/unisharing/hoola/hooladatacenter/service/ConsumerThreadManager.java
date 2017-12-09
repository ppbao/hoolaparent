package com.unisharing.hoola.hooladatacenter.service;


import com.unisharing.hoola.hooladatacenter.service.redis.sub.IMessageConsumer;

public class ConsumerThreadManager{
	
	private IMessageConsumer newUserMessageConsumer;
	
	protected Runnable createUserTask = new Runnable(){

        @SuppressWarnings({ "static-access"})
        @Override
        public void run() {
                for(;;){
                	newUserMessageConsumer.handle();
                }
        }
    };

	public void setNewUserMessageConsumer(IMessageConsumer newUserMessageConsumer) {
		this.newUserMessageConsumer = newUserMessageConsumer;
	}
	
	public void start(){
		Thread createUserThread1 = new Thread(createUserTask);
		System.out.println("===============createUserThread1.start();===============");
		createUserThread1.setName("createUserThread1");
		createUserThread1.start();
		
		Thread createUserThread2 = new Thread(createUserTask);
		System.out.println("===============createUserThread2.start();===============");
		createUserThread2.setName("createUserThread2");
		createUserThread2.start();
		
		Thread createUserThread3 = new Thread(createUserTask);
		System.out.println("===============createUserThread3.start();===============");
		createUserThread3.setName("createUserThread3");
		createUserThread3.start();
		
		Thread createUserThread4 = new Thread(createUserTask);
		System.out.println("===============createUserThread4.start();===============");
		createUserThread4.setName("createUserThread4");
		createUserThread4.start();
	}

}
