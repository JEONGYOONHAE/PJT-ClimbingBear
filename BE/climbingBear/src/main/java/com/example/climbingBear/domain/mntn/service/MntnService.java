package com.example.climbingBear.domain.mntn.service;

import com.example.climbingBear.domain.mntn.exception.NoExistMntnException;
import com.example.climbingBear.domain.mntn.dto.*;
import com.example.climbingBear.domain.mntn.entity.*;

import com.example.climbingBear.domain.mntn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Dictionary;
import java.util.List;
import java.util.stream.Collectors;

import static sun.swing.MenuItemLayoutHelper.max;

@Service
@RequiredArgsConstructor
@Transactional
public class MntnService {

    private final MntnRepository mntnRepository;
    private final SpotRepository spotRepository;
    private final FeatureRepository featureRepository;
    private final PathRepository pathRepository;
    private final MntnPlaceRepository mntnPlaceRepository;

    // 전체 산 조회
    @Transactional
    public List<MntnListResDto> findAllMountain(){
        List<Mountain>  mountains = mntnRepository.findAll(Sort.by(Sort.Direction.ASC, "mntnSeq"));
        return mountains.stream().map(MntnListResDto::new).collect(Collectors.toList());
    }

    // 특정 산 상세정보 조회
    @Transactional
    public MntnDetailResDto getMntnDetail(Long mntnSeq) {
        Mountain mntn = mntnRepository.findByMntnSeq(mntnSeq).orElseThrow(() ->
                new NoExistMntnException());
        List place = findMntnPlace(mntn);
        List<Feature> features = featureRepository.findByMntn(mntn);
        String level = null;
        Integer easy = 0;
        Integer middle = 0;
        Integer hard = 0;
        for (Feature f : features) {
            level = f.getPmtnDffl();
            if (level == "쉬움"){
                easy += 1;
            } else if (level == "중간") {
                middle += 1;
            } else {
                hard += 1;
            }
        }
        // 등산로 전체 중 가장 빈도 높은 난이도를 대표 난이도로 선정
        String result = null;
        if (max(easy, middle, hard) == easy) {
            result = "쉬움";
        } else if (max(easy, middle, hard) == middle) {
            result = "중간";
        }else {
            result = "어려움";
        }
        return MntnDetailResDto.ofMntnDetail(mntn, place, result);
    }

    // 산 특정 장소 조회
    public List<MntnPlaceListResDto> findMntnPlace(Mountain mntn){
        List<MountainPlace> mntnPlace = mntnPlaceRepository.findByMntn(mntn);
        return mntnPlace.stream().map(MntnPlaceListResDto::new).collect(Collectors.toList());
    }

    // 특정 산 특징 조회
    public List<MntnFeatureResDto> getMntnFeature(Long mntnSeq){
        Mountain mntn = mntnRepository.findByMntnSeq(mntnSeq).orElseThrow(() ->
                new NoExistMntnException());
        List<Feature> features = featureRepository.findByMntn(mntn);
//        List<Feature> features = featureRepository.findByMntn(mntn);
        for(Feature f : features){
            System.out.println(f.getFetureSeq());
            List paths = findMntnPath(f);
            f.setPaths(paths);
        }
        return features.stream().map(MntnFeatureResDto::new).collect(Collectors.toList());
    }

    // 산 정보 제공 페이지에서 정상 위도 경도 리스트
    public List<MntnPathListResDto> findMntnPath(Feature feature){
        List<Path> paths = pathRepository.findByFeature(feature);
        return paths.stream().map(MntnPathListResDto::new).collect(Collectors.toList());
    }

    // 등산 기록 페이지에서 등산로 위도 경도 json
    public MntnPathJsonResDto getMntnPathList(Long mntnSeq){
        Mountain mntn = mntnRepository.findByMntnSeq(mntnSeq).orElseThrow(() ->
                new NoExistMntnException());
        return MntnPathJsonResDto.of(mntn);
    }
}

