package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;

import java.util.List;

public interface LeftoverService {
    LeftoverDto setLeftover(LeftoverDto dto);

    List<LeftoverDto> getLastWeeksLeftovers(int type);
}