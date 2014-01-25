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
	// utiliser une map ??
	public IDisplayer loadDisplayer(String displayername) {
		Properties prop = getProperties("resources/displayers/displayers.txt");
		Object obj = instanciateClass(prop.getProperty(displayername));
		IDisplayer displayer;
		if(IDisplayer.class.isAssignableFrom(obj.getClass())) { // vérification que IDisplayer est bien implémenté par l'afficheur instancié (pour le cast)
			displayer = getDisplayer(prop.getProperty(displayername));
			if(displayer == null) {
				displayer = (IDisplayer)instanciateClass(prop.getProperty(displayername));
				displayers.add(displayer);
				displayersName.add(prop.getProperty(displayername));
			}
			return displayer;
		}
		else
			return null;
	}
	
	// Liste les afficheurs disponibles
	public Set<String> listDisplayers() {
		Properties prop = getProperties("resources/displayers/displayers.txt");
		return prop.stringPropertyNames();
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
		}
		return null;
	}
}
// charger l'afficheur uniquement à l'appel d'afficher() -> pattern proxy