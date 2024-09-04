package br.ufpb.dcx.rodrigor.projetos.edital.services;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.edital.model.Edital;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
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
        Participante p1 = new Participante();
        Participante p2 = new Participante();
        Participante p3 = new Participante();
        p1.setNome("Joao");
        p2.setNome("Maria");
        p3.setNome("Katia");
        MongoDatabase database = mongoDBConnector.getDatabase("editais");
        this.collection = database.getCollection("editais");
    }

    public void adicionarEdital(Edital edital){
        Document doc = editalToDocument(edital);
        collection.insertOne(doc);
    }

    public void removerProjeto(String titulo){
        collection.deleteOne(eq("titulo", new ObjectId(titulo)));
    }

    public List<Edital> listarEditais(){
        List<Edital> editais = new ArrayList<>();
        for (Document doc : collection.find()){
            editais.add(documentToEdital(doc));
        }
        return editais;
    }

    public List<Participante> listarParticipantes(Context ctx){
        ParticipanteService participante = ctx.appData(EDITAIS_SERVICE.key());
        List<Participante> inscritos = new LinkedList<>();
        for (Participante p : participantes){
            inscritos.add(p);
        }
        return inscritos;
    }

    public static Edital documentToEdital(Document doc){
        Edital edital = new Edital();
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
        doc.put("titulo", edital.getTitulo());
        doc.put("data", edital.getData());
        doc.put("descricao", edital.getDescricao());
        doc.put("calendario", edital.getCalendario());
        doc.put("pre-requisitos", edital.getPreRequisitos());
        doc.put("formulario-inscricao", edital.getFormInscricao());
        return doc;
    }
}
