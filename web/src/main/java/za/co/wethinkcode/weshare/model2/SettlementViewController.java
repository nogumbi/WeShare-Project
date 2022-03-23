package za.co.wethinkcode.weshare.model2;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.weshare.db2.DataRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

/**
 * Controller for handling calls from form submits for Claims
 */
public class SettlementViewController {
    public static final String PATH = "/settleclaim";

    public static void renderSettleClaimForm(Context context){
        UUID claimId = UUID.fromString(Objects.requireNonNull(context.queryParam("claimId")));

        Optional<Claim> maybeClaim = DataRepository.getInstance().getClaim(claimId);
        maybeClaim.ifPresent(c -> context.render("settleclaim.html", Map.of("claim", c)));

        if (maybeClaim.isEmpty()) {
            context.status(HttpCode.BAD_REQUEST);
            context.result("Invalid claim");
        }
    }

    public static void submitSettlement(Context context) {
        UUID claimId = UUID.fromString(Objects.requireNonNull(context.formParam("id")));
        Optional<Claim> maybeClaim = DataRepository.getInstance().getClaim(claimId);
        if (maybeClaim.isEmpty()) {
            context.status(HttpCode.BAD_REQUEST);
            context.result("Invalid claim");
            return;
        }
        Claim claim = maybeClaim.get();
        Settlement settlement = claim.settleClaim(LocalDate.now());
        DataRepository.getInstance().addSettlement(settlement);
        DataRepository.getInstance().updateClaim(claim);

        context.redirect("/home");
    }

    public static void submitNewSettlement(Context context) throws URISyntaxException, IOException, InterruptedException {

        Person currentPerson  = context.sessionAttribute("user");

        //1. Collect form data
        UUID claimId = UUID.fromString(Objects.requireNonNull(context.formParam("id")));
        String email = context.formParam("claimedBy");
//
        LocalDate dueDate = LocalDate.parse(Objects.requireNonNull(context.formParam("duedate")));

        String description = context.formParam("description");

        Double claimAmount = Double.parseDouble(Objects.requireNonNull(context.formParam("claimAmount")));

        HashMap<String,String> claimDataInHash = new HashMap<>();
        claimDataInHash.put("claimId", String.valueOf(claimId));
        claimDataInHash.put("dueDate", String.valueOf(dueDate));
        claimDataInHash.put("email",email);
        claimDataInHash.put("description",description);
        claimDataInHash.put("claimAmount", String.valueOf(claimAmount));

        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:5000/settlement"))
                .header("user", currentPerson.getEmail())
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(claimDataInHash))).build();


        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());



//            <form enctype="application/x-www-form-urlencoded" method="post" th:action="@{/settleclaim}">
//                <input type="hidden" name="id" th:value="${claim.id}">
//                <label for="email">You want to settle a claim from</label>
//                <input readonly disabled id="email" type="email" name="claimedBy" th:value="${claim.claimedBy.email}"/>
//                <label for="due_date">thas was due on</label>
//                <input readonly disabled name="duedate" id="due_date" type="date" th:value="${claim.dueDate}" required>
//                <label for="description">for</label>
//                <input readonly disabled id="description" type="text" name="description"
//        th:value="${claim.expense.description}">
//                <label for="claim_amount">You owe</label>
//                <input readonly disabled id="claim_amount" type="number" name="claimAmount"
//        th:value="${#numbers.formatDecimal(claim.amount,1,2,'POINT')}">
//                <input id="settle" type="submit" value="Settle"/>
//            </form>

    }
}