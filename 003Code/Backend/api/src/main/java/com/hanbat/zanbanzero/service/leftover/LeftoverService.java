package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverAndPreDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;

import java.util.List;

public interface LeftoverService {
    LeftoverDto setLeftover(LeftoverDto dto);

    int getAllLeftoverPage();

    List<LeftoverDto> getLeftoverPage(int count);

    List<LeftoverAndPreDto> getAllLeftoverAndPre(int page);

    List<LeftoverDto> getLastWeeksLeftovers(int type);
}