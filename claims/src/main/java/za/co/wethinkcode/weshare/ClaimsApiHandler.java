package za.co.wethinkcode.weshare;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.weshare.app.db.DataRepository;
import za.co.wethinkcode.weshare.app.db.memory.MemoryDb;
import za.co.wethinkcode.weshare.app.model.Claim;
import za.co.wethinkcode.weshare.app.model.Expense;
import za.co.wethinkcode.weshare.app.model.Settlement;

import java.util.Optional;
import java.util.UUID;

public class ClaimsApiHandler {

    private static final DataRepository db = new MemoryDb();

    public static void getOne(Context context) {
        /*
        **If id is malformed then BAD Request is returned.
        **and if claim is not found then NOT_FOUND is returned
        **else return the claim.
        */
        UUID id = UUID.fromString(context.formParam("id"));
        if (id == null) {
            context.status(HttpCode.BAD_REQUEST);
        }
        else {
            Optional<Claim> claim = db.getClaim(id);
            if (!claim.isPresent()) {
                context.status(HttpCode.NOT_FOUND);
            }
            else {
                context.json(claim);
            }
        }
    }

    public static void create(Context context) {
        Claim claim = context.bodyAsClass(Claim.class);
        Claim newClaim = db.addClaim(claim);
        if (newClaim != null) {
            context.header("Location", "/claim/" + newClaim.getId());
            context.status(HttpCode.CREATED);
            context.json(newClaim);
        }
        else {
            context.status(HttpCode.BAD_REQUEST);
        }
        
    }

   public static void update(Context context) {
    Claim claim = context.bodyAsClass(Claim.class);
    Claim updateClaim = db.updateClaim(claim);
       if (updateClaim != null) {
           context.status(HttpCode.OK);
           context.json(updateClaim);
       }
       else {
           context.status(HttpCode.BAD_REQUEST);
       }
   }

    public static void settle(Context context) {
        
    }
}
