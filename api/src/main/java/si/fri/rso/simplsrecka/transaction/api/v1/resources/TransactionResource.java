package si.fri.rso.simplsrecka.transaction.api.v1.resources;

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
import si.fri.rso.simplsrecka.transaction.lib.Transaction;
import si.fri.rso.simplsrecka.transaction.services.beans.TransactionBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;


@Log
@ApplicationScoped
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private Logger log = Logger.getLogger(TransactionResource.class.getName());

    @Inject
    private TransactionBean transactionBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all transactions.", summary = "Get all transactions")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of transactions",
                    content = @Content(schema = @Schema(implementation = Transaction.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of transactions in list")}
            )
    })
    @GET
    public Response getTransactions() {
        List<Transaction> transactions = transactionBean.getAllTransactions();
        return Response.status(Response.Status.OK).entity(transactions).build();
    }

    @Operation(description = "Get details for a specific transaction.", summary = "Get transaction details")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Transaction details",
                    content = @Content(schema = @Schema(implementation = Transaction.class))
            ),
            @APIResponse(responseCode = "404", description = "Transaction not found")
    })
    @GET
    @Path("/{transactionId}")
    public Response getTransaction(@Parameter(description = "Transaction ID.", required = true)
                                   @PathParam("transactionId") Integer transactionId) {
        Transaction transaction = transactionBean.getTransaction(transactionId);
        if (transaction == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(transaction).build();
    }

    @Operation(description = "Add a new transaction.", summary = "Add transaction")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Transaction successfully added"),
            @APIResponse(responseCode = "400", description = "Invalid transaction data")
    })
    @POST
    public Response createTransaction(@RequestBody(description = "DTO object with transaction details.",
            required = true,
            content = @Content(schema = @Schema(implementation = Transaction.class)))
                                          Transaction transaction) {
        if (transaction == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        transaction = transactionBean.createTransaction(transaction);
        return Response.status(Response.Status.CREATED).entity(transaction).build();
    }

    @Operation(description = "Update details for a specific transaction.", summary = "Update transaction")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Transaction successfully updated"),
            @APIResponse(responseCode = "404", description = "Transaction not found"),
            @APIResponse(responseCode = "400", description = "Invalid transaction data")
    })
    @PUT
    @Path("/{transactionId}")
    public Response updateTransaction(@Parameter(description = "Transaction ID.", required = true)
                                      @PathParam("transactionId") Integer transactionId,
                                      @RequestBody(description = "DTO object with updated transaction details.",
                                              required = true,
                                              content = @Content(schema = @Schema(implementation = Transaction.class)))
                                      Transaction transaction) {
        if (transaction == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Transaction updatedTransaction = transactionBean.updateTransaction(transactionId, transaction);
        if (updatedTransaction == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(updatedTransaction).build();
    }

    @Operation(description = "Delete a specific transaction.", summary = "Delete transaction")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Transaction successfully deleted"),
            @APIResponse(responseCode = "404", description = "Transaction not found")
    })
    @DELETE
    @Path("/{transactionId}")
    public Response deleteTransaction(@Parameter(description = "Transaction ID.", required = true)
                                      @PathParam("transactionId") Integer transactionId) {
        boolean deleted = transactionBean.deleteTransaction(transactionId);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}