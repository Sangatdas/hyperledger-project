package com.opencard.contracts;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DataType
public final class OpenCard {
    @Property @EqualsAndHashCode.Include
    private String cardNumber;

    @Property
    private int cardCVV;

    @Property
    private Date validFrom;

    @Property
    private Date validTo;

    @Property
    private String cardOwner;

    @Property
    private List<Account> linkedAccounts;

    @Property
    private Account primaryAccount;
}
