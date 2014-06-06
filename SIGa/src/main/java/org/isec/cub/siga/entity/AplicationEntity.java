package org.isec.cub.siga.entity;

import android.util.Log;

import java.sql.Timestamp;

/**
 * Created by rodrigo.tavares on 04-06-2014.
 */
public class AplicationEntity{

    private Timestamp   timestamp;
    private String      categoria;
    private String      location;
    private String      nome;
    private int duracao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public void description(){
        Log.w("LABEL", "\nDados recolhidos :> \nNome : " + getNome() + "\nCategoria : " + getCategoria() +
                "\nLocalização :" + getLocation() + "\nDuranção : " + getDuracao());
    }
}