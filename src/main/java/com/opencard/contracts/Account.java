package com.opencard.contracts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Getter
@AllArgsConstructor
@DataType
public class Account {

    @Property
    private String branchCode;

    @Property
    private String accountNumber;

    @Property
    private String accountOwner;

    @Property @Setter
    private Double accountBalance;

}
