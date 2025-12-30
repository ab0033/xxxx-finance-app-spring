package com.finance.payment.repository;

import com.finance.enums.PaymentProvider;
import com.finance.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringPaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByProviderAndProviderPaymentId(
            PaymentProvider provider,
            String providerPaymentId
    );

    Optional<PaymentEntity> findByIdempotencyKey(UUID idempotencyKey);

    @Modifying
    @Query("""
                update PaymentEntity p
                set p.providerPaymentId = :providerPaymentId,
                    p.providerPaymentStatus = :providerPaymentStatus
                where p.id = :id
            """)
    void attachProviderPaymentId(
            @Param("id") UUID id,
            @Param("providerPaymentId") String providerPaymentId,
            @Param("providerPaymentStatus") String providerPaymentStatus
    );

}
