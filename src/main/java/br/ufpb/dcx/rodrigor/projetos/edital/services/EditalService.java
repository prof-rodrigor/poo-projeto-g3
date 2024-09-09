package br.ufpb.dcx.rodrigor.projetos.edital.services;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.edital.model.Edital;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import br.ufpb.dcx.rodrigor.projetos.projeto.model.Projeto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static br.ufpb.dcx.rodrigor.projetos.Keys.EDITAIS_SERVICE;
import static com.mongodb.client.model.Filters.eq;

public class EditalService extends AbstractService {
    private final MongoCollection<Document> collection;
    private Participante participante = new Participante();

    List<Participante> participantes = new LinkedList<>();

    public EditalService(MongoDBConnector mongoDBConnector) {
        super(mongoDBConnector);
        MongoDatabase database = mongoDBConnector.getDatabase("editais");
        this.collection = database.getCollection("editais");
    }

    public void adicionarEdital(Edital edital){
        Document doc = editalToDocument(edital);
        collection.insertOne(doc);
        edital.setId(doc.getObjectId("_id").toString());
    }

    public void removerEdital(String id){
        collection.deleteOne(eq("_id", new ObjectId(id)));
    }

    public List<Edital> listarEditais(){
        List<Edital> editais = new ArrayList<>();
        for (Document doc : collection.find()){
            editais.add(documentToEdital(doc));
        }
        return editais;
    }

    public void atualizarEdital(Edital editalAtualizado) {
        Document doc = editalToDocument(editalAtualizado);
        collection.replaceOne(eq("_id", new ObjectId(editalAtualizado.getId())), doc);
    }

    public List<Participante> listarParticipantes(Context ctx){
        ParticipanteService participante = ctx.appData(EDITAIS_SERVICE.key());
        List<Participante> inscritos = new LinkedList<>();
        for (Participante p : participantes){
            inscritos.add(p);
        }
        return inscritos;
    }

    public Edital getEditalDetalhes() {
        // Precisa buscar no banco
        return new Edital(
                "id",
                "Edital de Concurso Público",
                "01/09/2024",
                "Este edital visa a contratação de servidores para a administração pública.",
                "01/10/2024 - 15/11/2024",
                "Diploma de nível superior",
                "Formulário disponível no site oficial");
    }

    public static Edital documentToEdital(Document doc){
        Edital edital = new Edital();
        edital.setId(doc.getObjectId("_id").toString());
        edital.setTitulo(doc.getString("titulo"));
        edital.setData(doc.getString("data"));
        edital.setDescricao(doc.getString("descricao"));
        edital.setCalendario(doc.getString("calendario"));
        edital.setPreRequisitos(doc.getString("pre-requisitos"));
        edital.setFormInscricao(doc.getString("formulario-inscricao"));
        return edital;
    }

    public static Document editalToDocument(Edital edital){
        Document doc = new Document();
        if (edital.getId() != null) {
            doc.put("_id", new ObjectId(edital.getId()));
        }
        doc.put("titulo", edital.getTitulo());
        doc.put("data", edital.getData());
        doc.put("descricao", edital.getDescricao());
        doc.put("calendario", edital.getCalendario());
        doc.put("pre-requisitos", edital.getPreRequisitos());
        doc.put("formulario-inscricao", edital.getFormInscricao());
        return doc;
    }
}
