package br.ufpb.dcx.rodrigor.projetos.edital.model;

import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;

public class Edital {
    private String id, titulo, data, descricao, calendario, preRequisitos, formInscricao;
    private Participante coordenador;

    private static final int MAX_TITULO_LENGTH = 250;
    private static final int MAX_DESCRICAO_LENGTH = 1000;
    private static final int MAX_PREREQUISITOS_LENGTH = 800;
    private static final int MAX_FORMINSCRICAO_LENGTH = 600;

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
        if (titulo.isBlank())
            throw new IllegalArgumentException("O título não pode estar vazio");
        if (titulo.length() > MAX_TITULO_LENGTH)
            throw new IllegalArgumentException("O título não pode ter mais de 250 caracteres");
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
        if (descricao.isBlank())
            throw new IllegalArgumentException("A descrição não pode estar vazio");
        if (descricao.length() > MAX_DESCRICAO_LENGTH)
            throw new IllegalArgumentException("A descrição não pode ter mais de 1000 caracteres");
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
        if (preRequisitos.isBlank())
            throw new IllegalArgumentException("Os pré-requisitos não podem estar vazio");
        if (preRequisitos.length() > MAX_PREREQUISITOS_LENGTH)
            throw new IllegalArgumentException("Os pré-requisitos não podem ter mais de 800 caracteres");
        this.preRequisitos = preRequisitos;
    }

    public String getFormInscricao() {
        return formInscricao;
    }

    public void setFormInscricao(String formInscricao) {
        if (formInscricao.isBlank())
            throw new IllegalArgumentException("O formulário de inscrição não pode estar vazio");
        if (formInscricao.length() > MAX_FORMINSCRICAO_LENGTH)
            throw new IllegalArgumentException("O formulário de inscrição não pode ter mais de 600 caracteres");
        this.formInscricao = formInscricao;
    }

    public Participante getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Participante coordenador) {
        this.coordenador = coordenador;
    }
}
