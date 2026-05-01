package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.FeePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {
}

