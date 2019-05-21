package mx.com.aion.data.enums;

public enum TiposDatosSQL { MONEY("MONEY"),VARCHAR("VARCHAR"), DATE("DATE"), INTEGER("INTEGER"), BIGDECIMAL("BIGDECIMAL");
	
	private String value;
	
	private TiposDatosSQL(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	

}
