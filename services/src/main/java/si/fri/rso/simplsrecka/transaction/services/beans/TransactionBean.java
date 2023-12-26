package si.fri.rso.simplsrecka.transaction.services.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.ArrayList;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;

import si.fri.rso.simplsrecka.transaction.lib.Transaction;
import si.fri.rso.simplsrecka.transaction.lib.LotteryResult;
import si.fri.rso.simplsrecka.transaction.lib.CombinedTransactionLotteryResult;
import si.fri.rso.simplsrecka.transaction.models.converters.TransactionConverter;
import si.fri.rso.simplsrecka.transaction.models.entities.TransactionEntity;


@RequestScoped
public class TransactionBean {

    private Logger log = Logger.getLogger(TransactionBean.class.getName());

    private Client httpClient;
    private String APITransactionURL;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        APITransactionURL = "http://52.226.192.46/lottery-drawing-results";
    }

    @Inject
    private EntityManager em;

    public List<Transaction> getAllTransactions() {
        TypedQuery<TransactionEntity> query = em.createNamedQuery("TransactionEntity.getAll", TransactionEntity.class);
        List<TransactionEntity> resultList = query.getResultList();
        return resultList.stream().map(TransactionConverter::toDto).collect(Collectors.toList());
    }

    //@Timed(name = "get_user_transactions")
    @Timeout(value = 10, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "getTransactionsFallback")
    public List<CombinedTransactionLotteryResult> getTransactions(Integer userId) {
        TypedQuery<TransactionEntity> query = em.createNamedQuery("TransactionEntity.getByUserId", TransactionEntity.class);
        query.setParameter("userId", userId);
        List<TransactionEntity> resultList = query.getResultList();

        List<CombinedTransactionLotteryResult> combinedResults = new ArrayList<>();

        for (TransactionEntity transaction : resultList) {
            try {
                String url = APITransactionURL + "/v1/lotteryResults/" + transaction.getTicketId() + "?drawingDate=" +
                        transaction.getDrawDate().toString();
                System.out.println(url);
                List<LotteryResult> lotteryResults = httpClient
                        .target(url)
                        .request()
                        .get(new GenericType<List<LotteryResult>>() {});

                LotteryResult res = lotteryResults.get(0);
                if (res.getTicketId() == transaction.getTicketId()
                        && res.getDrawingDate().equals(transaction.getDrawDate().toString())) {
                    CombinedTransactionLotteryResult combined = combineData(transaction, res);
                    combinedResults.add(combined);
                } else {
                    LotteryResult emp = new LotteryResult();
                    CombinedTransactionLotteryResult combined = combineData(transaction, emp);
                    combinedResults.add(combined);
                }
            } catch (WebApplicationException e) {
                if (e.getResponse().getStatus() == 404) {
                    log.info("No lottery drawing found for ticketId: " + transaction.getTicketId());
                    continue;
                } else {
                    log.severe(e.getMessage());
                    throw new InternalServerErrorException(e);
                }
            } catch (ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }

        return combinedResults;
    }

    @Timeout(value = 10, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "getTransactionsFallback")
    public List<CombinedTransactionLotteryResult> demo(Integer userId) {
        TypedQuery<TransactionEntity> query = em.createNamedQuery("TransactionEntity.getByUserId", TransactionEntity.class);
        query.setParameter("userId", userId);
        List<TransactionEntity> resultList = query.getResultList();

        List<CombinedTransactionLotteryResult> combinedResults = new ArrayList<>();

        for (TransactionEntity transaction : resultList) {
            try {
                String url = APITransactionURL + "/v1/lotteryResultssss/" + transaction.getTicketId() + "?drawingDate=" +
                        transaction.getDrawDate().toString();
                System.out.println(url);
                List<LotteryResult> lotteryResults = httpClient
                        .target(url)
                        .request()
                        .get(new GenericType<List<LotteryResult>>() {});

                LotteryResult res = lotteryResults.get(0);
                if (res.getTicketId() == transaction.getTicketId()
                        && res.getDrawingDate().equals(transaction.getDrawDate().toString())) {
                    CombinedTransactionLotteryResult combined = combineData(transaction, res);
                    combinedResults.add(combined);
                }
            } catch (ProcessingException | WebApplicationException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }

        return combinedResults;
    }

    public List<CombinedTransactionLotteryResult> getTransactionsFallback(Integer userId) {
        System.out.println("fallback");
        return new ArrayList<>();
    }

    //@Metered(name = "create_transaction")
    public Transaction createTransaction(Transaction transaction) {
        TransactionEntity transactionEntity = TransactionConverter.toEntity(transaction);
        try {
            beginTx();
            em.persist(transactionEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        if (transactionEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }
        return TransactionConverter.toDto(transactionEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    private CombinedTransactionLotteryResult combineData(TransactionEntity transaction, LotteryResult lotteryResult) {
        CombinedTransactionLotteryResult combined = new CombinedTransactionLotteryResult();

        combined.setAmount(transaction.getAmount());
        combined.setDrawDate(transaction.getDrawDate().toString());
        combined.setId(transaction.getId());
        combined.setPaidCombination(transaction.getPaidCombination());
        combined.setTicketId(transaction.getTicketId());
        combined.setTransactionDate(transaction.getTransactionDate().toString());
        combined.setType(transaction.getType().toString());
        combined.setUserId(transaction.getUserId());

        combined.setLotteryDrawingDate(lotteryResult.getDrawingDate());
        combined.setLotteryId(lotteryResult.getId());
        combined.setLotteryCategory(lotteryResult.getLotteryCategory());
        combined.setTotalPrize(lotteryResult.getTotalPrize());
        combined.setWinningCombination(lotteryResult.getWinningCombination());

        return combined;
    }

}