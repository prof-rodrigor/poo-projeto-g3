package br.ufpb.dcx.rodrigor.projetos.edital.service;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.edital.model.Edital;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.xml.xpath.XPathEvaluationResult;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class EditalService extends AbstractService {
    private final MongoCollection<Document> collection;
    private final ParticipanteService participanteService;

    public EditalService(MongoDBConnector mongoDBConnector, ParticipanteService participanteService) {
        super(mongoDBConnector);
        this.participanteService = participanteService;
        MongoDatabase mongoDatabase = mongoDBConnector.getDatabase("editais");
        this.collection = mongoDatabase.getCollection("editais");
    }

    // Adicionar
    public void adicionarEdital(Edital edital){
        Document doc = editalToDocument(edital);
        collection.insertOne(doc);
    }

    // Remover
    public void removerEdital(String id){
        collection.deleteOne(eq("id", new ObjectId(id)));
    }

    // Listar
    public List<Edital> listarEditais(){
        List<Edital> editais = new ArrayList<>();
        for (Document doc : collection.find()){
            editais.add(documentToEdital(doc));
        }
        return editais;
    }

    // Editar
    public void editarEdital(String id, Edital updateEdital, Participante coordenador){
        Bson filtro = Filters.eq("_id", new ObjectId(id));

        Bson updates = Updates.combine(
                Updates.set("titulo", updateEdital.getTitulo()),
                Updates.set("data", updateEdital.getData()),
                Updates.set("descricao", updateEdital.getDescricao()),
                Updates.set("calendario", updateEdital.getCalendario()),
                Updates.set("preRequisitos", updateEdital.getPreRequisitos()),
                Updates.set("formInscricao", updateEdital.getFormInscricao()),
                Updates.set("coordenador", coordenador.getId())

        );

        collection.updateOne(filtro, updates);
    }

    // Exibir Detalhes
    public Edital buscarEditalPorId(String id){
        Document doc = collection.find(Filters.eq("_id", id)).first();

        if (doc != null){
            return documentToEdital(doc);
        }else {
            return null;
        }
    }

    public Edital documentToEdital(Document doc){
        Edital edital = new Edital();
        edital.setId(doc.getString("_id"));
        edital.setTitulo(doc.getString("titulo"));
        edital.setData(doc.getString("data"));
        edital.setDescricao(doc.getString("descricao"));
        edital.setCalendario(doc.getString("calendario"));
        edital.setPreRequisitos(doc.getString("preRequisitos"));
        edital.setFormInscricao(doc.getString("formInscricao"));

        //Se tiver coordenador
        ObjectId coordenadorId = doc.getObjectId("coordenador");
        if (coordenadorId != null){
            Participante coordenador = participanteService.buscarParticipantePorId(String.valueOf(coordenadorId)) // Testando
                    .orElse(null);
            edital.setCoordenador(coordenador);
        }

        return edital;
    }

    public Document editalToDocument(Edital edital){
        Document doc = new Document();
        if (edital.getId() != null){
            doc.put("id", new ObjectId(edital.getId()));
        }

        doc.put("titulo", edital.getTitulo());
        doc.put("data", edital.getData());
        doc.put("descricao", edital.getDescricao());
        doc.put("calendario", edital.getCalendario());
        doc.put("preRequisitos", edital.getPreRequisitos());
        doc.put("formInscricao", edital.getFormInscricao());

        return doc;
    }
}
