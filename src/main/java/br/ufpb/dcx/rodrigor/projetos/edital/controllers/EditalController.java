package br.ufpb.dcx.rodrigor.projetos.edital.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.edital.model.Edital;
import br.ufpb.dcx.rodrigor.projetos.edital.service.EditalService;
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

        Edital edital = new Edital();
        edital.setTitulo(ctx.formParam("titulo"));
        edital.setData(ctx.formParam("data"));
        edital.setDescricao(ctx.formParam("descricao"));
        edital.setCalendario(ctx.formParam("calendario"));
        edital.setPreRequisitos(ctx.formParam("pre-requisitos"));
        edital.setFormInscricao(ctx.formParam("formulario"));

        String coordenadorId = ctx.formParam("coodenador");
        Participante coodenador = participanteService.buscarParticipantePorId(coordenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado"));

        edital.setCoordenador(coodenador);
        editalService.adicionarEdital(edital);
        ctx.redirect("/editais");
    }

    public void editarEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());

        String id = ctx.pathParam("id");
        Edital updateEdital = new Edital();
        updateEdital.setTitulo("titulo");
        updateEdital.setData(ctx.formParam("titulo"));
        updateEdital.setDescricao(ctx.formParam("descricao"));
        updateEdital.setCalendario(ctx.formParam("calendario"));
        updateEdital.setPreRequisitos(ctx.formParam("pre-requisitos"));
        updateEdital.setFormInscricao(ctx.formParam("formulario"));

        String coordenadorId = ctx.formParam("coodenador");
        Participante coodenador = participanteService.buscarParticipantePorId(coordenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado"));
        editalService.editarEdital(id, updateEdital, coodenador);
        ctx.redirect("/detalhes-edital/" + id);
    }

    public void exibirDetalhesEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAL_SERVICE.key());

        String id = ctx.pathParam("id");
        Optional<Edital> edital = editalService.buscarEditalPorId(id);

        if (edital.isPresent()) {
            ctx.attribute("editais", edital);
            ctx.render("editais/detalhes-edital.html");
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
