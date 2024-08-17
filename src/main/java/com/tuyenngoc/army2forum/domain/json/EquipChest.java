package com.tuyenngoc.army2forum.domain.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipChest {

    @JsonProperty("k")
    private int key;

    @JsonProperty("ei")
    private short equipIndex;

    @JsonProperty("et")
    private byte equipType;

    @JsonProperty("vl")
    private byte vipLevel;

    @JsonProperty("pd")
    private LocalDateTime purchaseDate;

    @JsonProperty("cid")
    private byte characterId;

    @JsonProperty("iu")
    private byte inUse;

    @JsonProperty("s")
    private int[] slots;

    @JsonProperty("ap")
    private int[] addPoints;

    @JsonProperty("apc")
    private int[] addPercents;

    public EquipChest(int key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipChest that = (EquipChest) o;
        return key == that.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
