package za.co.wethinkcode.weshare.login2;

import io.javalin.http.Context;
import za.co.wethinkcode.weshare.db2.DataRepository;
import za.co.wethinkcode.weshare.model2.NettExpensesController;
import za.co.wethinkcode.weshare.model2.Person;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginController {
    public static final String LOGIN_PATH = "/login";
    public static final String LOGOUT_PATH = "/logout";
    public static final String ROOT_PATH = "/index.html";
    public static final String EXPENSE_SERVER = "http://localhost:5000/";

    public static void handleLogin(Context context) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("in login");
        String username = context.formParam("email");
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(EXPENSE_SERVER+"expenses/"+username)).GET().build();
        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        if (username == null) {
            context.redirect(ROOT_PATH);
            return;
        }

        final Person person = DataRepository.getInstance().addPerson(new Person(username));
        context.sessionAttribute("user", person);

        context.redirect(NettExpensesController.PATH);
    }

    public static void handleLogout(Context context) {
        context.sessionAttribute("user", null);
        context.redirect(ROOT_PATH);
    }
}
