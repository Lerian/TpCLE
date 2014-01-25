package misc.ducktyping;

import java.lang.reflect.Method;

public class DuckTyping {

	public boolean isSubType(Class<?> psuper, Class<?> psub) {
		for(Method m : psuper.getMethods()) {
			try {
				Method m1 = psub.getMethod(m.getName(), m.getParameterTypes());
				if (!m.getReturnType().isAssignableFrom(m1.getReturnType())) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
