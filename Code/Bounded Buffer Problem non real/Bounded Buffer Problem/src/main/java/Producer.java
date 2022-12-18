import java.util.List;




class Producer implements Runnable {
 private List<Integer> sharedQueue;
 private int maxSize=4; //maximum number of products which sharedQueue can hold at a time.
  static int productionSize=5; //Total no of items to be produced by each producer
 int producerNo;
 public Producer(List<Integer> sharedQueue, int producerNo) {
     this.sharedQueue = sharedQueue;
     this.producerNo = producerNo;
 }

 @Override
 public void run() {
     for (int i = 1; i <= productionSize; i++) { //produce products.
         try {
             produce(i);

         } catch (InterruptedException e) {  e.printStackTrace(); }
     }
     
}

 private void produce(int i) throws InterruptedException {
  
    synchronized (sharedQueue) {            

       //if sharedQuey is full wait until consumer consumes.
       while (sharedQueue.size() == maxSize) {
             System.out.println(Thread.currentThread().getName()+", Queue is full, producerThread is waiting for "
                    + "consumerThread to consume, sharedQueue's size= "+maxSize);
             sharedQueue.wait();

         }

       //Bcz each producer must produce unique product
             //Ex= producer0 will produce 1-5  and producer1 will produce 6-10 in random order
       int producedItem = (productionSize*producerNo)+ i;  
       
       System.out.println(Thread.currentThread().getName() +" Produced : " + producedItem);
       sharedQueue.add(producedItem);
         Thread.sleep((long)(Math.random() * 1000));
         sharedQueue.notify();
     }
 }

    
}