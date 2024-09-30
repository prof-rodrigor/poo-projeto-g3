package br.ufpb.dcx.rodrigor.projetos.participante.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.participante.model.CategoriaParticipante;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import io.javalin.http.Context;
import java.util.*;

public class ParticipanteController {

    public ParticipanteController() {
    }

    public void listarParticipantes(Context ctx) {
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        ctx.attribute("participantes", participanteService.listarParticipantes());
        ctx.render("/participantes/lista_participantes.html");
    }

    public void mostrarFormularioCadastro(Context ctx) {
        ctx.attribute("categorias", CategoriaParticipante.values());
        ctx.render("/participantes/formulario_participante.html");
    }

    public void adicionarParticipante(Context ctx) {
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        try {
            Participante participante = new Participante();
            participante.setNome(ctx.formParam("nome"));
            participante.setSobrenome(ctx.formParam("sobrenome"));
            participante.setEmail(ctx.formParam("email"));
            participante.setTelefone(ctx.formParam("telefone"));
            participante.setCategoria(CategoriaParticipante.valueOf(ctx.formParam("categoria")));

            participanteService.adicionarParticipante(participante);
            ctx.redirect("/participantes");

        } catch (IllegalArgumentException e) {
            Map<String, String> formData = new HashMap<>();
            ctx.formParamMap().forEach((key, value) -> formData.put(key, value.get(0)));
            ctx.attribute("erro", e.getMessage());
            ctx.attribute("participante", formData);

            List<CategoriaParticipante> categorias = Arrays.asList(CategoriaParticipante.values());
            ctx.attribute("categorias", categorias);
            ctx.render("/participantes/formulario_participante.html");
        }

    }

    public void removerParticipante(Context ctx) {
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        String id = ctx.pathParam("id");
        participanteService.removerParticipante(id);
        ctx.redirect("/participantes");
    }
}