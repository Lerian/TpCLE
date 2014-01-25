package main;
import loading.FileLoader;

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
		IDisplayer displayer = loader.loadDisplayer("standard");
		displayer.display(loader.loadData("resources/instances/toto.txt"));
		displayer = loader.loadDisplayer("standard");
	}
}
