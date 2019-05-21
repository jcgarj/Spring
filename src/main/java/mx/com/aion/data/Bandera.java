package mx.com.aion.data;

public enum Bandera {VERDADERO("V"), FALSO("F");

	private String value;
	
	private Bandera(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}

}
