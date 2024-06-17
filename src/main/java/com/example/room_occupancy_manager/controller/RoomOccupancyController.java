package com.example.room_occupancy_manager.controller;

import java.util.List;

import com.example.room_occupancy_manager.model.RoomsOccupancy;
import com.example.room_occupancy_manager.service.RoomOccupancyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomOccupancyController {

    private final RoomOccupancyService roomOccupancyService;

    @GetMapping("/roomsOccupancy")
    List<RoomsOccupancy> getRoomOccupancy(@RequestParam final int numberOfPremiumRooms, @RequestParam final int numberOfEconomyRooms) throws JsonProcessingException {
      return roomOccupancyService.getTotalIncomeAndRoomsOccupied(numberOfPremiumRooms, numberOfEconomyRooms);
    }
}
