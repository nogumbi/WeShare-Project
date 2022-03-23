package za.co.wethinkcode.weshare.expense;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import kong.unirest.Unirest;
import za.co.wethinkcode.weshare.app.db.DataRepository;
import za.co.wethinkcode.weshare.app.model.Expense;
import za.co.wethinkcode.weshare.app.model.Person;

import java.net.http.HttpResponse;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Controller for handling API calls for Expenses
 */
public class CaptureExpenseController {
    public static final String PATH = "/expenses";

    public static void createExpense(Context context){
        System.out.println("dsssssddddddddddd");


    }

//        Person currentPerson = context.sessionAttribute("user");
//
//        //TODO proper server-side validation of form params
//        String description = context.formParam("description");
//        double amount;
//        try {
//            amount = Double.parseDouble(Objects.requireNonNull(context.formParam("amount")));
//        } catch (NumberFormatException e){
//            context.status(HttpCode.BAD_REQUEST);
//            context.result("Invalid amount specified");
//            return;
//        }
//        LocalDate date;
//        try {
//            date = LocalDate.parse(Objects.requireNonNull(context.formParam("date")));
//        } catch (DateTimeException e){
//            context.status(HttpCode.BAD_REQUEST);
//            context.result("Invalid due date specified");
//            return;
//        }
//
//        Expense expense = new Expense( amount, date, description, currentPerson );
//        DataRepository.getInstance().addExpense(expense);
//
//        context.redirect("/home");
//    }
}