package display;

import main.IDisplayer;

public class StandardDisplay implements IDisplayer {
	
	// Boucle pour simuler un traitement prenant du temps
	public StandardDisplay() {
		float i = 0;
		while(i < 1/*000000/**/){
			System.out.println(i);
			i++;}
	}

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
