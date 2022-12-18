import java.util.List;




class Importing implements Runnable {
 private List<Integer> inventory;
 private int inventorySize=4; //maximum number of products which inventory can hold at a time.
  static int productionSize=5; //Total no of items to be produced by each producer
 int TruckNo;
 public Importing(List<Integer> inventory, int TruckNo) {
     this.inventory = inventory;
     this.TruckNo = TruckNo;
 }

 @Override
 public void run() {
     for (int i = 1; i <= productionSize; i++) { //produce products.
         try {
             Import(i);

         } catch (InterruptedException e) {  e.printStackTrace(); }
     }
     
}

 private void Import(int i) throws InterruptedException {
  
    synchronized (inventory) {            

       //if sharedQuey is full wait until consumer consumes.
       while (inventory.size() == inventorySize) {
             System.out.println(Thread.currentThread().getName()+"|| Factory's inventory is full, Factory is waiting for "
                    + "ExportingTrucks  to export products, inventory size= "+inventorySize);
             inventory.wait();
         }

       //Bcz each producer must produce unique product
             //Ex= producer0 will produce 1-5  and producer1 will produce 6-10 in random order
       int producedItem = (productionSize*TruckNo)+ i;  
       
       System.out.println(Thread.currentThread().getName() +" import Product num : " + producedItem);
       inventory.add(producedItem);
         Thread.sleep((long)(Math.random() * 1000));
         inventory.notify();
     }
 }

    
}