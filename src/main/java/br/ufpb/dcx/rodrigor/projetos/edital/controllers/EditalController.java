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
        ctx.render("/editais/form-editais");
    }

    public void adicionarEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());

        Edital edital = new Edital();
        edital.setTitulo(ctx.formParam("titulo"));
        edital.setData(ctx.formParam("data"));
        edital.setDescricao(ctx.formParam("descricao"));
        edital.setCalendario(ctx.formParam("calendario"));
        edital.setPreRequisitos(ctx.formParam("pre-requisitos"));
        edital.setFormInscricao(ctx.formParam("formulario"));

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

    // Exibe o form para edição, reaproveitando o forms de criação
    public void mostrarFormularioEditar(Context ctx){
        String id = ctx.pathParam("id");
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());

        Optional<Edital> edital = editalService.buscarEditalPorId(id);

        if (edital.isPresent()) {
            ctx.attribute("edital", edital.get());
            ctx.attribute("professores", participanteService.listarProfessores());
            ctx.render("/editais/form-editais.html");
        } else {
            ctx.status(404).result("Edital não encontrado.");
        }
    }

    // Processa as mudanças
    public void editarEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());

        String id = ctx.pathParam("id");
        Optional<Edital> editalExistente = editalService.buscarEditalPorId(id);

        if (editalExistente.isPresent()) {
            Edital edital = editalExistente.get();
            edital.setTitulo(ctx.formParam("titulo"));
            edital.setData(ctx.formParam("data"));
            edital.setDescricao(ctx.formParam("descricao"));
            edital.setCalendario(ctx.formParam("calendario"));
            edital.setPreRequisitos(ctx.formParam("pre-requisitos"));
            edital.setFormInscricao(ctx.formParam("formulario"));

            String coordenadorId = ctx.formParam("coordenador");
            Participante coordenador = participanteService.buscarParticipantePorId(coordenadorId)
                    .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado"));

            if (coordenador.getCategoria() != CategoriaParticipante.PROFESSOR) {
                throw new IllegalArgumentException("Somente professores podem ser coordenadores.");
            }
            edital.setCoordenador(coordenador);

            // Atualizar o edital
            editalService.editarEdital(id, edital, coordenador);
            ctx.redirect("/editais");
        } else {
            ctx.status(404).result("Edital não encontrado.");
        }
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
}
