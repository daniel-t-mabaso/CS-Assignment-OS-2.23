package molecule;

public class Carbon extends Thread {

	private static int carbonCounter = 0;
	private int id;
	private Methane sharedMethane;

	public Carbon(Methane methane_obj) {
		Carbon.carbonCounter += 1;
		id = carbonCounter;
		this.sharedMethane = methane_obj;
	}

	public void run() {
		try {
         /*Do syncronisation before adding carbon molecule.*/
			this.sharedMethane.mutex.acquire();
			this.sharedMethane.addCarbon();
         
			/*Check if a molecule with 4 hydrogens then release all the locks if true*/
			if (this.sharedMethane.getHydrogen() >= 4) {
            
            //release and remove hydrogens
				this.sharedMethane.hydrogensQ.release(4);
				this.sharedMethane.removeHydrogen(4);
            
            //release and remove carbon
				this.sharedMethane.carbonQ.release();
				this.sharedMethane.removeCarbon(1);
            
				System.out.println("---Group ready for bonding---");
			} else {
         
            /*Release mutex block*/
				this.sharedMethane.mutex.release();
			}
         
         /*Acquire a carbon semaphore lock, call bond and use the reusable barrier*/
			this.sharedMethane.carbonQ.acquire();
			this.sharedMethane.bond("C" + this.id);
			this.sharedMethane.barrier.b_wait();
			this.sharedMethane.mutex.release();
		}
      catch (InterruptedException ex) {
			/* not handling this */
         }
	}

}
