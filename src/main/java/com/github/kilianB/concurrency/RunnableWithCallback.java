package com.github.kilianB.concurrency;

/**
 * A runnable wrapper which can be useful to schedule a task with a specific
 * delay and invoking a callback once the runnable finished executing.
 * 
 * <h2>e.g. conditional task rescheduling</h2>
 * 
 * <pre>
 * Callback callback = {
 *      //Check if we want to reschedule a new task
 *      Runnable rescheduledTask = new Task();
 *      Exector.schedule(new RunnableWithCallBack(rescheduledTask,this),newDelay,timeunit); 
 * };
 * 
 * Runnable task = new Task();
 * RunnableWithCallback wrapper = new RunnableWithCallback(task,callback);
 * Executor.shedule(wrapper,initialDelay,TimeUnit.seconds);
 * </pre>
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class RunnableWithCallback implements Runnable {

	private Runnable task, callback;

	/**
	 * @param task The task to execute
	 * @param callback executed after task finished
	 */
	public RunnableWithCallback(Runnable task, Runnable callback) {
		this.task = task;
		this.callback = callback;
	}

	@Override
	public void run() {
		task.run();
		callback.run();
	}
}