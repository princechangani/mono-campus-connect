package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.FeeInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeInvoiceRepository extends JpaRepository<FeeInvoice, Long> {
}

