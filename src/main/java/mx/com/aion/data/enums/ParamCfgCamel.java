package mx.com.aion.data.enums;

public enum ParamCfgCamel {
    USUARIO_CAMEL("Camel_Usuario");

    private String value;

    private ParamCfgCamel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
