package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(TwoPartyAssetIssuance.class)
public class AssetIssuanceResponder extends FlowLogic<Void> {
    private FlowSession sellerSession;

    public AssetIssuanceResponder(FlowSession sellerSession) {
        this.sellerSession = sellerSession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Responder flow logic goes here.
        subFlow(new ReceiveFinalityFlow(sellerSession));
        return null;
    }
}
