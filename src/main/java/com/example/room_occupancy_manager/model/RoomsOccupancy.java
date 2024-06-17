package com.example.room_occupancy_manager.model;

import java.math.BigDecimal;
import java.util.Currency;

import com.example.room_occupancy_manager.model.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomsOccupancy {

    private int roomsUsed;

    private BigDecimal income;
    private RoomType roomType;
    private Currency currency;

    @Override
    public String toString() {
        return "RoomsOccupancy{" +
            "roomsUsed=" + roomsUsed +
            ", income='" + income + '\'' +
            ", roomType=" + roomType +
            '}';
    }
}
