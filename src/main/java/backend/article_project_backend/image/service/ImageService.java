package backend.article_project_backend.image.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageService {
    
    private final Cloudinary cloudinary;

    public ImageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile imageFile) throws IOException {
        try {
            Map uploadResponse = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            return uploadResponse.get("secure_url").toString();    
        } catch (IOException e) {
            throw new IOException("Failed to upload image to Cloudinary.");
        }
    }
}
