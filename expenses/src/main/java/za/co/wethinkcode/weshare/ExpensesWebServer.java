package za.co.wethinkcode.weshare;

import io.javalin.Javalin;
import za.co.wethinkcode.weshare.controllers.ExpenseViewController;
import za.co.wethinkcode.weshare.controllers.PersonController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ExpensesWebServer {
    private static final int DEFAULT_PORT = Integer.parseInt(System.getenv("EXPENSE_PORT"));
    private final Javalin app;

    public static void main(String[] args) {
        ExpensesWebServer server = new ExpensesWebServer();
        server.start(DEFAULT_PORT);
    }

    private void start(int defaultPort) {
        app.start(defaultPort);
    }

    public ExpensesWebServer() {
        app = createAndConfigureServer();
        setUpRoutes(app);
    }

    private void setUpRoutes(Javalin app) {
        app.routes(() -> {
            expensesRoutes();
            expenseRoute();
            personRoute();
        });
    }

    private void expenseRoute() {
        path(ExpenseViewController.USER_EXPENSES_PATH, () -> {
            get(ExpenseViewController::getExpense);
        });
    }

    private void personRoute() {
        path(PersonController.PATH, () -> {
            get(PersonController::getTotals);
        });
    }

    private void expensesRoutes() {
        path(ExpenseViewController.EXPENSES_PATH, () -> {
            get(ExpenseViewController::getAllExpenses);
            post(ExpenseViewController::createExpense);
        });
    }

    private Javalin createAndConfigureServer() {
        return Javalin.create();
    }
}