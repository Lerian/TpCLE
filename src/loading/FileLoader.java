package loading;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import main.IDisplayer;

public class FileLoader {
	Collection<IDisplayer> displayers;
	Collection<String> displayersName;
	
	// Instancie un objet depuis un fichier
	public Object loadData(String filename) {
		Properties prop = getProperties(filename);
		String classname = prop.getProperty("class");
		Object instance = instanciateClass(classname);
		updateInstance(instance, prop);
		return instance;
	}
	
	// Charge un fichier en mémoire
	private Properties getProperties(String filename) {
		try {
			Properties prop = new Properties();
			prop.load(new FileReader(filename));
			return prop;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Vérifie l'existance de la classe demandée
	private boolean checkClass(String classname) {
		try {
			Class.forName(classname);
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Instancie la classe demandée
	private Object instanciateClass(String classname) {
		Class<?> wantedClass=null;
		if (checkClass(classname)) {
			try {
				wantedClass = Class.forName(classname);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if(wantedClass != null) {
				try {
					Object instance = wantedClass.newInstance();
					return instance;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	// Met à jour les champs de l'instance d'après le contenu du fichier
	private void updateInstance(Object instance, Properties props) {
		Method method = null;
		
		if(instance != null) {
			try {
				Set<String> keys = props.stringPropertyNames();
				
				for(String s : keys) {
					method = findSetter(instance.getClass(), s);
					if (method != null) {
						try {
							method.invoke(instance, props.getProperty(s));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		} else
			System.out.println("L'instance est nulle, pas d'update!");
	}
	
	//Trouver le setter aproprié à une clef
	private Method findSetter(Class<?> concernedClass, String key) {
		Method[] methods = null;
		methods = concernedClass.getMethods();
		
		if(!key.equals("class")) {
			for(Method m : methods) {
				if(m.getName().startsWith("set")) {
					if(m.getName().toLowerCase().substring(3).equals(key)) {
						if(m.getParameterTypes().length == 1) {
							if(m.getParameterTypes()[0].getCanonicalName().equals("java.lang.String")) {
								return m;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	// Charge un afficheur en mémoire
	public IDisplayer loadDisplayer(String displayername) {
		Properties prop = getProperties("resources/displayers/displayers.txt");
		// Une seule instance de l'afficheur demandée
		if(checkProperty("singleton", prop.getProperty(displayername))) {
			IDisplayer displayer = getDisplayer(className(prop.getProperty(displayername)));
			if(displayer == null) {
				// Chargement de l'afficheur via un proxy
				if(checkProperty("lazzy", prop.getProperty(displayername))) {
					LazzyDisplayer obj = new LazzyDisplayer(className(prop.getProperty(displayername))); // instancie le proxy d'afficheur
					displayers.add(obj);
					displayersName.add(className(prop.getProperty(displayername)));
					return obj;
				} else { // Chargement de l'afficheur
					Object obj = instanciateClass(className(prop.getProperty(displayername))); // instancie afficheur
					if(IDisplayer.class.isAssignableFrom(obj.getClass())) { // vérification que IDisplayer est bien implémenté par l'afficheur instancié (pour le cast)
						displayers.add((IDisplayer)obj);
						displayersName.add(prop.getProperty(displayername));
						return (IDisplayer)obj;
					} else {
						return null;
					}
				}
			} else {
				return displayer;
			}
		} else { // Instanciation d'un nouvel afficheur
			// Chargement de l'afficheur via un proxy
			if(checkProperty("lazzy", prop.getProperty(displayername))) {
				LazzyDisplayer obj = new LazzyDisplayer(className(prop.getProperty(displayername))); // instancie le proxy d'afficheur
				displayers.add(obj);
				displayersName.add(className(prop.getProperty(displayername)));
				return obj;
			} else { // Chargement de l'afficheur
				Object obj = instanciateClass(className(prop.getProperty(displayername))); // instancie afficheur
				if(IDisplayer.class.isAssignableFrom(obj.getClass())) { // vérification que IDisplayer est bien implémenté par l'afficheur instancié (pour le cast)
					displayers.add((IDisplayer)obj);
					displayersName.add(prop.getProperty(displayername));
					return (IDisplayer)obj;
				} else {
					return null;
				}
			}
		}
	}
	
	// Liste les afficheurs disponibles
	public Set<String> listDisplayers() {
		Properties prop = getProperties("resources/displayers/displayers.txt");
		return prop.stringPropertyNames();
	}
	
	// Récupère le nom de la classe voulue dans les paramètres de création
	private String className(String displayerDescription) {
		String[] properties = displayerDescription.split(", ");
		
		return properties[0];
	}
	
	// Vérifie si la propriété donnée est présente pour la classe donnée
	private boolean checkProperty(String property, String displayerDescription) {
		String[] properties = displayerDescription.split(", ");
		
		for(String s : properties) {
			if(s.toLowerCase().equals(property))
				return true;
		}
		return false;
	}
	
	// Vérifie si l'afficheur demandé est présent en mémoire
	private IDisplayer getDisplayer(String displayername) {
		if(displayersName == null) {
			displayers = new HashSet<IDisplayer>();
			displayersName = new HashSet<String>();
		}
		for(IDisplayer disp : displayers) {
			if(disp.getClass().getName().equals(displayername))
				return disp;
			if(disp.getClass().getName().equals("loading.LazzyDisplayer") && ((LazzyDisplayer)disp).getNotLazzyDisplayerClassName().equals(displayername))
				return disp;
		}
		return null;
	}
}
// charger l'afficheur uniquement à l'appel d'afficher() -> pattern proxy