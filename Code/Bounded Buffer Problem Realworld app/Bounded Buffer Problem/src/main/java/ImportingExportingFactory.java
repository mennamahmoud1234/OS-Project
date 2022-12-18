import java.util.LinkedList;
import java.util.List;
public class ImportingExportingFactory {

 public static void main(String args[]) {
  List<Integer> inventory = new LinkedList<>(); //Creating Shared Object 
   
  Importing importingTruck0=new Importing(inventory, 0);
  Exporting ExportingTruck0=new Exporting(inventory);

     Thread import0 = new Thread(importingTruck0, "importingTruck Num 0->");
     Thread export0 = new Thread(ExportingTruck0, "ExportingTruck Num 0->");
     import0.start();
     export0.start();
     
     
  Importing importingTruck1=new Importing(inventory, 1);
  Exporting ExportingTruck1=new Exporting(inventory);

     Thread import1 = new Thread(importingTruck1, "importingTruck Num 1->");
     Thread export1 = new Thread(ExportingTruck1, "ExportingTruck1 Num 1->");
     import1.start();
     export1.start();
 } 
    
}
