package si.fri.rso.simplsrecka.transaction.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import si.fri.rso.simplsrecka.transaction.lib.Transaction;
import si.fri.rso.simplsrecka.transaction.lib.CombinedTransactionLotteryResult;
import si.fri.rso.simplsrecka.transaction.services.beans.TransactionBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Log
@ApplicationScoped
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Transactions", description = "APIs for transaction operations")
@CrossOrigin(supportedMethods = "GET, POST, PUT, DELETE, HEAD, OPTIONS", allowOrigin = "*")
public class TransactionResource {

    private Logger log = LogManager.getLogger(TransactionResource.class.getName());

    @Inject
    private TransactionBean transactionBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Retrieves all transactions.", summary = "Retrieve all transactions")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Successful retrieval of transaction list",
                    content = @Content(schema = @Schema(implementation = Transaction.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Total number of transactions")}
            ),
            @APIResponse(responseCode = "500",
                    description = "Internal Server Error")
    })
    @GET
    public Response getTransactions() {
        try {
            List<Transaction> transactions = transactionBean.getAllTransactions();
            return Response.ok(transactions).build();
        } catch (Exception e) {
            log.error("Error creating transaction", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(description = "Get all transactions for a specific user.", summary = "Get user's transactions")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "All transaction for specific user",
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
            @APIResponse(responseCode = "404",
                    description = "User doesn't exists"),
            @APIResponse(responseCode = "500",
                    description = "Internal Server Error")
    })
    @GET
    @Path("/{userId}")
    public Response getTransactions(@Parameter(description = "User ID.", required = true)
                                   @PathParam("userId") Integer userId) {
        try {
            List<CombinedTransactionLotteryResult> transactions = transactionBean.getTransactions(userId);
            if (transactions.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No transactions found for user ID: " + userId).build();
            }
            return Response.ok(transactions).build();
        } catch (Exception e) {
            log.error("Error creating transaction", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(description = "Add a new transaction.", summary = "Add transaction")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Transaction successfully added"),
            @APIResponse(responseCode = "400",
                    description = "Invalid transaction data"),
            @APIResponse(responseCode = "500",
                    description = "Internal Server Error")
    })
    @POST
    public Response createTransaction(@RequestBody(description = "DTO object with transaction details.",
            required = true,
            content = @Content(schema = @Schema(implementation = Transaction.class)))
                                          Transaction transaction) {
        try {
            if (transaction == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            transaction = transactionBean.createTransaction(transaction);
            return Response.status(Response.Status.CREATED).entity(transaction).build();
        } catch (Exception e) {
            log.error("Error creating transaction", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}