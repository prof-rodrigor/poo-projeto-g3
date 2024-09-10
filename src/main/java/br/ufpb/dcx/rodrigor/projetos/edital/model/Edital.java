package br.ufpb.dcx.rodrigor.projetos.edital.model;

import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;

import java.util.List;

public class Edital {
    private String titulo, id, data, descricao, calendario, preRequisitos, formInscricao;
    private List<Participante> inscritos;

    public Edital(String id, String data, String titulo, String descricao, String calendario, String preRequisitos, String formInscricao) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.calendario = calendario;
        this.preRequisitos = preRequisitos;
        this.formInscricao = formInscricao;
    }

    public Edital() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCalendario() {
        return calendario;
    }

    public void setCalendario(String calendario) {
        this.calendario = calendario;
    }

    public String getPreRequisitos() {
        return preRequisitos;
    }

    public void setPreRequisitos(String preRequisitos) {
        this.preRequisitos = preRequisitos;
    }

    public String getFormInscricao() {
        return formInscricao;
    }

    public void setFormInscricao(String formInscricao) {
        this.formInscricao = formInscricao;
    }
}
