package br.ufpb.dcx.rodrigor.projetos.edital.model;

import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;

import java.util.List;
import java.util.Optional;

public class Edital {
    private String id, titulo, data, descricao, calendario, preRequisitos, formInscricao;
    Participante coordenador;
    private List<Participante> inscritos;

    public Edital(String id, String titulo, String data, String descricao, String calendario, String preRequisitos, String formInscricao, Participante coordenador) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.calendario = calendario;
        this.preRequisitos = preRequisitos;
        this.formInscricao = formInscricao;
        this.coordenador = coordenador;
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

    public Participante getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Participante coordenador) {
        this.coordenador = coordenador;
    }
}
