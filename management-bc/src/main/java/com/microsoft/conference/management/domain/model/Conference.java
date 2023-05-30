package com.microsoft.conference.management.domain.model;

import com.microsoft.conference.common.ListUtils;
import com.microsoft.conference.common.exception.SeatTypeException;
import com.microsoft.conference.management.domain.event.*;
import com.microsoft.conference.management.domain.publishableexception.SeatInsufficientException;
import org.enodeframework.domain.AbstractAggregateRoot;

import java.util.*;

public class Conference extends AbstractAggregateRoot {
    private ConferenceInfo conferenceInfo;
    private List<SeatType> seatTypes;
    private Map<String, List<ReservationItem>> reservationsMap;
    private boolean isPublished;

    public Conference(String id, ConferenceInfo info) {
        super(id);
        applyEvent(new ConferenceCreated(info));
    }

    public void update(ConferenceEditableInfo info) {
        applyEvent(new ConferenceUpdated(info));
    }

    public void publish() {
        if (isPublished) {
            throw new RuntimeException("Conference already published.");
        }
        applyEvent(new ConferencePublished());
    }

    public void unpublish() {
        if (!isPublished) {
            throw new RuntimeException("Conference already unpublished.");
        }
        applyEvent(new ConferenceUnpublished());
    }

    public void addSeat(SeatTypeInfo seatTypeInfo, int quantity) {
        applyEvent(new SeatTypeAdded("", seatTypeInfo, quantity));
    }

    public void updateSeat(String seatTypeId, SeatTypeInfo seatTypeInfo, int quantity) {
        SeatType seatType = ListUtils.single(seatTypes, x -> x.getId().equals(seatTypeId));
        if (seatType == null) {
            throw new SeatTypeException("Seat type not exist.");
        }
        applyEvent(new SeatTypeUpdated(seatTypeId, seatTypeInfo));
        if (seatType.getQuantity() != quantity) {
            int totalReservationQuantity = getTotalReservationQuantity(seatType.getId());
            if (quantity < totalReservationQuantity) {
                throw new SeatTypeException(String.format("Quantity cannot be small than total reservation quantity:%s", totalReservationQuantity));
            }
            applyEvent(new SeatTypeQuantityChanged(seatTypeId, quantity, quantity - totalReservationQuantity));
        }
    }

    public void removeSeat(String seatTypeId) {
        if (isPublished) {
            throw new SeatTypeException("Can't delete seat type from a conference that has been published");
        }
        if (hasReservation(seatTypeId)) {
            throw new SeatTypeException("The seat type has reservation, cannot be remove.");
        }
        applyEvent(new SeatTypeRemoved(seatTypeId));
    }

    public void makeReservation(String reservationId, List<ReservationItem> reservationItems) {
        if (!isPublished) {
            throw new SeatTypeException("Can't make reservation to the conference which is not published.");
        }
        if (reservationsMap.containsKey(reservationId)) {
            throw new SeatTypeException(String.format("Duplicated reservation, reservationId:%s", reservationId));
        }
        if (reservationItems == null || reservationItems.size() == 0) {
            throw new SeatTypeException(String.format("Reservation items can't be null or empty, reservationId:%s", reservationId));
        }
        List<SeatAvailableQuantity> seatAvailableQuantities = new ArrayList<>();
        for (ReservationItem reservationItem : reservationItems) {
            if (reservationItem.getQuantity() <= 0) {
                throw new SeatTypeException(String.format("Quantity must be bigger than than zero, reservationId:%s, seatTypeId:%s", reservationId, reservationItem.getSeatTypeId()));
            }
            SeatType seatType = seatTypes.stream().filter(x -> x.getId().equals(reservationItem.getSeatTypeId())).findFirst().orElse(null);
            if (seatType == null) {
                throw new SeatTypeException(String.format("Seat type '%s' not exist.", reservationItem.getSeatTypeId()));
            }
            int availableQuantity = seatType.getQuantity() - getTotalReservationQuantity(seatType.getId());
            if (availableQuantity < reservationItem.getQuantity()) {
                throw new SeatInsufficientException(id, reservationId);
            }
            seatAvailableQuantities.add(new SeatAvailableQuantity(seatType.getId(), availableQuantity - reservationItem.getQuantity()));
        }
        applyEvent(new SeatsReserved(reservationId, reservationItems, seatAvailableQuantities));
    }

