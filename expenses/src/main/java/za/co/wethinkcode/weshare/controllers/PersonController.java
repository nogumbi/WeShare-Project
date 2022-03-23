package za.co.wethinkcode.weshare.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.weshare.apihandler.PersonAPIHandler;

import java.util.ArrayList;
import java.util.List;

public class PersonController {


    public static final String PATH = "/person/{email}";
    private static Double totalExpenses;
    private static Double totalSettledClaims;
    private static Double totalUnsettledClaims;
    private static Double nettExpenses;

    public static List<Double> getTotals(Context context) {
        PersonAPIHandler personAPIHandler = new PersonAPIHandler(context);
        List<Double> totals = new ArrayList<>();
        totals.add(getTotalExpenses(context, personAPIHandler));
        totals.add(getTotalUnsettledClaims(context, personAPIHandler));
        totals.add(getTotalSettledClaims(context,personAPIHandler));
        totals.add(getNettExpenses(context, personAPIHandler));
        return totals;
    }

    private static Double getTotalUnsettledClaims(Context context, PersonAPIHandler personAPIHandler) {

        try {
            String urlString = "http://localhost:7070/totalclaimvalue/from/" + context.pathParam("email");
            totalUnsettledClaims = personAPIHandler.getTotalUnsettledClaims(urlString);
        } catch (Exception e){
            context.status(HttpCode.BAD_REQUEST);
        }
        return totalUnsettledClaims;
    }

    private static Double getTotalSettledClaims(Context context, PersonAPIHandler personAPIHandler) {
        try {
            String urlString = "http://localhost:7070/claims/from/" +
                    context.pathParam("email") + "?settled=" + context.queryParam("settled");
            totalSettledClaims = personAPIHandler.getTotalSettledClaims(urlString);
        } catch (Exception e) {
            context.status(HttpCode.BAD_REQUEST);
        }
        return totalSettledClaims;
    }

    private static double getTotalExpenses(Context context, PersonAPIHandler personAPIHandler) {

        System.out.println(context.pathParam("email"));
        try {
            totalExpenses = personAPIHandler.getTotalExpenses();
        } catch (Exception e){
            context.status(HttpCode.BAD_REQUEST);
        }
        return totalExpenses;
    }

    private static double getNettExpenses(Context context, PersonAPIHandler personAPIHandler) {
        try {
            nettExpenses = personAPIHandler.getNettExpenses(context.sessionAttribute("user"));
        } catch (Exception e) {
            context.status(HttpCode.BAD_REQUEST);
        }
        return nettExpenses;
    }
}
