package com.shops.dreamshops.service.image;

import com.shops.dreamshops.dto.ImageDto;
import com.shops.dreamshops.exception.ImageNotFoundException;
import com.shops.dreamshops.exception.ProductNotFoundException;
import com.shops.dreamshops.model.Image;
import com.shops.dreamshops.model.Product;
import com.shops.dreamshops.repository.ImageRepository;
import com.shops.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService iProductService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()-> new ImageNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                ()-> { throw new ImageNotFoundException("Image not found");
                });

    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = iProductService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFilename(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownload="api/v1/images/image/download/";
                image.setDownloadUrl(buildDownload+image.getId());
                Image savedImage=imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownload+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setFileName(savedImage.getFilename());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (IOException |SQLException e) {
                throw new RuntimeException(e.getMessage());
            }

        }
        return  savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long ImageId) {
        Image image = getImageById(ImageId);

        try {
            image.setFilename(file.getOriginalFilename());
            image.setFilename(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException |SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public List<Image> findByProductId(Long id) {
        return Optional.ofNullable(imageRepository.findByProductId(id))
                .filter(images -> !images.isEmpty()) // Zorgt ervoor dat de lijst niet leeg is
                .orElseThrow(() -> new ProductNotFoundException("No images found for product with id: " + id));
    }




}
