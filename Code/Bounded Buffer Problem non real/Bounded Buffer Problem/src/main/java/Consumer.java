import java.util.List;


class Consumer implements Runnable {
    private List<Integer> sharedQueue;
    static int wants = Producer.productionSize;
    int gets = 0;
 public Consumer(List<Integer> sharedQueue) {
     this.sharedQueue = sharedQueue;
 }
 
    @Override
public void run() {
    gets = 0;
    while (gets < wants) {
        try {
            consume();
            gets++;

        } catch (InterruptedException e) {  e.printStackTrace(); }
    }
}

 private void consume() throws InterruptedException {
                

    synchronized (sharedQueue) {
       //if sharedQuey is empty wait until producer produces.
       while (sharedQueue.size() == 0) {
           System.out.println(Thread.currentThread().getName()+", Queue is empty, consumerThread is waiting for "
                           + "producerThread to produce, sharedQueue's size= 0");  
           sharedQueue.wait();
            
         }

       Thread.sleep((long)(Math.random() * 2000));
         System.out.println(Thread.currentThread().getName()+", CONSUMED : "+ sharedQueue.remove(0));
         sharedQueue.notify();
     }
    
 }
 
}
