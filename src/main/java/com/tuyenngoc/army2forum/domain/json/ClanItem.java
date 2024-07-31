package com.tuyenngoc.army2forum.domain.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClanItem {

    @JsonProperty("id")
    private byte id;

    @JsonProperty("time")
    private LocalDateTime time;

}