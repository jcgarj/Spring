package mx.com.aion.data.enums;

public enum TipoCarga {
    ARCHIVO(1), BASE_DATOS(2), OTRO(3), FTP(4);

    private int value;

    private TipoCarga(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
