package za.co.wethinkcode.weshare.routing2;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import kong.unirest.json.JSONObject;
import za.co.wethinkcode.weshare.db2.DataRepository;
import za.co.wethinkcode.weshare.model2.Expense;
import za.co.wethinkcode.weshare.model2.Person;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

/**
 * Controller for handling API calls for Expenses
 */
public class CaptureExpenseController {
    public static final String PATH = "/expenses";
    public static final String EXPENSE_SERVER = "http://localhost:5000";


    public static void createExpense(Context context) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("inside the create expense");
        //1. collect the data from  post request
        Person currentPerson  = context.sessionAttribute("user");

//        HttpRequest request = HttpRequest.newBuilder().uri(new URI(EXPENSE_SERVER +"/"+currentPerson.getEmail())).GET().build();
//        HttpResponse<String> response = HttpClient
//                .newBuilder()
//                .build()
//                .send(request, HttpResponse.BodyHandlers.ofString());

        String description = context.formParam("description");
        context.cookie("JSESSIONID");
        double amount;
        try {
            amount = Double.parseDouble(Objects.requireNonNull(context.formParam("amount")));
        } catch (NumberFormatException e){
            context.status(HttpCode.BAD_REQUEST);
            context.result("Invalid amount specified");
            return;
        }
        LocalDate date;
        try {
            date = LocalDate.parse(Objects.requireNonNull(context.formParam("date")));
        } catch (DateTimeException e){
            context.status(HttpCode.BAD_REQUEST);
            context.result("Invalid due date specified");
            return;
        }
//
        //2. create a hashmap and turn it into a json and then send it with body
        HashMap<String,String> formData = new HashMap<>();
        formData.put("user", String.valueOf(currentPerson));
        formData.put("amount", String.valueOf(amount));
        formData.put("description", description);
        formData.put("date", String.valueOf(date));

        JSONObject formAsJsonObject = new JSONObject(formData);
        System.out.println("THis is the json string"+ formAsJsonObject);

        //3. send a request and include the JSON string as the body
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:5000/expenses"))
                .header("user", currentPerson.getEmail())
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(formAsJsonObject))).build();

        System.out.println("Body publisher prints out"+request.bodyPublisher());
        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println("done");

        //4. save in the WebDatabase
        DataRepository.getInstance().addExpense(new Expense(amount,date,description,currentPerson));

        //5. redirect to homepage
        context.redirect("/home");
    }

    public static void getExpenses(Context context) throws URISyntaxException, IOException, InterruptedException {

        context.queryParam("email");
//        1. collect the email address from the request
        String email = context.pathParamAsClass("email", String.class).get();
        System.out.println("youre in getExpenses:the email is"+email);

//        2. send a web domain request to expense domain and pass the email as parameter
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(EXPENSE_SERVER+"expenses?email="+email)).GET().build();
        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

//         3. If request is a success,Handle the response and desirialise it
        if (response.statusCode() == 200){


        }

    }


}



//1. create the endpoint
//2. create the controller
//3. get the email address from Context(the url)
//4. send a GET method to the expenses domain with the email address as a param.
//
//        RESPONSE
//5. check that the request goes to the doimain,recognises the endpoint and get inside the controller.
//        6. make the controller return a json object
//        7. deserialize the json object
//        8.


