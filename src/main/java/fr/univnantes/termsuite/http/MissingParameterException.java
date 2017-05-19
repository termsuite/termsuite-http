package fr.univnantes.termsuite.http;

public class MissingParameterException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String paramName;

	public MissingParameterException(String paramName) {
		super("Missing parameter \""+paramName+"\"");
		this.paramName = paramName;
	}
	
	public String getParamName() {
		return paramName;
	}
}
