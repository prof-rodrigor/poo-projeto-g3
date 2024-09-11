package br.ufpb.dcx.rodrigor.projetos.edital.model;

import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;

import java.util.List;
import java.util.Optional;

public class Edital {
    private String id, titulo, data, descricao, calendario, preRequisitos, formInscricao;
    private Participante coordenador;
    private static final int MAX_TITULO_LENGTH = 250;

    public Edital(String id, String titulo, String data, String descricao, String calendario, String preRequisitos,
            String formInscricao, Participante coordenador) {
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
        if (titulo == null || titulo.length() > MAX_TITULO_LENGTH)
            throw new IllegalArgumentException(
                    "Tamanho do titulo invalido, o titulo nao pode ser Null e deve ter no maximo" + MAX_TITULO_LENGTH
                            + " caracteres.");
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
//        if (descricao == null || descricao.length() > MAX_TITULO_LENGTH)
//            throw new IllegalArgumentException(
//                    "Tamanho da descricao invalida, a descricao nao pode ser Null e deve ter no maximo" + MAX_TITULO_LENGTH
//                            + " caracteres.");
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
        if (preRequisitos == null || preRequisitos.length() > MAX_TITULO_LENGTH)
            throw new IllegalArgumentException(
                    "Tamanho do pre-requisitos invalidos, nao pode ser Null e deve ter no maximo" + MAX_TITULO_LENGTH
                            + " caracteres. O cara não é a TI");
        this.preRequisitos = preRequisitos;
    }

    public String getFormInscricao() {
        return formInscricao;
    }

    public void setFormInscricao(String formInscricao) {
        if (formInscricao == null || formInscricao.length() > MAX_TITULO_LENGTH)
            throw new IllegalArgumentException(
                    "Tamanho do link do formulario invalido, nao pode ser Null e deve ter no maximo" + MAX_TITULO_LENGTH
                            + " caracteres.");
        this.formInscricao = formInscricao;
    }

    public Participante getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Participante coordenador) {
        this.coordenador = coordenador;
    }
}
