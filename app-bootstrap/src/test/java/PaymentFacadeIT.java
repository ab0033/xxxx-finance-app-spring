import com.finance.FinanceAppApplication;
import com.finance.enums.PaymentProvider;
import com.finance.enums.PaymentStatus;
import com.finance.model.Payment;
import com.finance.payment.dto.InitiatePaymentRequest;
import com.finance.payment.dto.InitiatePaymentResponse;
import com.finance.payment.service.PaymentFacade;
import com.finance.port.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = FinanceAppApplication.class)
@ActiveProfiles("test")
public class PaymentFacadeIT extends AbstractPostgresIT {
    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("SUCCESS: create payment and attach providerPaymentId")
    void should_create_payment_and_attach_provider_id() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();

        InitiatePaymentRequest dto = InitiatePaymentRequest.builder()
                .userId(userId)
                .provider(PaymentProvider.STRIPE)
                .currency("USD")
                .amount(new BigDecimal("100.00"))
                .idempotencyKey(idempotencyKey)
                .build();

        InitiatePaymentResponse response = paymentFacade.initiatePayment(dto);

        Optional<Payment> paymentOpt = paymentRepository.findByIdempotencyKey(idempotencyKey);
        assertThat(paymentOpt).isPresent();

        Payment payment = paymentOpt.get();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CREATED);
        assertThat(payment.getProvider()).isEqualTo(PaymentProvider.STRIPE);
        assertThat(payment.getAmount()).isEqualByComparingTo("100.00");
        assertThat(payment.getCurrency()).isEqualTo("USD");
        assertThat(payment.getProviderPaymentId()).isNotNull();

        assertThat(response.getStatus()).isEqualTo(PaymentStatus.CREATED);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getAmount()).isEqualTo("100.00");
        assertThat(response.getProvider()).isEqualTo(PaymentProvider.STRIPE);
        assertThat(response.getPaymentId()).isNotNull();
        assertThat(response.getProviderPaymentId()).isNotNull();
        assertThat(response.getProviderSecret()).isNotNull();
    }

    @Test
    @DisplayName("SUCCESS: mark payment as succeeded")
    void should_mark_payment_as_succeeded() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();

        InitiatePaymentRequest dto = InitiatePaymentRequest.builder()
                .userId(userId)
                .provider(PaymentProvider.STRIPE)
                .currency("USD")
                .amount(new BigDecimal("100.00"))
                .idempotencyKey(idempotencyKey)
                .build();

        paymentFacade.initiatePayment(dto);
        Payment payment = paymentRepository.findByIdempotencyKey(idempotencyKey).orElseThrow();
        String existingProviderPaymentId = payment.getProviderPaymentId();

        Payment succeeded = paymentFacade.markSucceeded(payment.getId(), "pi_test_123");

        assertThat(succeeded.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        assertThat(succeeded.getProviderPaymentStatus()).isEqualTo("pi_test_123");
        assertThat(succeeded.getProviderPaymentId()).isEqualTo(existingProviderPaymentId);
    }

    @Test
    @DisplayName("IDEMPOTENT: duplicate idempotency key should return existing payment")
    void should_return_existing_on_duplicate_idempotency_key() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();

        InitiatePaymentRequest req = InitiatePaymentRequest.builder()
                .userId(userId)
                .provider(PaymentProvider.STRIPE)
                .currency("USD")
                .amount(new BigDecimal("50.00"))
                .idempotencyKey(idempotencyKey)
                .build();

        InitiatePaymentResponse first = paymentFacade.initiatePayment(req);
        InitiatePaymentResponse second = paymentFacade.initiatePayment(req);

        var firstPayment = paymentRepository.findByIdempotencyKey(idempotencyKey).orElseThrow();
        var secondPayment = paymentRepository.findByIdempotencyKey(idempotencyKey).orElseThrow();

        assertThat(secondPayment.getId()).isEqualTo(firstPayment.getId());
        assertThat(secondPayment.getStatus()).isEqualTo(firstPayment.getStatus());
        assertThat(secondPayment.getIdempotencyKey()).isEqualTo(idempotencyKey);
        assertThat(first.getPaymentId()).isEqualTo(second.getPaymentId());
    }

    @Test
    @DisplayName("FAIL: markSucceeded should throw if payment not found")
    void should_throw_if_payment_not_found() {
        assertThatThrownBy(() -> paymentFacade.markSucceeded(
                UUID.randomUUID(),
                "pi_fake"
        ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Payment not found");
    }

    @Test
    @DisplayName("IDEMPOTENT: markSucceeded twice should be a no-op")
    void should_be_idempotent_when_mark_succeeded_twice() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();

        InitiatePaymentRequest req = InitiatePaymentRequest.builder()
                .userId(userId)
                .provider(PaymentProvider.STRIPE)
                .currency("USD")
                .amount(new BigDecimal("100.00"))
                .idempotencyKey(idempotencyKey)
                .build();

        paymentFacade.initiatePayment(req);
        Payment payment = paymentRepository.findByIdempotencyKey(idempotencyKey).orElseThrow();

        Payment first = paymentFacade.markSucceeded(payment.getId(), "pi_test_1");
        Payment second = paymentFacade.markSucceeded(payment.getId(), "pi_test_2");

        assertThat(first.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        assertThat(second.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        assertThat(second.getId()).isEqualTo(first.getId());
        assertThat(second.getProviderPaymentStatus()).isEqualTo("pi_test_1");
    }

}
