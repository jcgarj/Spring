package mx.com.aion.data.models.entity;

public class EtToken {

    private String application;
    private String username;

    public EtToken(String application, String username) {
        this.application = application;
        this.username = username;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
