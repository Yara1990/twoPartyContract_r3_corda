package com.template.states;

import com.template.contracts.TwoPartyAssetContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

// *********
// * Asset State which holds the ledger state data
// parameters includes:
//  seller
//  buyer
//  assetAmount*
// *********
@BelongsToContract(TwoPartyAssetContract.class)
public class TwoPartyAssetState implements ContractState {
    private final int assetAmount;
    private final Party buyer;
    private final Party seller;


    public TwoPartyAssetState(int assetAmount, Party buyer, Party seller) {
        this.assetAmount = assetAmount;
        this.buyer = buyer;
        this.seller = seller;

    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(buyer, seller);
    }

    public int getAssetAmount() {
        return assetAmount;
    }

    public Party getBuyer() {
        return buyer;
    }

    public Party getSeller() {
        return seller;
    }

}