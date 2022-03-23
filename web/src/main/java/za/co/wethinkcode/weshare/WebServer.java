package za.co.wethinkcode.weshare;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import za.co.wethinkcode.weshare.model2.Sessions;
import za.co.wethinkcode.weshare.model2.*;
import za.co.wethinkcode.weshare.claim2.ClaimExpenseController;
import za.co.wethinkcode.weshare.claim2.ClaimsApiController;

import za.co.wethinkcode.weshare.routing2.CaptureExpenseController;
import za.co.wethinkcode.weshare.login2.LoginController;
//import za.co.wethinkcode.weshare.settle.SettlementViewController;

import java.time.LocalDate;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    private static final int WEB_PORT = 7070;
    private static final String STATIC_DIR = "/html";
    private static final String EXPENSES_SERVER = "http://localhost:8080";
    private static final String CLAIMS_SERVER="http://localhost:8080";
    private static final String RATINGS_SERVER=" http://localhost:8080";
    private final Javalin app;


    /**
     * The main class starts the server on the default port 7070.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start(WEB_PORT);

    }

    public WebServer() {
        configureThymeleafTemplateEngine();
        app = createAndConfigureServer();
        setupRoutes(app);
    }

    public void start(int port) {
        app.start(port);
    }

    public int port() {
        return app.port();
    }

    public void close() {
        app.close();
    }

    /**
     * Setup the Thymeleaf template engine to load templates from 'resources/templates'
     */
    private void configureThymeleafTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateEngine.setTemplateResolver(templateResolver);

        templateEngine.addDialect(new LayoutDialect());
        JavalinThymeleaf.configure(templateEngine);
    }

    private void setupRoutes(Javalin server) {
        server.routes(() -> {
            loginAndLogoutRoutes();
            homePageRoute();
            expenseRoutes();
            claimRoutes();
            settlementRoutes();
        });
    }

    private static void settlementRoutes() {
        path(SettlementViewController.PATH, () -> {
            get(SettlementViewController::renderSettleClaimForm);
            post(SettlementViewController::submitSettlement);

        });
        post("/settlement",SettlementViewController::submitNewSettlement);
    }

    private static void claimRoutes() {
        get(ClaimExpenseController.PATH, ClaimExpenseController::renderClaimExpensePage);
//        post(ClaimsApiController.PATH, ClaimsApiController::create);
        post(ClaimsApiController.PATH, ClaimsApiController::createClaim);
        post("/claim", ClaimsApiController::createClaim);
    }

    private static void expenseRoutes() {
        path("/newexpense", () -> get(context -> {
            log.info("in newexpense");
            Expense expense = new Expense(0.00, LocalDate.now(), "?", context.sessionAttribute("user"));
            context.render("expenseform.html", Map.of("expense", expense));
        }));
        get("/expenses/",CaptureExpenseController::getExpenses);
        get("/expenses/{id}",CaptureExpenseController::getExpenses);
        post(CaptureExpenseController.PATH, CaptureExpenseController::createExpense);




    }

    private void homePageRoute() {
        path(NettExpensesController.PATH, () -> get(NettExpensesController::renderHomePage));
    }

    private void loginAndLogoutRoutes() {
        post(LoginController.LOGIN_PATH, LoginController::handleLogin);
        get(LoginController.LOGOUT_PATH, LoginController::handleLogout);
    }

    @NotNull
    private Javalin createAndConfigureServer() {
        return Javalin.create(config -> {
            config.addStaticFiles(STATIC_DIR, Location.CLASSPATH);
            config.sessionHandler(Sessions::nopersistSessionHandler);
            config.accessManager(new DefaultAccessManager());
        });
    }
}