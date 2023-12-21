package si.fri.rso.simplsrecka.transaction.models.converters;

import si.fri.rso.simplsrecka.transaction.models.entities.TransactionEntity;
import si.fri.rso.simplsrecka.transaction.lib.Transaction;

public class TransactionConverter {

    public static Transaction toDto(TransactionEntity entity) {
        Transaction dto = new Transaction();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setTicketId(entity.getTicketId());
        dto.setAmount(entity.getAmount());
        dto.setPaidCombination(entity.getPaidCombination());
        dto.setType(entity.getType().toString());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setDrawDate(entity.getDrawDate());

        return dto;
    }

    public static TransactionEntity toEntity(Transaction dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setUserId(dto.getUserId());
        entity.setTicketId(dto.getTicketId());
        entity.setAmount(dto.getAmount());
        entity.setPaidCombination(dto.getPaidCombination());
        entity.setType(TransactionEntity.TransactionType.valueOf(dto.getType()));
        entity.setTransactionDate(dto.getTransactionDate());
        entity.setDrawDate(dto.getDrawDate());

        return entity;
    }

}