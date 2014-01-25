package main;
import loading.FileLoader;
import misc.ducktyping.*;

public class Application {
	
	/*
	 * - charger les données
	 * - lister les afficheurs
	 * - charger l'afficheur voulu
	 * - afficher
	 */
	public static void main(String[] args) {
		FileLoader loader = new FileLoader();
		System.out.println(loader.listDisplayers());
		IDisplayer displayer;
		displayer = loader.loadDisplayer("standard");
		displayer.display(loader.loadData("resources/instances/toto.txt"));
		displayer = loader.loadDisplayer("standard");
		
		// Test du duck typing
		System.out.println("------------------Début du test de duck typing------------------");
		DuckTyping dt = new DuckTyping();
		if (dt.isSubType(IDisplayer.class, StandardDisplayDT.class)) {
			System.out.println("StandardDisplayDT implémente (ou est sous classe de) IDisplayer");
		} else {
			System.out.println("StandardDisplayDT n'implémente pas (ou n'est pas sous classe de) IDisplayer");
		}
		System.out.println("-------------------Fin du test de duck typing-------------------");
	}
}
