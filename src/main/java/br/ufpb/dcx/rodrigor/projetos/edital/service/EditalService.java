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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class EditalService extends AbstractService {
    private final MongoCollection<Document> collection;
    private final ParticipanteService participanteService;

    private static final Logger logger = LogManager.getLogger();


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
        edital.setId(doc.getObjectId("_id").toString());
    }

    // Remover
    public void removerEdital(String id){
        collection.deleteOne(eq("_id", new ObjectId(id)));
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
                Updates.set("coordenador", new ObjectId(coordenador.getId().toString()))  // Adiciona corretamente o ObjectId do coordenador
        );

        collection.updateOne(filtro, updates);
    }

    // Exibir Detalhes
    public Optional<Edital> buscarEditalPorId(String id) {
        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(doc).map(this::documentToEdital);
    }

    public Edital documentToEdital(Document doc){
        Edital edital = new Edital();
        edital.setId(doc.getObjectId("_id").toString());
        edital.setTitulo(doc.getString("titulo"));
        edital.setData(doc.getString("data"));
        edital.setDescricao(doc.getString("descricao"));
        edital.setCalendario(doc.getString("calendario"));
        edital.setPreRequisitos(doc.getString("preRequisitos"));
        edital.setFormInscricao(doc.getString("formInscricao"));

        //Se tiver coordenador
        ObjectId coordenadorId = doc.getObjectId("coordenador");

        if (coordenadorId == null) {
            logger.warn("Edital '{}' não possui coordenador", edital.getTitulo());
        } else {
            Participante coordenador = participanteService.buscarParticipantePorId(coordenadorId.toString())
                    .orElse(null);
            edital.setCoordenador(coordenador);
        }

        return edital;
    }

    public Document editalToDocument(Edital edital){
        Document doc = new Document();
        if (edital.getId() != null){
            doc.put("_id", new ObjectId(edital.getId()));
        }

        doc.put("titulo", edital.getTitulo());
        doc.put("data", edital.getData());
        doc.put("descricao", edital.getDescricao());
        doc.put("calendario", edital.getCalendario());
        doc.put("preRequisitos", edital.getPreRequisitos());
        doc.put("formInscricao", edital.getFormInscricao());

        if (edital.getCoordenador() != null) {
            doc.put("coordenador", new ObjectId((edital.getCoordenador().getId().toString())));
        }
        return doc;
    }
}
