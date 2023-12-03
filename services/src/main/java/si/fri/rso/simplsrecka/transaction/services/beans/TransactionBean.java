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

    @Timed
    public List<Transaction> getTransactionsFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).build();
        return JPAUtils.queryEntities(em, TransactionEntity.class, queryParameters).stream()
                .map(TransactionConverter::toDto).collect(Collectors.toList());
    }

    public Transaction getTransaction(Integer id) {
        TransactionEntity transactionEntity = em.find(TransactionEntity.class, id);
        if (transactionEntity == null) {
            throw new NotFoundException();
        }
        return TransactionConverter.toDto(transactionEntity);
    }

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

    public Transaction updateTransaction(Integer id, Transaction transaction) {
        TransactionEntity entity = em.find(TransactionEntity.class, id);
        if (entity == null) {
            return null;
        }
        TransactionEntity updatedEntity = TransactionConverter.toEntity(transaction);
        try {
            beginTx();
            updatedEntity.setId(entity.getId());
            updatedEntity = em.merge(updatedEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        return TransactionConverter.toDto(updatedEntity);
    }

    public boolean deleteTransaction(Integer id) {
        TransactionEntity transaction = em.find(TransactionEntity.class, id);
        if (transaction != null) {
            try {
                beginTx();
                em.remove(transaction);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else {
            return false;
        }
        return true;
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