package com.nttdata.apiusuario.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "telefonos")
@Data
public class Telefono {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String number;
    private String citycode;
    private String contrycode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario")
    private Usuario usuario;
    
    // Getter y Setter para 'number'
    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }
    
    // Getter y Setter para 'citycode'
    public String getcitycode() {
        return citycode;
    }

    public void setcitycode(String citycode) {
        this.citycode = citycode;
    }
    
    // Getter y Setter para 'contrycode'
    public String getcontrycode() {
        return contrycode;
    }

    public void setcontrycode(String contrycode) {
        this.contrycode = contrycode;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
   
}
