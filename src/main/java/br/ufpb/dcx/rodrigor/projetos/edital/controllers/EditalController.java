package br.ufpb.dcx.rodrigor.projetos.edital.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.edital.model.Edital;
import br.ufpb.dcx.rodrigor.projetos.edital.services.EditalService;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import io.javalin.http.Context;

import java.security.Key;

public class EditalController {

    public void listarEditais(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAIS_SERVICE.key());
        ctx.attribute("editais", editalService.listarEditais());
        ctx.render("/editais/lista_editais.html");
    }

    public void mostrarFormulario(Context ctx) {
        ctx.render("/editais/form-editais.html");
    }

    public void adicionarEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAIS_SERVICE.key());

        Edital edital = new Edital();
        edital.setTitulo(ctx.formParam("titulo"));
        edital.setDescricao(ctx.formParam("descricao"));
        edital.setPreRequisitos("pre-requisitos");
        edital.setCalendario("calendario");
        edital.setData("data");
        edital.setFormInscricao("form inscricao"); //Criar algo relacionado a inscrição no edital

        editalService.adicionarEdital(edital);
        ctx.redirect("/editais");
    }

    public void removeEdital(Context ctx) {
        EditalService editalService = ctx.appData(Keys.EDITAIS_SERVICE.key());
        String id = ctx.pathParam("id");
        editalService.removerEdital(id);
        ctx.redirect("/editais");
    }
}
