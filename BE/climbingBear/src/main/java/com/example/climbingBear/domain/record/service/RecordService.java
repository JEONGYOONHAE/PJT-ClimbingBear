package com.example.climbingBear.domain.record.service;

import com.example.climbingBear.domain.mntn.entity.Mountain;
import com.example.climbingBear.domain.mntn.exception.NoExistMntnException;
import com.example.climbingBear.domain.mntn.repository.MntnRepository;
import com.example.climbingBear.domain.record.Exception.NoRecordException;
import com.example.climbingBear.domain.record.dto.*;
import com.example.climbingBear.domain.record.entity.Record;
import com.example.climbingBear.domain.record.repository.RecordRepository;
import com.example.climbingBear.domain.user.entity.User;
import com.example.climbingBear.domain.user.exception.NoExistUserException;
import com.example.climbingBear.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final MntnRepository mntnRepository;

    // 등산 기록 저장
    public RecordPostResDto createRecord(RecordPostReqDto dto, Long userSeq) throws Exception {
        User user = userRepository.findByUserSeq(userSeq).orElseThrow(() ->
                new NoExistUserException());
        if (recordRepository.existsByUserAndYearAndMonthAndDay(user, dto.getYear(), dto.getMonth(), dto.getDay())){
            Record oldRecord = recordRepository.findByUserAndYearAndMonthAndDay(user, dto.getYear(), dto.getMonth(), dto.getDay()).orElseThrow(() ->
                    new NoRecordException());
                recordRepository.delete(oldRecord);
        }
        Mountain mntn = mntnRepository.findByMntnSeq(dto.getMntnSeq()).orElseThrow(() ->
                new NoExistMntnException());
        Record record = dto.toRecordEntity(user, mntn);
        recordRepository.save(record);
        return RecordPostResDto.of(record.getRecordSeq());
    }
}
