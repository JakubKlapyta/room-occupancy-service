package com.example.room_occupancy_manager.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomOccupancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetRoomOccupancyStatusOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/roomsOccupancy")
                .param("numberOfPremiumRooms", "3")
                .param("numberOfEconomyRooms", "2")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetRoomOccupancyStatusBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/roomsOccupancy")
                .param("numberOfPremiumRooms", "-3")
                .param("numberOfEconomyRooms", "2")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
