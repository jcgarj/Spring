package mx.com.aion.data.enums;

public enum Estatus {ERROR(1),EXITO(0);

	private int value;
	
	private Estatus(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}

}
