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
public class ClanItem {

    @JsonProperty("id")
    private byte id;

    @JsonProperty("time")
    private LocalDateTime time;

}