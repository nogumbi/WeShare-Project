package za.co.wethinkcode.weshare.claim2;

import com.google.gson.JsonObject;
import io.javalin.http.Context;
import kong.unirest.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import za.co.wethinkcode.weshare.db2.DataRepository;
import za.co.wethinkcode.weshare.model2.Claim;
import za.co.wethinkcode.weshare.model2.Expense;
import za.co.wethinkcode.weshare.model2.Person;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller for handling API calls for Claims
 */
public class ClaimsApiController {
    public static final String PATH = "/api/claims";

    public static void create(@NotNull Context context) {
        ClaimViewModel claimViewModel = context.bodyAsClass(ClaimViewModel.class);
        Expense expense = context.sessionAttribute("expense");

        String claimFromEmail = claimViewModel.getClaimFromWho();

        Person p = DataRepository.getInstance().addPerson(new Person(claimFromEmail));
        Claim c = expense.createClaim(p, claimViewModel.getClaimAmount(), claimViewModel.dueDateAsLocalDate());
        DataRepository.getInstance().addClaim(c);
        DataRepository.getInstance().updateExpense(expense);

        Optional<Expense> maybeExpense = DataRepository.getInstance().getExpense(expense.getId());
        expense = maybeExpense.orElseThrow(() -> new RuntimeException("Could not reload expense"));
        context.sessionAttribute("expense", expense);

        claimViewModel.setId(expense.getClaims().size());
        context.status(201);
        context.json(claimViewModel);
    }

    public static void createClaim(@NotNull Context context) throws URISyntaxException, IOException, InterruptedException {

        ClaimViewModel claimViewModel = context.bodyAsClass(ClaimViewModel.class);

        String claimFromWho = claimViewModel.getClaimFromWho();
        Double claimAmount = claimViewModel.getClaimAmount();
        LocalDate dueDate = claimViewModel.dueDateAsLocalDate();

        System.out.println("Congradualitions you are inside createClaim in the Webserver");

        Person currentPerson = context.sessionAttribute("user");

        //1. Collect information from the claims form
//        String claimFromWho = context.formParam("claimFromWho");

        System.out.println(claimFromWho);
        System.out.println("after current claimFromWho");
//        Double claimAmount = Double.parseDouble(Objects.requireNonNull(context.formParam("amount")));

        System.out.println("after current claimAmount");
//        LocalDate dueDate = LocalDate.parse(Objects.requireNonNull(context.formParam("dueDate")));;

        System.out.println("after current Local date");

        System.out.println("data has been collected");

        //2. Store information in a hashmap
        HashMap<String,String> claimData = new HashMap<>();
        claimData.put("claimFromWho",claimFromWho);
        claimData.put("claimAmount", String.valueOf(claimAmount));
        claimData.put("dueDate", String.valueOf(dueDate));

        System.out.println("data is now in the hashmap");
        //3. Turn the hashmap into a Json object

        JSONObject claimDataAsJson = new JSONObject(claimData);

        System.out.println("data is now converted to json");
        //4. Send a request to claims domain with json as body

        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:5000/claim"))
                .header("user", currentPerson.getEmail())
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(claimDataAsJson))).build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

        context.status(200);
        context.json(claimViewModel);
//
    }
}