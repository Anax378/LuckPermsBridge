package org.anax.luckPermsBridge.thread;

import java.util.ArrayDeque;
import java.util.Queue;

public class ThreadPool{
	private int maxWorkers;
	private int activeWorkers = 0;
	Queue<Runnable> q = new ArrayDeque<>();

	public ThreadPool(int maxWorkers){
		this.maxWorkers = maxWorkers;
	}

	synchronized void deregister(){
		activeWorkers--;
		while(activeWorkers < maxWorkers && !q.isEmpty()){
			activeWorkers++;
			new ThreadPoolWorker(q.poll(), this).start();
		}
	}

	public synchronized void enqueue(Runnable task){
		if(task == null){
			return;
		}
		if(activeWorkers < maxWorkers){
			activeWorkers++;
			new ThreadPoolWorker(task, this).start();
		}else{
			q.offer(task);
		}
	}

	private static class ThreadPoolWorker extends Thread {
		Runnable task;
		ThreadPool pool;

		ThreadPoolWorker(Runnable task, ThreadPool pool){
			this.task = task;
			this.pool = pool;
		}

		@Override
		public void run(){
			try{
				task.run();
			} finally {
				pool.deregister();
			}
		}
	}

}


