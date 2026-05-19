package com.example.spaceflow.Service;

import com.example.spaceflow.DTO.NotificationResponse;
import com.example.spaceflow.DTO.WaitingListResponse;
import com.example.spaceflow.Model.Booking;
import com.example.spaceflow.Model.User;
import com.example.spaceflow.Model.WaitingList;
import com.example.spaceflow.Repository.BookingRepository;
import com.example.spaceflow.Repository.UserRepository;
import com.example.spaceflow.Repository.WaitingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingListService {

    private final WaitingListRepository waitingListRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final UserRepository userRepository;

    //checks for booking conflict, if none returns available, otherwise adds user to waiting list with position
    public WaitingListResponse addToWaitingList(Integer userId, Integer spaceId, LocalDate date, LocalTime start, LocalTime end) {
        List<Booking> conflict = bookingRepository.findOverlappingBookings(spaceId, date, start, end);

        if (conflict.isEmpty()) {
            return new WaitingListResponse("AVAILABLE", "Space is available, no need to join waiting list", null);
        }

        int position = waitingListRepository.findBySpaceIdAndDateOrderByPositionAsc(spaceId, date).size() + 1;

        WaitingList wl = new WaitingList();
        wl.setUserId(userId);
        wl.setSpaceId(spaceId);
        wl.setDate(date);
        wl.setStartTime(start);
        wl.setEndTime(end);
        wl.setPosition(position);
        wl.setStatus("WAITING");

        waitingListRepository.save(wl);

        return new WaitingListResponse("ADDED", "Added to waiting list", position);
    }

    //returns waiting list ordered by position for the same space and date
    public List<WaitingList> getWaitingList(Integer spaceId, LocalDate date) {
        return waitingListRepository.findBySpaceIdAndDateOrderByPositionAsc(spaceId, date);
    }

    //sends email and WhatsApp notifications to all waiting users and updates their status to NOTIFIED
    public NotificationResponse notifyWaitingUsers(Integer spaceId, LocalDate date) {

        List<WaitingList> list = waitingListRepository.findBySpaceIdAndDateOrderByPositionAsc(spaceId, date);

        int count = 0;

        for (WaitingList w : list) {
            User user = userRepository.findUserById(w.getUserId());

            w.setStatus("NOTIFIED");
            waitingListRepository.save(w);

            emailService.sendEmail(user.getEmail(), "Waiting List", "A space is now available for your request");
            whatsAppService.sendWhatsApp(user.getPhone(), "A space is now available for your request");

            count++;
        }

        return new NotificationResponse("SUCCESS", "Users notified successfully", count);
    }
}
