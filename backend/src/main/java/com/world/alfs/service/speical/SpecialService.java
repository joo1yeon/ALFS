package com.world.alfs.service.speical;

import com.world.alfs.common.exception.CustomException;
import com.world.alfs.common.exception.ErrorCode;
import com.world.alfs.controller.speical.response.GetSpecialListResponse;
import com.world.alfs.controller.speical.response.GetSpecialResponse;
import com.world.alfs.domain.product.Product;
import com.world.alfs.domain.product.repository.ProductRepository;
import com.world.alfs.domain.product_img.ProductImg;
import com.world.alfs.domain.product_img.repostiory.ProductImgRepository;
import com.world.alfs.domain.special.Special;
import com.world.alfs.domain.special.repository.SpecialRepository;
import com.world.alfs.domain.supervisor.Supervisor;
import com.world.alfs.domain.supervisor.repository.SupervisorRepository;
import com.world.alfs.service.speical.dto.AddSpecialDto;
import com.world.alfs.service.speical.dto.DeleteSpecialDto;
import com.world.alfs.service.speical.dto.SetSpecialDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class SpecialService {

    private final SpecialRepository specialRepository;
    private final ProductRepository productRepository;
    private final SupervisorRepository supervisorRepository;
    private final ProductImgRepository productImgRepository;

    public Long addSpecial(AddSpecialDto dto) {
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(()->new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        Supervisor supervisor = supervisorRepository.findById(dto.getSupervisorId()).orElseThrow(()->new CustomException(ErrorCode.SUPERVISOR_NOT_FOUND));

        Optional<Special> existingSpecial = specialRepository.findById(dto.getProductId());
        if (existingSpecial.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_SPECIAL_ID);
        }

        Special special = dto.toEntity(product, supervisor);
        specialRepository.save(special);

        return special.getId();
    }

    public List<GetSpecialListResponse> getAllSpecial(){

        List<Special> specialList = specialRepository.findAll();

        return specialList.stream()
                .map(special -> {
                    ProductImg img = productImgRepository.findByProductId(special.getProduct().getId());
                    return special.toGetSpecialListResponse(img);
                })
                .collect(Collectors.toList());
    }

    public GetSpecialResponse getSpecial(Long id){
        Special special = specialRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        ProductImg img = productImgRepository.findByProductId(special.getProduct().getId());
        return special.toGetSpecialResponse(img);
    }

    public Long setSpecial(Long id, SetSpecialDto dto){

        // 이벤트 특가상품이 존재하는지 확인
        Product product = productRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        Special special= specialRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 이벤트 특가상품의 관리자와 dto에 보내준 관리자 id와 일치하는지 비교
        Supervisor supervisor = supervisorRepository.findById(special.getSupervisor().getId()).orElseThrow(()->new CustomException(ErrorCode.SUPERVISOR_NOT_FOUND));
        if (!special.getSupervisor().getId().equals(dto.getSupervisorId())) {
            throw new CustomException(ErrorCode.SUPERVISOR_ID_MISMATCH);
        }

        special.setSpecial(dto, product, supervisor);
        return special.getId();
    }

    public Long deleteSpecial(Long id, DeleteSpecialDto dto){

        // 이벤트 특가상품이 존재하는지 확인
        Special special= specialRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // id에 해당하는 이벤트 특가상품의 관리자와 dto에 보내준 관리자 id와 일치하는지 비교
                if (!special.getSupervisor().getId().equals(dto.getSupervisorId())) {
            throw new CustomException(ErrorCode.SUPERVISOR_ID_MISMATCH);
        }

        specialRepository.deleteById(id);
        return id;
    }


}