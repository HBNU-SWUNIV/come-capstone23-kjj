package com.hanbat.zanbanzero.service.store;

import com.hanbat.zanbanzero.dto.calculate.CalculateMenuForGraphDto;
import com.hanbat.zanbanzero.dto.store.*;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.SameNameException;
import com.hanbat.zanbanzero.exception.exceptions.StringToMapException;
import com.hanbat.zanbanzero.exception.exceptions.UploadFileException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StoreService {
    StoreDto isSetting();

    StoreDto getStoreData() throws CantFindByIdException;

    StoreDto setSetting(StoreSettingDto dto) throws SameNameException;

    void setStoreImage(MultipartFile file) throws CantFindByIdException, UploadFileException;

    Integer getToday();

    List<StoreWeekendDto> getLastWeeksUser();

    int getAllUsers();

    List<CalculateMenuForGraphDto> getPopularMenus();

    StoreDto updateStoreTitle(StoreTitleDto dto) throws CantFindByIdException;

    StoreDto updateStoreInfo(StoreInfoDto dto) throws CantFindByIdException;

    StoreStateDto setOff(StoreOffDto dto, int year, int month, int day);

    List<StoreStateDto> getClosedDays(int year, int month);

    Integer getCalculatePreUser();

    Map<String, Integer> getCalculatePreFood() throws StringToMapException;

    Map<String, Integer> getCalculatePreMenu() throws StringToMapException;
}
