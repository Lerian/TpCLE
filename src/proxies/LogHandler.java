package proxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogHandler implements InvocationHandler{

	Object target;
	
	public LogHandler(Object target) {
		super();
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		System.out.println("Méthode "+m.getName()+" appelée");
		
		return m.invoke(target, args);
	}

}
