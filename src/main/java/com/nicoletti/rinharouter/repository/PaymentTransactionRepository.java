package com.nicoletti.rinharouter.repository;

import com.nicoletti.rinharouter.dto.PaymentSummary;
import com.nicoletti.rinharouter.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    @Query("""
                SELECT new com.nicoletti.rinharouter.dto.PaymentSummary(
                    pt.endpointType,
                    COUNT(pt.id),
                    SUM(pt.amount)
                )
                FROM PaymentTransaction pt
                WHERE pt.processedAt BETWEEN :from AND :to
                GROUP BY pt.endpointType
            """)
    List<PaymentSummary> summarizeByEndpoint(@Param("from") LocalDateTime from,
                                             @Param("to") LocalDateTime to);

}
