package mx.com.aion.data.util;

public class ExceptionPE extends Throwable {
	
	private static final long serialVersionUID = 1L;
	
	private String method;
    private String messageException;
    private String errorMessage;
    private int errorCode;
    private Throwable initCause;
    
    public ExceptionPE(){
    	super();
    }
    
    public ExceptionPE(String e){
    	super(e);
    }
    

    public ExceptionPE(String method, Exception exception){
    	setStackTrace(exception.getStackTrace());
    	initCause(exception);
    	setMethod(method);
    	setMessageException(exception.getMessage());
    }
    
    public ExceptionPE(String method, int errorCode, String errorMessage, Throwable exception) {
        setStackTrace(exception.getStackTrace());
        initCause(exception);
        setMethod(method);
        setErrorCode(errorCode);
        setErrorMessage(errorMessage);
    }

    public ExceptionPE(String method, int errorCode, String errorMessage) {
        setMethod(method);
        setErrorCode(errorCode);
        setErrorMessage(errorMessage);
    }
    
    public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMessageException() {
		return messageException;
	}

	public void setMessageException(String messageException) {
		this.messageException = messageException;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public Throwable getInitCause() {
		return initCause;
	}

	public void setInitCause(Throwable initCause) {
		this.initCause = initCause;
	}
    

}
