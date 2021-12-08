package com.microsoft.conference.registration.commandhandler;

import com.microsoft.conference.common.registration.commands.seatassignment.AssignSeat;
import com.microsoft.conference.common.registration.commands.seatassignment.CreateSeatAssignments;
import com.microsoft.conference.common.registration.commands.seatassignment.UnassignSeat;
import com.microsoft.conference.registration.domain.order.model.Order;
import com.microsoft.conference.registration.domain.seatassigning.model.Attendee;
import com.microsoft.conference.registration.domain.seatassigning.model.OrderSeatAssignments;
import org.enodeframework.annotation.Command;
import org.enodeframework.annotation.Subscribe;
import org.enodeframework.commanding.CommandContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Command
public class OrderSeatAssignmentsCommandHandler {

    @Subscribe
    public CompletableFuture<Boolean> handleAsync(CommandContext context, CreateSeatAssignments command) {
        return context.getAsync(command.aggregateRootId, Order.class).thenCompose(order -> {
            OrderSeatAssignments orderSeatAssignments = order.createSeatAssignments();
            return context.addAsync(orderSeatAssignments);
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, AssignSeat command) {
        return orderSeatHandler(context, command.aggregateRootId, orderSeatAssignments -> {
            orderSeatAssignments.assignSeat(command.getPosition(), new Attendee(
                    command.getPersonalInfo().getFirstName(),
                    command.getPersonalInfo().getLastName(),
                    command.getPersonalInfo().getEmail()));
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(CommandContext context, UnassignSeat command) {
        return orderSeatHandler(context, command.aggregateRootId, orderSeatAssignments -> {
            orderSeatAssignments.unAssignSeat(command.getPosition());
        });
    }

    private CompletableFuture<Void> orderSeatHandler(CommandContext context, String id, Consumer<OrderSeatAssignments> consumer) {
        return context.getAsync(id, OrderSeatAssignments.class).thenAccept(orderSeatAssignments -> {
            consumer.accept(orderSeatAssignments);
        }).exceptionally(throwable -> {
            return null;
        });
    }
}
