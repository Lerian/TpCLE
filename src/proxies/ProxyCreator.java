package proxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.ClassLoader;
import java.util.Arrays;

public class ProxyCreator {
	
	static public Object logObject(Object src) {
		InvocationHandler ih = new LogHandler(src);
		
		return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), src.getClass().getInterfaces(), ih);
	}
	
	static public Object lockObject(Object src) {
		InvocationHandler lockHandler = new LockHandler(src);
		
		Class<?>[] interfaces = src.getClass().getInterfaces();
		interfaces = Arrays.copyOf(interfaces, interfaces.length+1);
		interfaces[interfaces.length-1] = Blockable.class;
		
		return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces , lockHandler);
	}
}
