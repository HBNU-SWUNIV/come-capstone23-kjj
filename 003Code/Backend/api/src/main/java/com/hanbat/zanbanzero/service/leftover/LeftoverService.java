package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;

import java.util.List;

public interface LeftoverService {
    List<LeftoverDto> getLastWeeksLeftovers(int type);
}