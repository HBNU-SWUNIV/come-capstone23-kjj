package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverAndPreDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
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

    private int dataSize = 5;


    @Transactional
    public void setLeftover(LeftoverDto dto) throws WrongParameter {
        Calculate target = calculateRepository.findByDate(DateTools.makeTodayDateString());
        if (target == null) throw new WrongParameter("정산 데이터가 없습니다.");

        Leftover leftover = leftoverRepository.findByLeftoverPreId(target.getId());
        if (leftover != null) leftover.setLeftover(dto.getLeftover());
        else {
            Leftover result = Leftover.createLeftover(leftoverPreRepository.getReferenceById(target.getId()), dto);
            leftoverRepository.save(result);
        }
    }

    public int getAllLeftoverPage() {
        Pageable pageable = PageRequest.of(0, dataSize);
        Page<Leftover> result = leftoverRepository.findAll(pageable);

        return result.getTotalPages();
    }

    @Transactional
    public List<LeftoverDto> getLeftoverPage(int count) {
        Pageable pageable = PageRequest.of(count, dataSize);
        List<Leftover> result = leftoverRepository.findAllByOrderByLeftoverPreIdDesc(pageable).getContent();

        return result.stream()
                .map((history) -> LeftoverDto.createLeftoverDto(history))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<LeftoverAndPreDto> getAllLeftoverAndPre(int page) throws CantFindByIdException {
        Pageable pageable = PageRequest.of(page, dataSize);
        List<Long> calculates = calculateRepository.findAllByOrderByIdDesc(pageable).getContent().stream().map(calculate -> calculate.getId()).collect(Collectors.toList());

        List<LeftoverAndPreDto> result = new ArrayList<>();
        for (Long id : calculates) {
            Leftover leftover = leftoverRepository.findById(id).orElse(new Leftover(null, null, 0));
            LeftoverPre leftoverPre = leftoverPreRepository.findById(id).orElseThrow(CantFindByIdException::new);

            result.add(LeftoverAndPreDto.createLeftoverAndPreDto(leftover, leftoverPre));
        }

        return result;
    }

    @Transactional
    public List<LeftoverDto> getLastWeeksLeftovers(int type) throws WrongParameter {
        List<LeftoverDto> result = new ArrayList<>();
        LocalDate date = DateTools.getLastWeeksMonday(type);

        for (int i = 0; i < dataSize; i ++) {
            String targetDate = DateTools.toFormatterString(date.plusDays(i));

            Calculate target = calculateRepository.findByDate(targetDate);
            if (target == null) {
                result.add(new LeftoverDto(targetDate, 0.0));
                continue;
            }

            Leftover leftover = leftoverRepository.findByLeftoverPreId(target.getId());
            if (leftover == null) result.add(new LeftoverDto(targetDate, 0.0));
            else result.add(LeftoverDto.createLeftoverDto(leftover));

        }

        return result;
    }
}
