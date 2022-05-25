import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

class Scratch {
	public static void main(String[] args) throws InterruptedException {

		Scheduler scheduler = new Scheduler(100, 10);

		Instant inst = Instant.ofEpochMilli(Instant.now().toEpochMilli() + (5 * 1000));

		Request r1 = new Request("Amit", LocalDateTime.ofInstant(inst, ZoneId.systemDefault()));
		scheduler.addRequest(r1);

		inst = Instant.ofEpochMilli(Instant.now().toEpochMilli() + (10 * 1000));
		r1 = new Request("Shivam 1", LocalDateTime.ofInstant(inst, ZoneId.systemDefault()));
		scheduler.addRequest(r1);


		r1 = new Request("Shivam 2", LocalDateTime.ofInstant(inst, ZoneId.systemDefault()));
		scheduler.addRequest(r1);

		scheduler.runScheduler();

		System.out.println("Going For Sleep");

		System.out.println("Good Bye");

	}

}

class Scheduler implements Runnable{
	private PriorityBlockingQueue<Request> queue;
	private ExecutorService executor;
	private AtomicBoolean isRunning;
	private Thread current;
	public Scheduler(int jobCapacity, int workers ) {
		queue=new PriorityBlockingQueue<>(jobCapacity, Comparator.comparing(Request::getTime));
		executor=Executors.newFixedThreadPool(workers);
		isRunning =new AtomicBoolean(false);
	}

	boolean addRequest(Request request){
		return queue.offer(request);
	}

	void terminate(){
		isRunning.set(false);
	}

	void runScheduler(){

		if(!isRunning.get()){
			synchronized (this) {
				if(!isRunning.get()) {
					isRunning.set(true);
					current=new Thread(this);
					current.start();
				}
			}
		}
	}


	@Override
	public void run() {
		while(isRunning.get()){
			while (!queue.isEmpty() && LocalDateTime.now().isAfter(queue.peek().getTime())) {
				Request request = queue.poll();
				executor.execute(new RequestExecutor(request));
			}
		}
		System.out.println("All jobs Completed");
	}
}


class RequestExecutor implements Runnable{

	Request fRequest;
	RequestExecutor(Request request){
		this.fRequest=request;
	}
	@Override
	public void run() {
		System.out.println("Request :"+fRequest.getName() +" at "+fRequest.getTime().toString()+" currentTime: "+LocalDateTime.now());
	}
}


class Request{
	String name;
	LocalDateTime time;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Request(String name, LocalDateTime time) {
		this.name = name;
		this.time = time;
	}


}
