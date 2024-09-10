package br.ufpb.dcx.rodrigor.projetos;

import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.edital.model.Edital;
import br.ufpb.dcx.rodrigor.projetos.edital.service.EditalService;
import br.ufpb.dcx.rodrigor.projetos.participante.model.CategoriaParticipante;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AtividadeTest {
    private EditalService editalService;
    private ParticipanteService participanteService;
    private MongoDBConnector mongoDBConnector;

    @BeforeEach
    public void setUp() {
        mongoDBConnector = new MongoDBConnector("mongodb://localhost:27017");
        participanteService = new ParticipanteService(mongoDBConnector);
        editalService = new EditalService(mongoDBConnector, participanteService);
    }

    @Test
    public void testAdicionarEditalReal() {
        // Criando o participante coordenador
        Participante coordenador = new Participante();
        coordenador.setNome("Coordenador Teste");
        coordenador.setSobrenome("Sobrenome");
        coordenador.setEmail("coordenador@example.com");
        coordenador.setTelefone("83 99999-9999");
        coordenador.setCategoria(CategoriaParticipante.PROFESSOR);

        // Salvando coordenador no banco
        participanteService.adicionarParticipante(coordenador);

        // Criando o edital
        Edital edital = new Edital();
        edital.setTitulo("Edital Teste");
        edital.setData("10/09/2024");
        edital.setDescricao("Descrição do edital de teste.");
        edital.setCalendario("Calendário do edital");
        edital.setPreRequisitos("Pré-requisitos");
        edital.setFormInscricao("Formulário de inscrição");
        edital.setCoordenador(coordenador);

        // Salvando o edital no banco
        editalService.adicionarEdital(edital);

        // Verificar se o edital foi adicionado
        List<Edital> editais = editalService.listarEditais();
        assertTrue(editais.stream().anyMatch(e -> e.getTitulo().equals("Edital Teste")));
    }



    @Test
    void testSomething() {
        assertEquals("teste","teste");

    }
}
