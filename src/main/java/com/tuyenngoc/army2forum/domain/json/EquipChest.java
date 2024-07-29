package com.tuyenngoc.army2forum.domain.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipChest {

    @JsonProperty("key")
    private int key;

    @JsonProperty("equipIndex")
    private short equipIndex;

    @JsonProperty("equipType")
    private byte equipType;

    @JsonProperty("vipLevel")
    private byte vipLevel;

    @JsonProperty("purchaseDate")
    private LocalDateTime purchaseDate;

    @JsonProperty("characterId")
    private byte characterId;

    @JsonProperty("inUse")
    private byte inUse;

    @JsonProperty("slots")
    private byte[] slots;

    @JsonProperty("additionalPoints")
    private byte[] addPoints;

    @JsonProperty("additionalPercent")
    private byte[] addPercents;

}
