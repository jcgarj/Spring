package mx.com.aion.data.enums;

public enum TipoRuteo {
    CARGA_ARCHIVO(1), EJECUTA_PROCESO(2);

    private int value;

    private TipoRuteo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
