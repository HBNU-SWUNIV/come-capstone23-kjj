package com.hanbat.zanbanzero.service.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverAndPreDto;
import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.entity.calculate.Calculate;
import com.hanbat.zanbanzero.entity.leftover.Leftover;
import com.hanbat.zanbanzero.entity.leftover.LeftoverPre;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.calculate.CalculateRepository;
import com.hanbat.zanbanzero.repository.leftover.LeftoverPreRepository;
import com.hanbat.zanbanzero.repository.leftover.LeftoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (dto.getLeftover() == null) {
            throw new WrongRequestDetails("데이터가 부족합니다.");
        }

        Calculate target = calculateRepository.findByDate(dto.getDate());

        if (target == null) {
            throw new WrongRequestDetails("잘못된 날짜 정보입니다.");
        }

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
}
