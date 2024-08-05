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

}
