package br.ufpb.dcx.rodrigor.projetos.participante.services;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.participante.model.CategoriaParticipante;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

public class ParticipanteService {
    private static final Logger logger = LogManager.getLogger(ParticipanteService.class);

    private final String host;

    public ParticipanteService(String host) {
        this.host = host;
    }

    public List<Participante> listarParticipantesPorCategoria(CategoriaParticipante categoriaParticipante) {
        String url = host + "/v1/participantes";
        if (categoriaParticipante != null) {
            url += "?categoria=" + categoriaParticipante.name();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<Participante>>() {});
            } else if (response.statusCode() == HTTP_NOT_FOUND) {
                return new LinkedList<>();
            } else {
                logger.error("Erro ao recuperar participantes: {}", response.statusCode());
                throw new RuntimeException("Erro ao recuperar participantes: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Erro ao recuperar participantes", e);
            throw new RuntimeException("Erro ao recuperar participantes", e);
        }
    }

    public List<Participante> listarProfessores() {
        return listarParticipantesPorCategoria(CategoriaParticipante.PROFESSOR);
    }

    public List<Participante> listarParticipantes() {
        return this.listarParticipantesPorCategoria(null);
    }


    public Optional<Participante> buscarParticipantePorId(String string) {
        String url = host + "/v1/participantes/" + string;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                return Optional.of(mapper.readValue(response.body(), Participante.class));
            } else if (response.statusCode() == HTTP_NOT_FOUND) {
                return Optional.empty();
            } else {
                logger.error("Erro ao recuperar participante: {}", response.statusCode());
                throw new RuntimeException("Erro ao recuperar participante: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Erro ao recuperar participante", e);
            throw new RuntimeException("Erro ao recuperar participante", e);
        }
    }
//
//    public void adicionarParticipante(Participante participante) {
//        Document doc = participanteToDocument(participante);
//        collection.insertOne(doc);
//        participante.setId(doc.getObjectId("_id"));
//    }
//
//    public void atualizarParticipante(Participante participanteAtualizado) {
//        Document doc = participanteToDocument(participanteAtualizado);
//        collection.replaceOne(eq("_id", new ObjectId(participanteAtualizado.getId().toString())), doc);
//    }
//
//    public void removerParticipante(String id) {
//        collection.deleteOne(eq("_id", new ObjectId(id)));
//    }
//
//    public static Participante documentToParticipante(Document doc) {
//        Participante participante = new Participante();
//        participante.setId(doc.getObjectId("_id"));
//        participante.setNome(doc.getString("nome"));
//        participante.setSobrenome(doc.getString("sobrenome"));
//        participante.setEmail(doc.getString("email"));
//        participante.setTelefone(doc.getString("telefone"));
//        participante.setCategoria(CategoriaParticipante.valueOf(doc.getString("categoria")));
//        return participante;
//    }
//
//    public static Document participanteToDocument(Participante participante) {
//        Document doc = new Document();
//        if (participante.getId() != null) {
//            doc.put("_id", participante.getId());
//        }
//        doc.put("nome", participante.getNome());
//        doc.put("sobrenome", participante.getSobrenome());
//        doc.put("email", participante.getEmail());
//        doc.put("telefone", participante.getTelefone());
//        doc.put("categoria", participante.getCategoria().name());
//        return doc;
//    }
}