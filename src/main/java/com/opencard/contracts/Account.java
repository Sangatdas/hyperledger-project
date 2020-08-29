package com.opencard.contracts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@DataType()
public final class Account {

    @Property() @EqualsAndHashCode.Include
    private final String branchCode;

    @Property() @EqualsAndHashCode.Include
    private final String accountNumber;

    @Property()
    private final String accountOwner;

    @Property()
    private Double accountBalance;

    public Account(final String branchCode,
                   final String accountNumber,
                   final String accountOwner,
                   final Double accountBalance) {
        this.branchCode = branchCode;
        this.accountNumber = accountNumber;
        this.accountOwner = accountOwner;
        this.accountBalance = accountBalance;
    }

    public void deposit(final Double amount) {
        this.accountBalance += amount;
    }

    public void withdraw(final Double amount) {
        this.accountBalance -= amount;
    }
}
