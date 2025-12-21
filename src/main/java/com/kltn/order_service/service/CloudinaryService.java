package com.kltn.order_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    private String extractPublicIdFromUrl(String url) {
        return url.replaceAll("^.*/upload/v\\d+/", "").replaceFirst("\\.[^.]+$", "");
    }

    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String, Object> responce = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderName));
        return (String) responce.get("secure_url");
    }

    @SuppressWarnings("rawtypes")
    public Map uploadVideo(MultipartFile file, String folderName) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", folderName));
    }

    public void deleteFilesByUrls(List<String> urls) {
        try {
            urls.forEach(url -> {
                try {
                    String publicId = extractPublicIdFromUrl(url);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> response = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    System.out.println("Xóa " + publicId + ": " + response.get("result"));
                } catch (IOException e) {
                    System.err.println("Lỗi khi xóa " + url + ": " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Lỗi khi xử lý danh sách URL: " + e.getMessage());
        }
    }

}
