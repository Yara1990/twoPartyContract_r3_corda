package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.TwoPartyAssetContract;
import com.template.states.TwoPartyAssetState;
import net.corda.core.contracts.Command;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.Date;

@InitiatingFlow
@StartableByRPC
public class TwoPartyAssetIssuance extends FlowLogic<Void> {
    private final Integer assetAmount;
    private final Party sellerParty;

    /**
     * The progress tracker provides checkpoints indicating the progress of the flow to observers.
     */
    private final ProgressTracker progressTracker = new ProgressTracker();

    public TwoPartyAssetIssuance(Integer assetAmount, Party sellerParty) {
        this.assetAmount = assetAmount;
        this.sellerParty = sellerParty;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    /**
     * The flow logic is encapsulated within the call() method.
     */
    @Suspendable
    @Override
    public Void call() throws FlowException {
        // We retrieve the notary identity from the network map.
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        // We create the transaction components.
        TwoPartyAssetState outputState = new TwoPartyAssetState(assetAmount, getOurIdentity(), sellerParty);
        Command command = new Command<>(new TwoPartyAssetContract.Commands.Create(), getOurIdentity().getOwningKey());

        // We create a transaction builder and add the components.
        TransactionBuilder txBuilder = new TransactionBuilder(notary)
                .addOutputState(outputState, TwoPartyAssetContract.ID)
                .addCommand(command);

        // Signing the transaction.
        SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);

        // Creating a session with the other party.
        FlowSession sellerPartySession = initiateFlow(sellerParty);

        // We finalise the transaction and then send it to the counterparty.
        subFlow(new FinalityFlow(signedTx, sellerPartySession));

        return null;

    }
}
