package mx.com.aion.data.enums;

public enum TipoProceso {SINCRONO(1),ASINCRONO(2);

private int value;

private TipoProceso(int value) {
	this.value = value;
}

public int getValue(){
	return value;
}

}