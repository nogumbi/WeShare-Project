package za.co.wethinkcode.weshare.claim2;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.weshare.db2.DataRepository;
import za.co.wethinkcode.weshare.model2.Claim;
import za.co.wethinkcode.weshare.model2.Expense;
import za.co.wethinkcode.weshare.model2.Person;

import java.time.LocalDate;
import java.util.*;

/**
 * Controller for handling calls from form submits for Claims
 */
public class ClaimExpenseController {
    public static final String PATH = "/claimexpense";

    public static void renderClaimExpensePage(Context context){
        UUID expenseId = UUID.fromString(context.queryParam("expenseId"));

        Optional<Expense> maybeExpense = DataRepository.getInstance().getExpense(expenseId);
        if (maybeExpense.isEmpty()) {
            context.status(HttpCode.BAD_REQUEST);
            context.result("Invalid expense");
            return;
        }
        Expense expense = maybeExpense.get();
        context.sessionAttribute("expense", expense);
        context.render("claimexpense.html",
                Map.of("newClaim", new Claim( expense, new Person("email@domain.com"), 0.0, LocalDate.now() ),
                        "expense", expense));
    }
    
}