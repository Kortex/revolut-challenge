package com.ariskourt.revolut.domain;

import com.ariskourt.revolut.domain.common.AbstractVersionedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "bank_account")
public class BankAccount extends AbstractVersionedEntity {

    @Id
    @GeneratedValue(generator = "uuid_generator")
    @GenericGenerator(
        name = "uuid_generator",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "account_balance")
    private Long accountBalance;

}

