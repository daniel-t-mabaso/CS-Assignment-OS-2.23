package molecule;

public class Hydrogen extends Thread {

	private static int carbonCounter = 0;
	private int id;
	private Methane sharedMethane;

	public Hydrogen(Methane methane_obj) {
		Hydrogen.carbonCounter += 1;
		id = carbonCounter;
		this.sharedMethane = methane_obj;

	}

	public void run() {
		try {
         /*Do syncronisation before adding hydrogen molecule.*/
			this.sharedMethane.mutex.acquire();
			this.sharedMethane.addHydrogen();
         
			/*Check if a molecule with 4 hydrogens and 1 carbon has been formed then release all the locks if true*/
			if ((this.sharedMethane.getHydrogen() >= 4) && (this.sharedMethane.getCarbon() >= 1)) {
         
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
         
         /*Acquire a hydrogen semaphore lock, call bond and use the reusable barrier*/
			this.sharedMethane.hydrogensQ.acquire();
			this.sharedMethane.bond("H" + this.id);
			this.sharedMethane.barrier.b_wait();
		}
      catch (InterruptedException ex) {
			/* not handling this */
         }
	}

}
