package mx.com.aion.data.enums;

public enum TipoMovimiento {
    STATEMENT("ST"), LEDGER("LD");

    private String value;

    private TipoMovimiento(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}