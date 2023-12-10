package si.fri.rso.simplsrecka.transaction.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;

import si.fri.rso.simplsrecka.transaction.lib.Transaction;
import si.fri.rso.simplsrecka.transaction.models.converters.TransactionConverter;
import si.fri.rso.simplsrecka.transaction.models.entities.TransactionEntity;


@RequestScoped
public class TransactionBean {

    private Logger log = Logger.getLogger(TransactionBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Transaction> getAllTransactions() {
        TypedQuery<TransactionEntity> query = em.createNamedQuery("TransactionEntity.getAll", TransactionEntity.class);
        List<TransactionEntity> resultList = query.getResultList();
        return resultList.stream().map(TransactionConverter::toDto).collect(Collectors.toList());
    }

    @Timed(name = "get_user_transactions")
    public List<Transaction> getTransactions(Integer userId) {
        TypedQuery<TransactionEntity> query = em.createNamedQuery("TransactionEntity.getByUserId", TransactionEntity.class);
        query.setParameter("userId", userId);
        List<TransactionEntity> resultList = query.getResultList();
        return resultList.stream().map(TransactionConverter::toDto).collect(Collectors.toList());
    }

    @Metered(name = "create_transaction")
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
}