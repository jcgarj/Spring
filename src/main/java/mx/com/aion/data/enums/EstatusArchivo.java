package mx.com.aion.data.enums;

public enum EstatusArchivo {
    EN_PROCESO("EP"), ERROR_IDENTIFICAR_ARCHVO("ED"), ERROR("ER"), PROCESADO("PR"), REPROCESADO("RE");

    private String value;

    private EstatusArchivo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
