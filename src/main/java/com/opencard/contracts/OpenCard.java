package com.opencard.contracts;

import com.owlike.genson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode
@DataType()
public final class OpenCard {
    @Property()
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
                + "@"
                + Integer.toHexString(hashCode())
                + "["
                + "cardNumber='" + cardNumber + '\''
                + ", cardCVV=" + cardCVV
                + ", validFrom=" + validFrom
                + ", validTo=" + validTo
                + ", cardOwner='" + cardOwner + '\''
                + ", linkedAccounts=" + linkedAccounts
                + ", primaryAccount=" + primaryAccount
                + ']';
    }
}
