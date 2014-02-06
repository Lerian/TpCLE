package proxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LockHandler implements InvocationHandler {

	Object target;
	boolean locked;
	
	public LockHandler(Object target) {
		super();
		this.target = target;
	}
	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		
		if(m.getName().equals("isBlocked")) {
			return true;
		}
		if(m.getName().equals("block")) {
			locked = !locked;
		}
		
		if(locked) {
			return null;
		} else {
			return m.invoke(target, args);
		}
	}

}
