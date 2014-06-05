package org.isec.cub.siga.entity;

import java.sql.Timestamp;

/**
 * Created by rodrigo.tavares on 04-06-2014.
 */
public class AplicationEntity{

    private Timestamp   timestamp;
    private String      categoria;
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
}