package com.template.contracts;


import com.template.states.TwoPartyAssetState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.identity.AbstractParty;
import net.corda.core.transactions.LedgerTransaction;

import java.util.stream.Collectors;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class TwoPartyAssetContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.template.contracts.TwoPartyAssetContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) {
        final CommandWithParties<Commands.Create> command = requireSingleCommand(tx.getCommands(), Commands.Create.class);
        requireThat(require -> {
            // Generic constraints around the Issuance transaction.
            require.using("No inputs should be consumed when issuing an Asset.",
                    tx.getInputs().isEmpty());
            require.using("Only one output state should be created.",
                    tx.getOutputs().size() == 1);
            final TwoPartyAssetState out = tx.outputsOfType(TwoPartyAssetState.class).get(0);
            require.using("The seller and the buyer cannot be the same entity.",
                    out.getBuyer() != out.getSeller());
            require.using("The Amount value must be non-negative.",
                    out.getAssetAmount() > 0);

            return null;
        });
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        class Create implements Commands {}
    }
}