import java.util.List;


class Exporting implements Runnable {
    private List<Integer> inventory;
    static int wants = Importing.productionSize;
    int gets = 0;
 public Exporting(List<Integer> inventory) {
     this.inventory = inventory;
 }
 
    @Override
public void run() {
    gets = 0;
    while (gets < wants) {
        try {
            Export();
            gets++;

        } catch (InterruptedException e) {  e.printStackTrace(); }
    }
}

 private void Export() throws InterruptedException {
                

    synchronized (inventory) {
       //if sharedQuey is empty wait until producer produces.
       while (inventory.size() == 0) {
           System.out.println(Thread.currentThread().getName()+"|| Factory's inventory is empty, Factory is waiting for "
                           + "importing Trucks to import Products, inventory size= 0");  
           inventory.wait();
            
         }

       Thread.sleep((long)(Math.random() * 2000));
         System.out.println(Thread.currentThread().getName()+" Export product num : "+ inventory.remove(0));
         inventory.notify();
     }
    
 }
 
}
