import java.util.LinkedList;
import java.util.List;
public class MULTIPLE_ProducerConsumerWaitNotify {

 public static void main(String args[]) {
  List<Integer> sharedQueue = new LinkedList<>(); //Creating shared object
   
  Producer producer0=new Producer(sharedQueue, 0);
  Consumer consumer0=new Consumer(sharedQueue);

     Thread producerThread0 = new Thread(producer0, "ProducerThread0");
     Thread consumerThread0 = new Thread(consumer0, "ConsumerThread0");
     producerThread0.start();
     consumerThread0.start();
     
     
  Producer producer1=new Producer(sharedQueue, 1);
  Consumer consumer1=new Consumer(sharedQueue);

     Thread producerThread1 = new Thread(producer1, "ProducerThread1");
     Thread consumerThread1 = new Thread(consumer1, "ConsumerThread1");
     producerThread1.start();
     consumerThread1.start();
 } 
    
}