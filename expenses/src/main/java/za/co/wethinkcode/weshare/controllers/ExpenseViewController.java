package za.co.wethinkcode.weshare.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import kong.unirest.JsonNode;
import org.apache.http.protocol.HTTP;
import za.co.wethinkcode.weshare.app.db.ExpenseDataRepository;
import za.co.wethinkcode.weshare.app.model.Expense;
import za.co.wethinkcode.weshare.app.model.ExpenseViewModel;
import za.co.wethinkcode.weshare.app.model.Person;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ExpenseViewController {

    public static final String EXPENSES_PATH = "/expenses";
    public static final String USER_EXPENSES_PATH = "/expenses/{id}";

    private static Double amount;
    private static LocalDate date;

    public static void getAllExpenses(Context context) {

        try {
            Person person = new Person(context.queryParam("email"));

            ExpenseDataRepository expenseDataRepository = ExpenseDataRepository.getInstance();
            ImmutableList<Expense> expenses = expenseDataRepository.findExpensesPaidBy(person);

//            for (Expense expense:
//                    expenses) {
//                System.out.println(expense);
//
//            }
//            Expense expense1 = expenses.get(0);
//            System.out.println(expense1);

            if (!context.queryParam("email").isBlank()) {
                context.result(expenses.toString());
            } else {
                context.status(HttpCode.BAD_REQUEST);
            }

        } catch (NullPointerException e) {
            context.status(HttpCode.BAD_REQUEST);
        }
    }

    public static void getExpense(Context context) {

        ExpenseDataRepository expenseDataRepository = ExpenseDataRepository.getInstance();
        try {
            UUID id = UUID.fromString(context.pathParam("id"));
            try {
                Expense expense = expenseDataRepository.getExpense(id).get();
                ExpenseViewModel expenseViewModel = new ExpenseViewModel(expense.getDate().toString(),
                        expense.getDescription(), expense.getPaidBy().getEmail(),
                        expense.getId().toString(), expense.getAmount());
                context.json(expenseViewModel);
            } catch (Exception e) {
                context.status(HttpCode.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            context.status(HttpCode.BAD_REQUEST);
        }
    }

    public static void createExpense(Context context) {

        try {
            Expense expense = context.bodyAsClass(Expense.class);
            ExpenseDataRepository expenseDataRepository = ExpenseDataRepository.getInstance();
            if (expenseDataRepository.getExpense(expense.getId()) == null) {
                expenseDataRepository.addExpense(expense);
            } else {
                expenseDataRepository.updateExpense(expense);
            }
            context.status(HttpCode.CREATED);
        } catch (Exception e) {
            context.status(HttpCode.BAD_REQUEST);
        }

    }
}
