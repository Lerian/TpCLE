package main;
import loading.FileLoader;
import misc.ducktyping.*;
import display.StandardDisplay;
import proxies.*;

public class Application {
	
	/*
	 * - charger les données
	 * - lister les afficheurs
	 * - charger l'afficheur voulu
	 * - afficher
	 */
	public static void main(String[] args) {
		
		// Test du proxy pour ajouter des fonctions
			// log
		IDisplayer disp = new StandardDisplay();
		Object p = ProxyCreator.logObject(disp);
		((IDisplayer)p).display("toto");
			// lock
		Object p2 = ProxyCreator.lockObject(disp);
		System.out.println("affichage");
		((IDisplayer)p2).display("toto");
		System.out.println("blocage");
		((Blockable)p2).block(true);
		System.out.println("affichage");
		((IDisplayer)p2).display("toto");/**/
		
		/*/ Test des différentes options d'afficheurs
		FileLoader loader = new FileLoader();
		System.out.println(loader.listDisplayers());
		IDisplayer displayer;
		System.out.println("\nChargement de l'afficheur 1");
		displayer = loader.loadDisplayer("standard");
		System.out.println("Utilisation de l'afficheur 1");
		displayer.display(loader.loadData("resources/instances/toto.txt"));
		
		System.out.println("\nChargement de l'afficheur 2");
		displayer = loader.loadDisplayer("standardMaj");
		System.out.println("Utilisation de l'afficheur 2");
		displayer.display(loader.loadData("resources/instances/toto.txt"));
		
		System.out.println("\nChargement de l'afficheur 1");
		displayer = loader.loadDisplayer("standard");
		System.out.println("Utilisation de l'afficheur 1");
		displayer.display(loader.loadData("resources/instances/toto.txt"));

		System.out.println("\nChargement de l'afficheur 2");
		displayer = loader.loadDisplayer("standardMaj");
		System.out.println("Utilisation de l'afficheur 2");
		displayer.display(loader.loadData("resources/instances/toto.txt"));/**/
		
		/*/ Test du duck typing
		System.out.println("------------------Début du test de duck typing------------------");
		DuckTyping dt = new DuckTyping();
		if (dt.isSubType(IDisplayer.class, StandardDisplayDT.class)) {
			System.out.println("StandardDisplayDT implémente (ou est sous classe de) IDisplayer");
		} else {
			System.out.println("StandardDisplayDT n'implémente pas (ou n'est pas sous classe de) IDisplayer");
		}
		System.out.println("-------------------Fin du test de duck typing-------------------");/**/
	}
}
