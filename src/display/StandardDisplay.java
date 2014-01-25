package display;

import main.IDisplayer;

public class StandardDisplay implements IDisplayer {

	@Override
	public void display(Object obj) {
		if (obj != null) {
			System.out.println("----------------------------------");
			System.out.println("Description de l'instance de "+obj.getClass().getCanonicalName()+" :");
			System.out.println(obj);
		} else
			System.out.println("L'instance est nulle, pas d'affichage!");
	}

}