    public void commitReservation(String reservationId) {
        if (reservationsMap.containsKey(reservationId)) {
            List<ReservationItem> reservationItems = reservationsMap.get(reservationId);
            List<SeatQuantity> seatQuantities = new ArrayList<>();
            for (ReservationItem reservationItem : reservationItems) {
                SeatType seatType = ListUtils.single(seatTypes, (x -> x.getId() == reservationItem.getSeatTypeId()));
                seatQuantities.add(new SeatQuantity(seatType.getId(), seatType.getQuantity() - reservationItem.getQuantity()));
            }
            applyEvent(new SeatsReservationCommitted(reservationId, seatQuantities));
        }
    }

    public void cancelReservation(String reservationId) {
        if (reservationsMap.containsKey(reservationId)) {
            List<ReservationItem> reservationItems = reservationsMap.get(reservationId);
            List<SeatAvailableQuantity> seatAvailableQuantities = new ArrayList<>();
            for (ReservationItem reservationItem : reservationItems) {
                SeatType seatType = ListUtils.single(seatTypes, (x -> Objects.equals(x.getId(), reservationItem.getSeatTypeId())));
                int availableQuantity = seatType.getQuantity() - getTotalReservationQuantity(seatType.getId());
                seatAvailableQuantities.add(new SeatAvailableQuantity(seatType.getId(), availableQuantity + reservationItem.getQuantity()));
            }
            applyEvent(new SeatsReservationCancelled(reservationId, seatAvailableQuantities));
        }
    }

    private boolean hasReservation(String seatTypeId) {
        return reservationsMap.values().stream().anyMatch(x -> x.stream().anyMatch(y -> Objects.equals(y.getSeatTypeId(), seatTypeId)));
    }

    private int getTotalReservationQuantity(String seatTypeId) {
        int totalReservationQuantity = 0;
        for (List<ReservationItem> reservation : reservationsMap.values()) {
            ReservationItem reservationItem = ListUtils.singleOrDefault(reservation, x -> Objects.equals(x.getSeatTypeId(), seatTypeId));
            if (reservationItem != null) {
                totalReservationQuantity += reservationItem.getQuantity();
            }
        }
        return totalReservationQuantity;
    }

    private void handle(ConferenceCreated evnt) {
        id = evnt.getAggregateRootId();
        conferenceInfo = evnt.getInfo();
        seatTypes = new ArrayList<>();
        reservationsMap = new HashMap<>();
        isPublished = false;
    }

    private void handle(ConferenceUpdated evnt) {
        ConferenceEditableInfo editableInfo = evnt.getInfo();
        conferenceInfo = new ConferenceInfo(
                conferenceInfo.getAccessCode(),
                conferenceInfo.getOwner(),
                conferenceInfo.getSlug(),
                editableInfo.getName(),
                editableInfo.getDescription(),
                editableInfo.getLocation(),
                editableInfo.getTagline(),
                editableInfo.getTwitterSearch(),
                editableInfo.getStartDate(),
                editableInfo.getEndDate());
    }

    private void handle(ConferencePublished evnt) {
        isPublished = true;
    }

    private void handle(ConferenceUnpublished evnt) {
        isPublished = false;
    }

    private void handle(SeatTypeAdded evnt) {
        SeatType seatType = new SeatType(evnt.getSeatTypeId(), evnt.getSeatTypeInfo());
        seatType.setQuantity(evnt.getQuantity());
        seatTypes.add(seatType);
    }

    private void handle(SeatTypeUpdated evnt) {
        ListUtils.single(seatTypes, x -> Objects.equals(x.getId(), evnt.getSeatTypeId())).setSeatTypeInfo(evnt.getSeatTypeInfo());
    }

    private void handle(SeatTypeQuantityChanged evnt) {
        ListUtils.single(seatTypes, x -> Objects.equals(x.getId(), evnt.getSeatTypeId())).setQuantity(evnt.getQuantity());
    }

    private void handle(SeatTypeRemoved evnt) {
        // remove 指定的seatTypeId
        seatTypes.remove(ListUtils.single(seatTypes, x -> x.getId().equals(evnt.getSeatTypeId())));
    }

    private void handle(SeatsReserved evnt) {
        reservationsMap.put(evnt.getReservationId(), evnt.getReservationItems());
    }

    private void handle(SeatsReservationCommitted evnt) {
        reservationsMap.remove(evnt.getReservationId());
        for (SeatQuantity seatQuantity : evnt.getSeatQuantities()) {
            ListUtils.single(seatTypes, (x -> Objects.equals(x.getId(), seatQuantity.getSeatTypeId()))).setQuantity(seatQuantity.getQuantity());
        }
    }

    private void handle(SeatsReservationCancelled evnt) {
        reservationsMap.remove(evnt.getReservationId());
    }
}