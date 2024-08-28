package br.ufpb.dcx.rodrigor.projetos.edital.services;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class EditalService extends AbstractService {
    private final MongoCollection<Document> collection;

    public EditalService(MongoDBConnector mongoDBConnector) {
        super(mongoDBConnector);
        MongoDatabase database = mongoDBConnector.getDatabase("editais");
        this.collection = database.getCollection("editais"); //Precisa conectar, não estou conseguindo
    }
    
}
