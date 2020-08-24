package com.opencard.contracts;

import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Getter
@DataType
public class Account {

    @Property()
    private String branchCode;

    @Property()
    private String accountNumber;

    @Property()
    private String accountOwner;

    @Property() @Setter
    private Double accountBalance;

    public Account(@JsonProperty final String branchCode, @JsonProperty final String accountNumber,
                   @JsonProperty final String accountOwner, @JsonProperty final Double accountBalance) {
        this.branchCode = branchCode;
        this.accountNumber = accountNumber;
        this.accountOwner = accountOwner;
        this.accountBalance = accountBalance;
    }
}
