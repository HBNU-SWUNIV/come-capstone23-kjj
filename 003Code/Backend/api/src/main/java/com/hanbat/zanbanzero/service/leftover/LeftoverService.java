package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverAndPreDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
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

@Service
@RequiredArgsConstructor
public class LeftoverService {

    private final CalculateRepository calculateRepository;
    private final LeftoverRepository leftoverRepository;
    private final LeftoverPreRepository leftoverPreRepository;

    private static final int DATA_SIZE = 5;

    @Transactional
    public LeftoverDto setLeftover(LeftoverDto dto) throws WrongParameter {
        Calculate target = calculateRepository.findByDate(DateTools.makeTodayToLocalDate());
        if (target == null) throw new WrongParameter("Calculate : null");

        Leftover leftover = leftoverRepository.findByLeftoverPreId(target.getId());
        if (leftover != null) leftover.setLeftover(dto.getLeftover());
        else {
            leftover = Leftover.of(leftoverPreRepository.getReferenceById(target.getId()), dto);
            leftoverRepository.save(leftover);
        }
        return LeftoverDto.of(leftover);
    }

    public int getAllLeftoverPage() {
        Pageable pageable = PageRequest.of(0, DATA_SIZE);
        Page<Leftover> result = leftoverRepository.findAll(pageable);

        return result.getTotalPages();
    }

    @Transactional
    public List<LeftoverDto> getLeftoverPage(int count) {
        Pageable pageable = PageRequest.of(count, DATA_SIZE);
        List<Leftover> result = leftoverRepository.findAllByOrderByLeftoverPreIdDesc(pageable).getContent();

        return result.stream()
                .map(LeftoverDto::of)
                .toList();
    }

    @Transactional
    public List<LeftoverAndPreDto> getAllLeftoverAndPre(int page) throws CantFindByIdException {
        Pageable pageable = PageRequest.of(page, DATA_SIZE);
        List<Long> calculates = calculateRepository.findAllByOrderByIdDesc(pageable)
                .getContent().stream()
                .map(Calculate::getId)
                .toList();

        List<LeftoverAndPreDto> result = new ArrayList<>();
        for (Long id : calculates) {
            Leftover leftover = leftoverRepository.findById(id).orElse(new Leftover(null, null, 0));
            LeftoverPre leftoverPre = leftoverPreRepository.findById(id).orElseThrow(CantFindByIdException::new);

            result.add(LeftoverAndPreDto.of(leftover, leftoverPre));
        }

        return result;
    }

    @Transactional
    public List<LeftoverDto> getLastWeeksLeftovers(int type) throws WrongParameter {
        List<LeftoverDto> result = new ArrayList<>();
        LocalDate date = DateTools.getLastWeeksMonday(type);

        for (int i = 0; i < DATA_SIZE; i ++) {
            LocalDate targetDate = date.plusDays(i);

            Calculate target = calculateRepository.findByDate(targetDate);
            if (target == null) {
                result.add(LeftoverDto.of(targetDate, 0.0));
                continue;
            }

            Leftover leftover = leftoverRepository.findByLeftoverPreId(target.getId());
            if (leftover == null) result.add(LeftoverDto.of(targetDate, 0.0));
            else result.add(LeftoverDto.of(leftover));
        }
        return result;
    }
}
