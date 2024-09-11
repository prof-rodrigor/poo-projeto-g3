package br.ufpb.dcx.rodrigor.projetos.edital.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.edital.model.Edital;
import br.ufpb.dcx.rodrigor.projetos.edital.service.EditalService;
import br.ufpb.dcx.rodrigor.projetos.participante.model.CategoriaParticipante;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import io.javalin.http.Context;

import java.util.Optional;


public class EditalController {

    public void listarEditais(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());
        ctx.attribute("editais", editalService.listarEditais());
        ctx.render("/editais/lista_editais.html");
    }

    public void mostrarFormulario(Context ctx) {
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        ctx.attribute("professores", participanteService.listarProfessores());
        ctx.render("/editais/form-editais.html");
    }

    public void adicionarEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        validacao(ctx);
        Edital edital = new Edital();
        edital.setTitulo(ctx.formParam("titulo"));
        edital.setData(ctx.formParam("data"));
        edital.setCalendario(ctx.formParam("calendario"));

        /*edital.setCalendario(formatarCalendario(ctx));*/ //Chamando método de formatar o calendario

        edital.setPreRequisitos(ctx.formParam("pre-requisitos"));
        edital.setFormInscricao(ctx.formParam("formulario"));

        edital.setDescricao(ctx.formParam("descricao"));

        String coordenadorId = ctx.formParam("coordenador");
        Participante coordenador = participanteService.buscarParticipantePorId(coordenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado"));

        if (coordenador.getCategoria() != CategoriaParticipante.PROFESSOR) {
            throw new IllegalArgumentException("Somente professores podem ser coordenadores.");
        }
        edital.setCoordenador(coordenador);
        editalService.adicionarEdital(edital);
        ctx.redirect("/editais");
    }

    public void editarEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        validacao(ctx);
        String id = ctx.pathParam("id");
        Edital updateEdital = new Edital();
        updateEdital.setTitulo(ctx.formParam("titulo"));
        updateEdital.setData(ctx.formParam("data"));  // Correção da variável "data"
        updateEdital.setDescricao(ctx.formParam("descricao"));
        updateEdital.setCalendario(ctx.formParam("calendario"));
        updateEdital.setPreRequisitos(ctx.formParam("pre-requisitos"));
        updateEdital.setFormInscricao(ctx.formParam("formulario"));

        String coordenadorId = ctx.formParam("coordenador");
        Participante coordenador = participanteService.buscarParticipantePorId(coordenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado"));
        editalService.editarEdital(id, updateEdital, coordenador);
        ctx.redirect("/detalhe-edital/" + id);
    }

    public void exibirDetalhesEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());

        String id = ctx.pathParam("id");
        Optional<Edital> edital = editalService.buscarEditalPorId(id);

        if (edital.isPresent()) {
            ctx.attribute("editais", edital);
            ctx.render("/editais/detalhe_edital.html");
        } else {
            ctx.status(404).result("Edital não encontrado"); //redirencionar para a pagina de erro
        }
    }

    public void removerEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());
        String id = ctx.pathParam("id");
        editalService.removerEdital(id);
        ctx.redirect("/editais");
    }

    private String formatarCalendario(Context ctx) {
        StringBuilder calendario = new StringBuilder();
        String[] dataHorario = ctx.formParams("dataHorario").toArray(new String[0]);
        String[] atividade = ctx.formParams("atividade").toArray(new String[0]);

        for (int i = 0; i < dataHorario.length; i++) {
            calendario.append(dataHorario[i]).append(" - ").append(atividade[i]);
            if (i < dataHorario.length - 1) {
                calendario.append(" | ");
            }
        }
        return calendario.toString();
    }

    public void validacao(Context ctx) {
        String erro = null;

        if (ctx.formParam("id") == null || ctx.formParam("id").trim().isEmpty()) {
            erro = "ID não pode ser nulo ou vazio.";
        } else if (ctx.formParam("titulo") == null || ctx.formParam("titulo").trim().isEmpty() || ctx.formParam("titulo").length() > 30) {
            erro = "Título não pode ser nulo ou vazio.";
        } else if (ctx.formParam("data") == null || ctx.formParam("data").trim().isEmpty()) {
            erro = "Data não pode ser nula ou vazia.";
        } else if (ctx.formParam("descricao") == null || ctx.formParam("descricao").length() > 300) {
            erro = "Descrição não pode exceder 300 caracteres.";
        } else if (ctx.formParam("pre-requisitos") != null && ctx.formParam("pre-requisitos").length() > 250) {
            erro = "Pré-requisitos não podem exceder 250 caracteres.";
        } else if (ctx.formParam("formulario") == null || ctx.formParam("formulario").trim().isEmpty()) {
            erro = "Formulário de inscrição não pode ser nulo ou vazio.";
        }
        if (erro != null) {
            ctx.sessionAttribute("erro", erro);
            ctx.redirect("/formulario");
        }
    }
}