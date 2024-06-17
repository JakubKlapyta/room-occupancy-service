package com.example.room_occupancy_manager.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import com.example.room_occupancy_manager.exc.NegativeRoomNumberException;
import com.example.room_occupancy_manager.model.RoomsOccupancy;
import com.example.room_occupancy_manager.model.enums.RoomType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class RoomOccupancyService {

    public List<RoomsOccupancy> getTotalIncomeAndRoomsOccupied(int numberOfPremiumRooms, int numberOfEconomyRooms) throws JsonProcessingException {

        if (numberOfPremiumRooms < 0 || numberOfEconomyRooms < 0) {
            throw new NegativeRoomNumberException();
        }

        ArrayList<BigDecimal> proposedRoomPrices = convertJsonArrayToBigDecimalList();

        var availableEconomyRooms = numberOfEconomyRooms;
        var availablePremiumRooms = numberOfPremiumRooms;

        var premiumRoomsUsage = new RoomsOccupancy(0, BigDecimal.ZERO, RoomType.Premium, Currency.getInstance("EUR"));
        var economyRoomsUsage = new RoomsOccupancy(0, BigDecimal.ZERO, RoomType.Economy, Currency.getInstance("EUR"));

        proposedRoomPrices.sort(Collections.reverseOrder());

        for (int i = 0; i < proposedRoomPrices.size(); i++) {

            if (availablePremiumRooms == 0 && availableEconomyRooms == 0) {
                break;
            }

            BigDecimal proposedPrice = proposedRoomPrices.get(i);

            if (proposedPrice.compareTo(new BigDecimal("100")) >= 0 && availablePremiumRooms > 0) {

                availablePremiumRooms = getAvailableRooms(premiumRoomsUsage, proposedPrice, availablePremiumRooms);

            } else if (proposedPrice.compareTo(new BigDecimal("100")) < 0) {

                if (proposedRoomPrices.size() - i > availableEconomyRooms && availablePremiumRooms > 0) {

                    availablePremiumRooms = getAvailableRooms(premiumRoomsUsage, proposedPrice, availablePremiumRooms);

                } else if (availableEconomyRooms > 0) {

                    availableEconomyRooms = getAvailableRooms(economyRoomsUsage, proposedPrice, availableEconomyRooms);
                }
            }
        }
        return List.of(premiumRoomsUsage, economyRoomsUsage);
    }

    private static int getAvailableRooms(final RoomsOccupancy premiumRoomsOccupancy, final BigDecimal proposedPrice, int availablePremiumRooms) {

        premiumRoomsOccupancy.setRoomsUsed(premiumRoomsOccupancy.getRoomsUsed() + 1);
        premiumRoomsOccupancy.setIncome(premiumRoomsOccupancy.getIncome().add(proposedPrice));

        availablePremiumRooms -= 1;

        return availablePremiumRooms;
    }

    private static ArrayList<BigDecimal> convertJsonArrayToBigDecimalList() throws JsonProcessingException {

        //Mock data could in real life example could be from DB or can be implemented along with endpoint as input
        String proposedRoomPricesJson = "[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]";

        ArrayList<BigDecimal> bigDecimalList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(proposedRoomPricesJson);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                bigDecimalList.add(node.decimalValue());
            }
        }
        return bigDecimalList;
    }
}
