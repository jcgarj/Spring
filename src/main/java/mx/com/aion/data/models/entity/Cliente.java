package mx.com.aion.data.models.entity;

import java.io.Serializable;
import java.util.Date;

public class Cliente implements Serializable {//Ocupa JdbcTemplate

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Date crateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCrateAt() {
        return crateAt;
    }

    public void setCrateAt(Date crateAt) {
        this.crateAt = crateAt;
    }
}
