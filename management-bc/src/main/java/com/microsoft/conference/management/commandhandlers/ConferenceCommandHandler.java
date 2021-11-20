package com.microsoft.conference.management.commandhandlers;

import com.microsoft.conference.common.management.commands.*;
import com.microsoft.conference.management.domain.model.*;
import com.microsoft.conference.management.domain.service.RegisterConferenceSlugService;
import org.enodeframework.annotation.Command;
import org.enodeframework.annotation.Subscribe;
import org.enodeframework.commanding.ICommandContext;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Command
public class ConferenceCommandHandler {

    @Resource
    private RegisterConferenceSlugService registerConferenceSlugService;

    @Subscribe
    public CompletableFuture<Boolean> handleAsync(ICommandContext context, CreateConference command) {
        return execInternal(context, command);
    }

    private CompletableFuture<Boolean> execInternal(ICommandContext context, CreateConference command) {
        Conference conference = new Conference(command.getAggregateRootId(), new ConferenceInfo(
                command.getAccessCode(),
                new ConferenceOwner(command.getOwnerName(), command.getOwnerEmail()),
                command.getSlug(),
                command.getName(),
                command.getDescription(),
                command.getLocation(),
                command.getTagline(),
                command.getTwitterSearch(),
                command.getStartDate(),
                command.getEndDate()));
        registerConferenceSlugService.registerSlug(command.getId(), conference.getId(), command.getSlug());
        return context.addAsync(conference);
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, UpdateConference command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.update(new ConferenceEditableInfo(
                    command.name,
                    command.description,
                    command.location,
                    command.tagline,
                    command.twitterSearch,
                    command.startDate,
                    command.endDate));
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, PublishConference command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.publish();
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, UnpublishConference command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.unpublish();
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, AddSeatType command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.addSeat(new SeatTypeInfo(
                    command.name,
                    command.description,
                    command.price), command.quantity);
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, RemoveSeatType command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.removeSeat(command.getSeatTypeId());
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, UpdateSeatType command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.updateSeat(
                    command.seatTypeId,
                    new SeatTypeInfo(command.name, command.description, command.price),
                    command.quantity);
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, MakeSeatReservation command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.makeReservation(command.reservationId, command.seats.stream().map(x -> new ReservationItem(x.seatType, x.quantity)).collect(Collectors.toList()));
        });
    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, CommitSeatReservation command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.commitReservation(command.reservationId);
        });

    }

    @Subscribe
    public CompletableFuture<Void> handleAsync(ICommandContext context, CancelSeatReservation command) {
        return conferenceHandler(context, command.getAggregateRootId(), conference -> {
            conference.cancelReservation(command.reservationId);
        });
    }

    private CompletableFuture<Void> conferenceHandler(ICommandContext context, String id, Consumer<Conference> consumer) {
        return context.getAsync(id, Conference.class).thenAccept(x -> {
            consumer.accept(x);
        }).exceptionally(throwable -> {
            return null;
        });
    }
}
