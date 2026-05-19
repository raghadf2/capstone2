package com.example.spaceflow.Controller;

import com.example.spaceflow.DTO.NotificationResponse;
import com.example.spaceflow.DTO.WaitingListResponse;
import com.example.spaceflow.Model.WaitingList;
import com.example.spaceflow.Service.WaitingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/waiting-list")
@RequiredArgsConstructor
public class WaitingListController {

    private final WaitingListService waitingListService;

    @PostMapping("/add")
    public ResponseEntity<WaitingListResponse> addToWaitingList(@RequestParam Integer userId, @RequestParam Integer spaceId, @RequestParam LocalDate date, @RequestParam LocalTime start, @RequestParam LocalTime end) {
        return ResponseEntity.status(200).body(waitingListService.addToWaitingList(userId, spaceId, date, start, end));
    }

    @GetMapping("/get")
    public ResponseEntity<List<WaitingList>> getWaitingList(@RequestParam Integer spaceId, @RequestParam LocalDate date) {
        return ResponseEntity.status(200).body(waitingListService.getWaitingList(spaceId, date));
    }

    @PostMapping("/notify")
    public ResponseEntity<NotificationResponse> notifyUsers(@RequestParam Integer spaceId, @RequestParam LocalDate date) {
        return ResponseEntity.status(200).body(waitingListService.notifyWaitingUsers(spaceId, date));
    }
}
