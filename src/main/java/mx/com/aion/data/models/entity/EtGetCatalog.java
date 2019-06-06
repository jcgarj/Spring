package mx.com.aion.data.models.entity;

public class EtGetCatalog {

    private String application;
    private String catalogId;

    public EtGetCatalog(String application, String catalogId) {
        this.application = application;
        this.catalogId = catalogId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }
}
