package br.ufpb.dcx.rodrigor.projetos;

import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.edital.controllers.EditalController;
import br.ufpb.dcx.rodrigor.projetos.edital.service.EditalService;
import br.ufpb.dcx.rodrigor.projetos.form.controller.FormController;
import br.ufpb.dcx.rodrigor.projetos.login.LoginController;
import br.ufpb.dcx.rodrigor.projetos.participante.controllers.ParticipanteController;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import br.ufpb.dcx.rodrigor.projetos.projeto.controllers.ProjetoController;
import br.ufpb.dcx.rodrigor.projetos.projeto.services.ProjetoService;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;

public class App {
    private static final Logger logger = LogManager.getLogger();

    private static final int PORTA_PADRAO = 8000;
    private static final String PROP_PORTA_SERVIDOR = "porta.servidor";
    private static final String PROP_MONGODB_CONNECTION_STRING = "mongodb.connectionString";

    private final Properties propriedades;
    private MongoDBConnector mongoDBConnector = null;

    public App() {
        this.propriedades = carregarPropriedades();
    }

    public void iniciar() {
        Javalin app = inicializarJavalin();
        configurarPaginasDeErro(app);
        configurarRotas(app);

        // Lidando com exceções não tratadas
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Erro não tratado", e);
            ctx.status(500);
        });

    }

    private void registrarServicos(JavalinConfig config, MongoDBConnector mongoDBConnector) {
        /* EditalService editalService = new EditalService(mongoDBConnector); */
        ParticipanteService participanteService = new ParticipanteService("http://localhost:8000");
        config.appData(Keys.PROJETO_SERVICE.key(), new ProjetoService(mongoDBConnector, participanteService));
        config.appData(Keys.EDITAL_SERVICE.key(), new EditalService(mongoDBConnector, participanteService));
        config.appData(Keys.PARTICIPANTE_SERVICE.key(), participanteService);

        /*
         * config.appData(Keys.EDITAIS_SERVICE.key(), editalService);
         */
    }

    private void configurarPaginasDeErro(Javalin app) {
        app.error(404, ctx -> ctx.render("erro_404.html"));
        app.error(500, ctx -> ctx.render("erro_500.html"));
    }

    private void getAutentication() {

    }
    private Javalin inicializarJavalin() {
        int porta = obterPortaServidor();

        logger.info("Iniciando aplicação na porta {}", porta);

        Consumer<JavalinConfig> configConsumer = this::configureJavalin;

        return Javalin.create(configConsumer).start(porta);
    }

    private void configureJavalin(JavalinConfig config) {
        TemplateEngine templateEngine = configurarThymeleaf();

        config.events(event -> {
            event.serverStarting(() -> {
                mongoDBConnector = inicializarMongoDB();
                config.appData(Keys.MONGO_DB.key(), mongoDBConnector);
                registrarServicos(config, mongoDBConnector);
            });
            event.serverStopping(() -> {
                if (mongoDBConnector == null) {
                    logger.error("MongoDBConnector não deveria ser nulo ao parar o servidor");
                } else {
                    mongoDBConnector.close();
                    logger.info("Conexão com o MongoDB encerrada com sucesso");
                }
            });
        });
        config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.directory = "/public";
            staticFileConfig.location = Location.CLASSPATH;
        });
        config.fileRenderer(new JavalinThymeleaf(templateEngine));

    }

    private int obterPortaServidor() {
        if (propriedades.containsKey(PROP_PORTA_SERVIDOR)) {
            try {
                return Integer.parseInt(propriedades.getProperty(PROP_PORTA_SERVIDOR));
            } catch (NumberFormatException e) {
                logger.error("Porta definida no arquivo de propriedades não é um número válido: '{}'",
                        propriedades.getProperty(PROP_PORTA_SERVIDOR));
                System.exit(1);
            }
        } else {
            logger.info("Porta não definida no arquivo de propriedades, utilizando porta padrão {}", PORTA_PADRAO);
        }
        return PORTA_PADRAO;
    }

    private TemplateEngine configurarThymeleaf() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    private MongoDBConnector inicializarMongoDB() {
        String connectionString = propriedades.getProperty(PROP_MONGODB_CONNECTION_STRING);
        logger.info("Lendo string de conexão ao MongoDB a partir do application.properties");
        if (connectionString == null) {
            logger.error(
                    "O string de conexão ao MongoDB não foi definido no arquivo /src/main/resources/application.properties");
            logger.error("Defina a propriedade '{}' no arquivo de propriedades", PROP_MONGODB_CONNECTION_STRING);
            System.exit(1);
        }

        logger.info("Conectando ao MongoDB");
        MongoDBConnector db = new MongoDBConnector(connectionString);
        if (db.conectado("config")) {
            logger.info("Conexão com o MongoDB estabelecida com sucesso");
        } else {
            logger.error("Falha ao conectar ao MongoDB");
            System.exit(1);
        }
        return db;
    }

    private void configurarRotas(Javalin app) {
        app.before(ctx -> {
            // Se a rota não for /login e o usuário não estiver autenticado
            if (!ctx.path().equals("/login") && ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/login");
            }
            // if (ctx.path().startsWith("/admin/novo") && usuario.getRole() != Role.ADMIN)
            // {
            // ctx.redirect("/forbidden"); //
            // }
        });
        LoginController loginController = new LoginController();
        app.get("/", ctx -> ctx.redirect("/login"));
        app.get("/login", loginController::mostrarPaginaLogin);
        app.post("/login", loginController::processarLogin);
        app.get("/logout", loginController::logout);

        app.get("/area-interna", ctx -> {
            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/login");
            } else {
                ctx.render("area_interna.html");
            }
        });

        // Rotas para o controlador de projeto
        ProjetoController projetoController = new ProjetoController();
        app.get("/projetos", projetoController::listarProjetos);
        app.get("/projetos/novo", projetoController::mostrarFormulario);
        app.post("/projetos", projetoController::adicionarProjeto);
        app.get("/projetos/{id}/remover", projetoController::removerProjeto);

        // Rotas para o controlador de participante
        ParticipanteController participanteController = new ParticipanteController();
        app.get("/participantes", participanteController::listarParticipantes);
        app.get("/participantes/novo", participanteController::mostrarFormularioCadastro);
        app.post("/participantes", participanteController::adicionarParticipante);
        app.get("/participantes/{id}/remover", participanteController::removerParticipante);

        // Rotas para o controlador de edital
        EditalController editalController = new EditalController();
        app.get("/editais", editalController::listarEditais);
        app.post("/editais", editalController::adicionarEdital);
        app.get("/editais/novo", editalController::mostrarFormulario);
        app.get("/editais/detalhe/{id}", editalController::exibirDetalhesEdital);
        app.get("/editais/{id}/remover", editalController::removerEdital);
        app.get("/editais/editar/{id}", editalController::mostrarFormularioEditar);
        app.post("/editais/editar/{id}", editalController::editarEdital);

        // Rotas para o controlador de formulários
        FormController formController = new FormController();
        app.get("/form/{formId}", formController::abrirFormulario);
        app.post("/form/{formId}", formController::validarFormulario);
    }

    private Properties carregarPropriedades() {
        Properties prop = new Properties();
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Arquivo de propriedades /src/main/resources/application.properties não encontrado");
                logger.error(
                        "Use o arquivo application.properties.examplo como base para criar o arquivo application.properties");
                System.exit(1);
            }
            prop.load(input);
        } catch (IOException ex) {
            logger.error("Erro ao carregar o arquivo de propriedades /src/main/resources/application.properties", ex);
            System.exit(1);
        }
        return prop;
    }

    public static void main(String[] args) {
        try {
            new App().iniciar();
        } catch (Exception e) {
            logger.error("Erro ao iniciar a aplicação", e);
            System.exit(1);
        }
    }
}