package com.example.room_occupancy_manager.sevice;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import com.example.room_occupancy_manager.exc.NegativeRoomNumberException;
import com.example.room_occupancy_manager.model.RoomsOccupancy;
import com.example.room_occupancy_manager.model.enums.RoomType;
import com.example.room_occupancy_manager.service.RoomOccupancyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RoomOccupancyServiceTest {

    @Autowired
    RoomOccupancyService roomOccupancyService;

    @Test
    public void testRoomThreePremiumThreeEconomy() throws JsonProcessingException {
        List<RoomsOccupancy> results = roomOccupancyService.getTotalIncomeAndRoomsOccupied(3, 3);

        checkResults(results, 3, 3, new BigDecimal("738"), new BigDecimal("167.99"));
    }

    @Test
    public void testRoomSevenPremiumFiveEconomy() throws JsonProcessingException {
        List<RoomsOccupancy> results = roomOccupancyService.getTotalIncomeAndRoomsOccupied(7, 5);

        checkResults(results, 6, 4, new BigDecimal("1054"), new BigDecimal("189.99"));

    }

    @Test
    public void testRoomTwoPremiumSevenEconomy() throws JsonProcessingException {
        List<RoomsOccupancy> results = roomOccupancyService.getTotalIncomeAndRoomsOccupied(2, 7);

        checkResults(results, 2, 4, new BigDecimal("583"), new BigDecimal("189.99"));

    }

    @Test
    public void testRoomSevenPremiumOneEconomy() throws JsonProcessingException {
        List<RoomsOccupancy> results = roomOccupancyService.getTotalIncomeAndRoomsOccupied(7, 1);

        checkResults(results, 7, 1, new BigDecimal("1153.99"), new BigDecimal("45"));
    }

    @Test
    public void testNegativePremiumRooms() {
        assertThrows(NegativeRoomNumberException.class, () -> roomOccupancyService.getTotalIncomeAndRoomsOccupied(-7, 1));
    }

    @Test
    public void testNegativeEconomyRooms() {
        assertThrows(NegativeRoomNumberException.class, () -> roomOccupancyService.getTotalIncomeAndRoomsOccupied(7, -1));
    }

    private static void checkResults(final List<RoomsOccupancy> results, int premiumRoomsOccupied, int economyRoomsOccupied, BigDecimal premiumIncome,
                                     BigDecimal economyIncome) {
        assertThat(results, hasSize(2));

        assertThat(results, hasItem(hasProperty("roomType", equalTo(RoomType.Premium))));
        assertThat(results, hasItem(hasProperty("roomType", equalTo(RoomType.Economy))));

        for (RoomsOccupancy res : results) {
            if (res.getRoomType() == RoomType.Premium) {
                assertThat(res.getRoomsUsed(), equalTo(premiumRoomsOccupied));
                assertThat(res.getIncome(), comparesEqualTo(premiumIncome));
                assertThat(res.getCurrency(), equalTo(Currency.getInstance("EUR")));
            }
            if (res.getRoomType() == RoomType.Economy) {
                assertThat(res.getRoomsUsed(), equalTo(economyRoomsOccupied));
                assertThat(res.getIncome(), comparesEqualTo(economyIncome));
                assertThat(res.getCurrency(), equalTo(Currency.getInstance("EUR")));
            }
        }
    }
}
