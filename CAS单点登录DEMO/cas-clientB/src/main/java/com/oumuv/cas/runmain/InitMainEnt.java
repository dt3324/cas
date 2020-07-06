package com.oumuv.cas.runmain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class InitMainEnt {
 
	public static void main(String[] argsq) {
		
		String[] args={"1",
				"http://192.168.99.230:8443/cas/login?service=http%3A%2F%2F192.168.99.115%3A9003%2Fhello",
				"SESSION=MDUwYTEyYTktMzM5NS00YzM0LWIzNWItOWI2MjUyYWVhYzU3",
				"40"};
		if("1".equals(args[0])){
			System.out.println("----------------并发测试开始");
			multiRuntime(args[1],args[2],Integer.valueOf(args[3]));
		}else{
			System.out.println("----------------疲劳测试开始");
			strain(args[1],args[2]);
		}
		
	}
	
	/**
	 * 压力测试
	 * 
	 */
	
	public static void multiRuntime(String url,String sessionId,int count){
//		int count = 10;//设置并发数量
		int z =0;
		while(true){
			z++;
			//线程池准备
			CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
			ExecutorService executorService = Executors.newFixedThreadPool(count);
			long now = System.currentTimeMillis();//开始时间
			for (int i = 0; i < count; i++){
				
				executorService.execute(new InitMainEnt().new Task(cyclicBarrier,i,url,sessionId));
			}
			
			executorService.shutdown();
			while (!executorService.isTerminated()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			long end = System.currentTimeMillis();//结束时间
			System.out.println("第"+z+"批总共耗时!---------"+(end-now));//总共耗时
		}
	}
 
 
	public class Task implements Runnable {
		private CyclicBarrier cyclicBarrier;
		private int count;
		private String url;
		private String sessionId;
		public Task(CyclicBarrier cyclicBarrier,int count,String url,String sessionId) {
			this.cyclicBarrier = cyclicBarrier;
			this.count = count;
			this.url=url;
			this.sessionId=sessionId;
		}
 
 
		@Override
		public void run() {
			try {
				// 等待所有任务准备就绪
				cyclicBarrier.await();
				System.out.println("线程"+Thread.currentThread().getName()+"开始----------");
				//测试路径
				String url_ =url;
				boolean result = HttpClientUtil.sendPost(url_,sessionId);
				if(result){
					System.out.println(result);
					System.out.println("线程"+Thread.currentThread().getName()+"----------成功");
				}else{
					System.out.println(result);
					System.out.println("线程"+Thread.currentThread().getName()+"----------失败");
				}
				cyclicBarrier.await();
			} catch (Exception e) {
				System.out.println("出现超时的线程"+count);
				e.printStackTrace();
			}
		}
	}
	/**
	 * 疲劳测试
	 * 
	 */
	
	public static void strain(String url,String sessionId){
		long start = System.currentTimeMillis();
		while(true){
			boolean result = HttpClientUtil.sendPost(url,sessionId);
			if(!result){
				long end = System.currentTimeMillis();//结束时间
				System.out.println("距离开始时间"+(end-start)/1000 +"秒后，疲劳测试出错");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}