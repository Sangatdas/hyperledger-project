package com.opencard.contracts;

import com.owlike.genson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@DataType()
public final class OpenCard {
    @Property() @EqualsAndHashCode.Include
    private final String cardNumber;

    @Property()
    private final int cardCVV;

    @Property()
    private final Date validFrom;

    @Property()
    private final Date validTo;

    @Property()
    private final String cardOwner;

    @Property()
    private List<Account> linkedAccounts;

    @Property()
    private Account primaryAccount;

    public OpenCard(@JsonProperty("cardNumber") final String cardNumber,
                    @JsonProperty("cardCVV") final int cardCVV,
                    @JsonProperty("validFrom") final Date validFrom,
                    @JsonProperty("validTo") final Date validTo,
                    @JsonProperty("cardOwner") final String cardOwner,
                    @JsonProperty("linkedAccounts") final List<Account> linkedAccounts,
                    @JsonProperty("primaryAccount") final Account primaryAccount) {
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.cardOwner = cardOwner;
        this.linkedAccounts = linkedAccounts;
        this.primaryAccount = primaryAccount;
    }

    public Account linkAccount(final Account newAccount) throws IllegalArgumentException {
        if (this.linkedAccounts.contains(newAccount)) {
            throw new IllegalArgumentException("Account has already been linked");
        }
        this.linkedAccounts.add(newAccount);
        return newAccount;
    }

    public void unlinkAccount(final String accountNumber) throws IllegalArgumentException {
        if (this.linkedAccounts.stream().noneMatch(account ->
                account.getBranchCode().concat(account.getAccountNumber()).equals(accountNumber))) {
            throw new IllegalArgumentException("Account has not been linked");
        }
        this.linkedAccounts = this.linkedAccounts
                .stream()
                .filter(account -> (!account.getBranchCode().concat(account.getAccountNumber()).equals(accountNumber)))
                .collect(Collectors.toList());
    }

    public void setPrimaryAccount(final String accountNumber) {
        this.linkedAccounts.forEach(account -> {
            if (account.getBranchCode().concat(account.getAccountNumber()).equals(accountNumber)) {
                this.primaryAccount = account;
            }
        });
    }
}
