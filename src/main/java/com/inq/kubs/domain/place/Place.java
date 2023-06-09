package com.inq.kubs.domain.place;

import com.inq.kubs.domain.place.enums.Area;
import com.inq.kubs.domain.place.enums.Sector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Sector sector;

    @Enumerated(EnumType.STRING)
    private Area area;

    private String placeName;

    private Integer floor;

    @Transient
    private Boolean isAble = true;

    //편의 메서드
    public void disAble() {
        this.isAble = false;
    }
}
