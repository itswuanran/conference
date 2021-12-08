package com.microsoft.conference.payments.commandhandler;

import com.microsoft.conference.common.payment.commands.CancelPayment;
import com.microsoft.conference.common.payment.commands.CompletePayment;
import com.microsoft.conference.common.payment.commands.CreatePayment;
import com.microsoft.conference.payments.domain.model.Payment;
import com.microsoft.conference.payments.domain.model.PaymentItem;
import org.enodeframework.annotation.Command;
import org.enodeframework.annotation.Subscribe;
import org.enodeframework.commanding.CommandContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * CommandMessageHandler<CreatePayment>,
 * CommandMessageHandler<CompletePayment>,
 * CommandMessageHandler<CancelPayment>
 */
@Command
public class PaymentCommandHandler {

    @Subscribe
    public void handleAsync(CommandContext context, CreatePayment command) {
        List<PaymentItem> paymentItemList = command.getLines().stream().map(x -> new PaymentItem(x.description, x.amount)).collect(Collectors.toList());
        context.addAsync(new Payment(
                command.getAggregateRootId(),
                command.getOrderId(),
                command.getConferenceId(),
                command.getDescription(),
                command.getTotalAmount(),
                paymentItemList));
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, CompletePayment command) {
        return context.getAsync(command.getAggregateRootId(), Payment.class)
                .thenAccept(payment -> payment.complete());
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, CancelPayment command) {
        return context.getAsync(command.getAggregateRootId(), Payment.class)
                .thenAccept(payment -> payment.cancel());
    }
}