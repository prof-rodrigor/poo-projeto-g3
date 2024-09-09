package br.ufpb.dcx.rodrigor.projetos.edital.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.edital.model.Edital;
import br.ufpb.dcx.rodrigor.projetos.edital.service.EditalService;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import io.javalin.http.Context;

public class EditalController {

    public void listarEditais(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAl_SERVICE.key());
        ctx.attribute("editais", editalService.listarEditais());
        ctx.render("editais/lista_editais.html");
    }

    public void mostrarFormulario(Context ctx) {
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        ctx.attribute("coodenadores", participanteService.listarProfessores());
        ctx.render("editais/form-editais.html");
    }

    public void adicionarEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAl_SERVICE.key());
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
        EditalService editalService = ctx.appData(Keys.EDITAl_SERVICE.key());
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());

        String id = ctx.pathParam("id");
        Edital updateEdital = new Edital();
        updateEdital.setTitulo("titulo");
        updateEdital.setData("titulo");
        updateEdital.setDescricao("titulo");
        updateEdital.setCalendario("titulo");
        updateEdital.setPreRequisitos("titulo");
        updateEdital.setFormInscricao("formulario");

        String coordenadorId = ctx.formParam("coodenador");
        Participante coodenador = participanteService.buscarParticipantePorId(coordenadorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado"));
        editalService.editarEdital(id, updateEdital, coodenador);
        ctx.redirect("/detalhes-edital/" + id);
    }

    public void exibirDetalhesEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAl_SERVICE.key());

        String id = ctx.pathParam("id");
        Edital edital = editalService.buscarEditalPorId(id);

        if (edital != null) {
            ctx.attribute("editais", edital);
            ctx.render("editais/detalhes-edital.html");
        } else {
            ctx.status(404).result("Edital não encontrado"); //redirencionar para a pagina de erro
        }
    }

    public void removerEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAl_SERVICE.key());
        String id = ctx.pathParam("id");
        editalService.removerEdital(id);
        ctx.redirect("/editais");
    }
}
