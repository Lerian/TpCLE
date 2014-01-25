package loading;

import main.IDisplayer;

public class LazzyDisplayer implements IDisplayer {

	IDisplayer notLazzyDisplayer;
	String notLazzyDisplayerClassName;
	
	public LazzyDisplayer(String displayerClassName) {
		notLazzyDisplayerClassName = displayerClassName;
	}
		
	// Instancie l'afficheur demandé
	private IDisplayer instanciateDisplayer(String classname) {
		Class<?> wantedClass=null;
			try {
				wantedClass = Class.forName(classname);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if(wantedClass != null) {
				try {
					Object instance = wantedClass.newInstance();
					if (IDisplayer.class.isAssignableFrom(instance.getClass())) {
						return (IDisplayer)instance;
					} else {
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return null;
	}
	
	@Override
	public void display(Object obj) {
		if (notLazzyDisplayer == null) {
			notLazzyDisplayer = instanciateDisplayer(notLazzyDisplayerClassName);
		}
		notLazzyDisplayer.display(obj);
	}

}
