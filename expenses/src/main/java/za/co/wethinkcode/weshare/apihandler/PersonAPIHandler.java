package za.co.wethinkcode.weshare.apihandler;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.weshare.app.db.ExpenseDataRepository;
import za.co.wethinkcode.weshare.app.model.Person;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PersonAPIHandler {

    private HttpClient client;
    private static Context context;
    private static Double totalExpenses;
    private static Double totalSettledClaims;
    private static Double totalUnsettledClaims;
    private static ExpenseDataRepository expenseDataRepository = null;

    public PersonAPIHandler(Context context) {
        this.expenseDataRepository = ExpenseDataRepository.getInstance();
        this.client = HttpClient.newHttpClient();
        this.context = context;
    }

    public static Double getTotalExpenses() {
        try {
            Person person = new Person(context.pathParam("email"));
            totalExpenses = expenseDataRepository.getTotalExpensesFor(person);
            return totalExpenses;
        } catch (Exception e) {
            context.status(HttpCode.BAD_REQUEST);
        }
        return totalExpenses;
    }

    public Double getTotalUnsettledClaims(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            totalUnsettledClaims = Double.parseDouble(response.body());
        } catch (IOException | InterruptedException e) {
            context.status(HttpCode.BAD_REQUEST);
        }
        return totalUnsettledClaims;
    }

    public Double getTotalSettledClaims(String urlString) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            totalSettledClaims = Double.parseDouble(response.body());
        } catch (IOException | InterruptedException e) {
            context.status(HttpCode.BAD_REQUEST);
        }
        return totalSettledClaims;
    }

    public Double getNettExpenses(Context context) {
        double nettExpenses = expenseDataRepository.getNettExpensesFor(context.sessionAttribute("user"));
        return nettExpenses;
    }
}
