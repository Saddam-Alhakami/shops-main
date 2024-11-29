package com.shops.dreamshops.service.image;

import com.shops.dreamshops.dto.ImageDto;
import com.shops.dreamshops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, Long ImageId);
    List<Image> findByProductId(Long id);



}
