package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverAndPreDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;

import java.util.List;

public interface LeftoverService {
    LeftoverDto setLeftover(LeftoverDto dto);

    int getAllLeftoverPage();

    List<LeftoverDto> getLeftoverPage(int count);

    List<LeftoverAndPreDto> getAllLeftoverAndPre(int page) throws CantFindByIdException;

    List<LeftoverDto> getLastWeeksLeftovers(int type);
}