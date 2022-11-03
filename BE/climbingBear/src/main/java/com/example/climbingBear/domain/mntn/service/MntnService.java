package com.example.climbingBear.domain.mntn.service;

import com.example.climbingBear.domain.diary.exception.NoExistMntnException;
import com.example.climbingBear.domain.mntn.dto.MntnDetailReqDto;
import com.example.climbingBear.domain.mntn.dto.MntnDetailResDto;
import com.example.climbingBear.domain.mntn.dto.MntnListResDto;
import com.example.climbingBear.domain.mntn.dto.MntnResDto;
import com.example.climbingBear.domain.mntn.entity.Mountain;

import com.example.climbingBear.domain.mntn.entity.Spot;
import com.example.climbingBear.domain.mntn.repository.MntnRepository;
import com.example.climbingBear.domain.mntn.repository.SpotRepository;
import com.example.climbingBear.domain.user.dto.UserListResDto;
import com.example.climbingBear.domain.user.entity.User;
import com.example.climbingBear.domain.user.exception.NoExistUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MntnService {

    private final MntnRepository mntnRepository;
    private final SpotRepository spotRepository;

    public MntnResDto mntnDetail(Long mntnSeq){
        Mountain mntn = mntnRepository.findByMntnSeq((mntnSeq)).orElseThrow(() ->
                new NoExistMntnException());
        Spot spot = spotRepository.findByMntnNm(mntn).orElseThrow(() ->
                new NoExistUserException());
        MntnResDto mntnResDto = MntnResDto.ofSpot(spot);
        return mntnResDto;
    }

    public List<MntnListResDto> findAllMountain(){
        List<Mountain>  mountains = mntnRepository.findAll(Sort.by(Sort.Direction.ASC, "mntnSeq"));
        return mountains.stream().map(MntnListResDto::new).collect(Collectors.toList());
    }

    public MntnDetailResDto getMntnDetail(Long mntnSeq){
        Mountain mntn = mntnRepository.findByMntnSeq(mntnSeq).orElseThrow(() ->
                new NoExistMntnException());
        return MntnDetailResDto.ofMntnDetail(mntn);
    }

}
