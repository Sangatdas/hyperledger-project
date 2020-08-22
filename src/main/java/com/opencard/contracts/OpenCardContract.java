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
                        url = ""))
)
@Default
public final class OpenCardContract implements ContractInterface {

    private final Genson genson = new Genson();

    @Transaction()
    public OpenCard createOpenCard(final Context ctx, final String cardNumber,
                                   final int cardCVV, final String validFrom,
                                   final String validTo, final String cardOwner) {

        ChaincodeStub stub = ctx.getStub();

        String cardState = stub.getStringState(cardNumber);
        if (!cardState.isEmpty()) {
            String errorMessage = String.format("Card %s already exists.", cardNumber);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }
        try {
            Date validF = new SimpleDateFormat("dd-MM-yyyy").parse(validFrom);
            Date validT = new SimpleDateFormat("dd-MM-yyyy").parse(validTo);
            OpenCard card = new OpenCard(cardNumber, cardCVV, validF, validT, cardOwner, null, null);

            return card;
        } catch (ParseException pe) {
            String errorMessage = String.format("Invalid date.");
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }
    }

    public OpenCard getOpenCard(final Context ctx, final String cardNumber) {
        ChaincodeStub stub = ctx.getStub();
        String carState = stub.getStringState(cardNumber);

        if (carState.isEmpty()) {
            String errorMessage = String.format("Card %s does not exist", cardNumber);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        OpenCard card = genson.deserialize(carState, OpenCard.class);

        return card;
    }
}
