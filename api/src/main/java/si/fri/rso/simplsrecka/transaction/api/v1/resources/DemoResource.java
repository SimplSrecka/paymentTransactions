package si.fri.rso.simplsrecka.transaction.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import si.fri.rso.simplsrecka.transaction.lib.CombinedTransactionLotteryResult;
import si.fri.rso.simplsrecka.transaction.services.beans.TransactionBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/demo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {

    private Logger log = Logger.getLogger(DemoResource.class.getName());

    @Inject
    private TransactionBean transactionBean;

    @GET
    @Path("/{userId}")
    public Response demo(@Parameter(description = "User ID.", required = true)
                                    @PathParam("userId") Integer userId) {
        try {
            List<CombinedTransactionLotteryResult> transactions = transactionBean.demo(userId);
            if (transactions.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No transactions found for user ID: " + userId).build();
            }
            return Response.ok(transactions).build();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error fetching transactions for user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/fallback/{userId}")
    public Response demo_fallback(@Parameter(description = "User ID.", required = true)
                         @PathParam("userId") Integer userId) {
        try {
            List<CombinedTransactionLotteryResult> transactions = transactionBean.demo_fallback(userId);
            if (transactions.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No transactions found for user ID: " + userId).build();
            }
            return Response.ok(transactions).build();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error fetching transactions for user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
