package com.shops.dreamshops.controller;

import com.shops.dreamshops.dto.ImageDto;
import com.shops.dreamshops.exception.ImageNotFoundException;
import com.shops.dreamshops.exception.ProductNotFoundException;
import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.model.Image;
import com.shops.dreamshops.response.ApiResponse;
import com.shops.dreamshops.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {

        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload successful", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<?> downloadImage(@PathVariable Long imageId) throws SQLException {
        try {
            Image image = imageService.getImageById(imageId);

            if (image == null) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image Not Found", null));
            }

            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                    .body(resource);
        } catch (ImageNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image Not Found", null));
        } catch (SQLException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving image", null));
        }
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam MultipartFile file) {

        try {
            Image image = imageService.getImageById(imageId);

            if (image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok().body(new ApiResponse("Update successful", image));

            }
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Update failed", e.getMessage()));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("update failed",null));
    }

    @DeleteMapping ("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {

        try {
            Image image = imageService.getImageById(imageId);

            if (image != null) {
                imageService.deleteImageById( imageId);
                return ResponseEntity.ok().body(new ApiResponse("delete successful", null));

            }
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("delete failed", e.getMessage()));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("delete failed",null));
    }


    @GetMapping("image/by/{id}")
    public ResponseEntity<ApiResponse> findByProductId(@PathVariable  Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("images",imageService.findByProductId(id)));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NOT FOUND",null));
        }
    }


}