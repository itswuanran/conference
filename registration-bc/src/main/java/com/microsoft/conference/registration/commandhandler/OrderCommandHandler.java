package com.microsoft.conference.registration.commandhandler;

import com.microsoft.conference.common.registration.commands.order.*;
import com.microsoft.conference.registration.domain.SeatQuantity;
import com.microsoft.conference.registration.domain.SeatType;
import com.microsoft.conference.registration.domain.order.PricingService;
import com.microsoft.conference.registration.domain.order.model.Order;
import org.enodeframework.annotation.Command;
import org.enodeframework.annotation.Subscribe;
import org.enodeframework.commanding.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Command
public class OrderCommandHandler {

    @Autowired
    private PricingService pricingService;

    @Subscribe
    public CompletableFuture<Boolean> handleAsync(CommandContext context, PlaceOrder command) {
        List<SeatQuantity> seats = new ArrayList<>();
        command.getSeatInfos().forEach(x -> seats.add(new SeatQuantity(new SeatType(x.seatType, x.seatName, x.unitPrice), x.quantity)));
        return context.addAsync(new Order(
                command.getAggregateRootId(),
                command.getConferenceId(),
                seats,
                pricingService));
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, AssignRegistrantDetails command) {
        return orderHandle(context, command.getAggregateRootId(), order -> {
            order.assignRegistrant(command.getFirstName(), command.getLastName(), command.getEmail());
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, ConfirmReservation command) {
        return context.getAsync(command.getAggregateRootId(), Order.class).thenAccept(order -> {
            order.confirmReservation(command.isReservationSuccess);
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, ConfirmPayment command) {
        return context.getAsync(command.getAggregateRootId(), Order.class).thenAccept(order -> {
            order.confirmPayment(command.isPaymentSuccess);
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, MarkAsSuccess command) {
        return context.getAsync(command.getAggregateRootId(), Order.class).thenAccept(order -> {
            order.markAsSuccess();
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, CloseOrder command) {
        return context.getAsync(command.getAggregateRootId(), Order.class).thenAccept(order -> {
            order.close();
        });
    }

    private CompletableFuture<Void> orderHandle(CommandContext context, String id, Consumer<Order> consumer) {
        return context.getAsync(id, Order.class).thenAccept(order -> {
            consumer.accept(order);
        }).exceptionally(throwable -> {
            return null;
        });
    }
}
