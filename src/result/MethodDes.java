package result;

public class MethodDes {
	private String className;
	private String methodName;
	private String action;
	private String resouce;
	
	public MethodDes() {
		this.className = "";
		this.methodName = "";
		this.action = "";
		this.resouce = "";
	}
	
	public MethodDes(String className, String methodName, String action, String resouce) {
		this.className = className;
		this.methodName = methodName;
		this.action = action;
		this.resouce = resouce;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getResouce() {
		return resouce;
	}

	public void setResouce(String resouce) {
		this.resouce = resouce;
	}
	
	
	
	
}
