package com.opencard.contracts;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Contract(
        name = "OpenCard",
        info = @Info(
                title = "OpenCard Contract",
                description = "Contract to define rules in OpenCard Application",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "sangatdas5@gmail.com",
                        name = "OpenCard",
                        url = "https://github.com/Sangatdas/hyperledger-project"))
)
@Default
public final class OpenCardContract implements ContractInterface {

    private final Genson genson = new Genson();

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public OpenCard createOpenCard(final Context ctx, final String cardNumber,
                                   final int cardCVV, final String validFrom,
                                   final String validTo, final String cardOwner) {

        ChaincodeStub stub = ctx.getStub();

        String cardState = stub.getStringState(cardNumber);
        if (!cardState.isEmpty()) {
            String errorMessage = String.format("Card %s already exists.", cardNumber);
            throw new ChaincodeException(errorMessage);
        }
        try {
            List<Account> linkedAccounts = new LinkedList<>();
            Account primaryAccount = null;
            Date validF = new SimpleDateFormat("dd-MM-yyyy").parse(validFrom);
            Date validT = new SimpleDateFormat("dd-MM-yyyy").parse(validTo);
            OpenCard card = new OpenCard(cardNumber, cardCVV, validF,
                    validT, cardOwner, linkedAccounts, primaryAccount);
            cardState = genson.serialize(card);
            stub.putStringState(cardNumber, cardState);
            return card;
        } catch (ParseException pe) {
            String errorMessage = "Invalid date.";
            throw new ChaincodeException(errorMessage);
        } catch (Exception e) {
            throw new ChaincodeException((e.getMessage()));
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public OpenCard getOpenCard(final Context ctx, final String cardNumber) {
        try {
            ChaincodeStub stub = ctx.getStub();
            String cardState = stub.getStringState(cardNumber);

            if (cardState.isEmpty()) {
                String errorMessage = String.format("Card %s does not exist", cardNumber);
                throw new ChaincodeException(errorMessage);
            }
            return genson.deserialize(cardState, OpenCard.class);
        } catch (Exception e) {
            throw new ChaincodeException(e.getMessage());
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Account linkAccount(final Context ctx, final String cardNumber, final int cvv,
                               final String branchCode, final String accountNumber,
                               final String accountOwner, final Double accountBalance) {
        try {
            ChaincodeStub stub = ctx.getStub();
            String cardState = stub.getStringState(cardNumber);
            if (cardState.isEmpty()) {
                String errorMessage = String.format("Card %s does not exist", cardNumber);
                throw new ChaincodeException(errorMessage);
            }
            OpenCard card = genson.deserialize(cardState, OpenCard.class);
            if (card.getCardCVV() != cvv) {
                String errorMessage = String.format("Invalid CVV provided for card %s", cardNumber);
                throw new ChaincodeException(errorMessage);
            }
            Account newAccount = new Account(branchCode, accountNumber, accountOwner, accountBalance);
            card.linkAccount(newAccount);
            String updatedOpenCardState = genson.serialize(card);
            stub.putStringState(cardNumber, updatedOpenCardState);
            return newAccount;

        } catch (Exception e) {
            throw new ChaincodeException(e.getMessage());
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String unlinkAccount(final Context ctx, final String cardNumber,
                                final int cvv, final String branchCode,
                                final String accountNumber) {
        try {
            ChaincodeStub stub = ctx.getStub();
            String cardState = stub.getStringState(cardNumber);
            if (cardState.isEmpty()) {
                String errorMessage = String.format("Card %s does not exist", cardNumber);
                throw new ChaincodeException(errorMessage);
            }
            OpenCard card = genson.deserialize(cardState, OpenCard.class);
            if (card.getCardCVV() != cvv) {
                String errorMessage = String.format("Invalid CVV provided for card %s", cardNumber);
                throw new ChaincodeException(errorMessage);
            }
            card.unlinkAccount(branchCode.concat(accountNumber));
            String updatedOpenCardState = genson.serialize(card);
            stub.putStringState(cardNumber, updatedOpenCardState);
            return String.format("Unlinked account %s from OpenCard", branchCode.concat(accountNumber));

        } catch (Exception e) {
            throw new ChaincodeException(e.getMessage());
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String setPrimaryAccountForCard(final Context ctx, final String cardNumber,
                                           final int cvv, final String branchCode,
                                           final String accountNumber) {
        try {
            ChaincodeStub stub = ctx.getStub();
            String cardState = stub.getStringState(cardNumber);
            if (cardState.isEmpty()) {
                String errorMessage = String.format("Card %s does not exist", cardNumber);
                throw new ChaincodeException(errorMessage);
            }
            OpenCard card = genson.deserialize(cardState, OpenCard.class);
            if (card.getCardCVV() != cvv) {
                String errorMessage = String.format("Invalid CVV provided for card %s", cardNumber);
                throw new ChaincodeException(errorMessage);
            }
            card.setPrimaryAccount(branchCode.concat(accountNumber));
            String updatedOpenCardState = genson.serialize(card);
            stub.putStringState(cardNumber, updatedOpenCardState);
            return String.format("Account %s set as primary account", branchCode.concat(accountNumber));
        } catch (Exception e) {
            throw new ChaincodeException(e.getMessage());
        }
    }
}
