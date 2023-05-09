package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverAndPreDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.calculate.CalculateRepository;
import com.hanbat.zanbanzero.repository.leftover.LeftoverPreRepository;
import com.hanbat.zanbanzero.repository.leftover.LeftoverRepository;
import com.hanbat.zanbanzero.service.DateTools;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeftoverService {

    private final CalculateRepository calculateRepository;
    private final LeftoverRepository leftoverRepository;
    private final LeftoverPreRepository leftoverPreRepository;

    private int pageSize = 5;


    @Transactional
    public void setLeftover(LeftoverDto dto) throws WrongRequestDetails {
        Calculate target = calculateRepository.findByDate(DateTools.makeTodayDateString());

        Leftover result = Leftover.createLeftover(leftoverPreRepository.getReferenceById(target.getId()), dto);

        leftoverRepository.save(result);
    }

    public int getAllLeftoverPage() {
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<Leftover> result = leftoverRepository.findAll(pageable);

        return result.getTotalPages();
    }

    @Transactional
    public List<LeftoverDto> getLeftoverPage(int count) {
        Pageable pageable = PageRequest.of(count, pageSize);
        List<Leftover> result = leftoverRepository.findAllByOrderByLeftoverPreIdDesc(pageable).getContent();

        return result.stream()
                .map((history) -> LeftoverDto.createLeftoverDto(history))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<LeftoverAndPreDto> getAllLeftoverAndPre(int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Leftover> data = leftoverRepository.findAllByOrderByLeftoverPreIdDesc(pageable).getContent();
        List<LeftoverPre> data2 = leftoverPreRepository.findAllByOrderByCalculateIdDesc(pageable).getContent();

        List<LeftoverAndPreDto> result = new ArrayList<>();
        for (int i = 0; i < pageSize; i ++) {
            result.add(LeftoverAndPreDto.createLeftoverAndPreDto(data.get(i), data2.get(i)));
        }

        return result;
    }

    @Transactional
    public List<LeftoverDto> getLastWeeksLeftovers(int type) throws WrongParameter {
        if (type != 0 && type != 1) {
            throw new WrongParameter("잘못된 타입입니다.");
        }
        int weekSize = 5;
        List<LeftoverDto> result = new ArrayList<>();

        LocalDate date = DateTools.getLastWeeksMonday(type);

        for (int i = 0; i < weekSize; i ++) {
            String targetDate = DateTools.toFormatterString(date.plusDays(i));

            Calculate target = calculateRepository.findByDate(targetDate);
            if (target == null) {
                result.add(new LeftoverDto(targetDate, 0.0));
                continue;
            }

            Leftover leftover = leftoverRepository.findByLeftoverPreId(target.getId());
            if (leftover == null) {
                result.add(new LeftoverDto(targetDate, 0.0));
            }
            else {
                result.add(LeftoverDto.createLeftoverDto(leftover));
            }
        }

        return result;
    }
}
