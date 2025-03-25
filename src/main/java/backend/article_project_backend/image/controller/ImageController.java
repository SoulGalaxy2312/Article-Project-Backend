package backend.article_project_backend.image.controller;

import backend.article_project_backend.image.service.ImageService;
import backend.article_project_backend.utils.common.path.AppPaths;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(AppPaths.IMAGE_URI)
public class ImageController {
    
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // @PostMapping("/upload")
    // public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
    //     try {
    //         String imageUrl = imageService.uploadImage(file);
    //         Map<String, String> response = new HashMap<>();
    //         response.put("imageUrl", imageUrl);
    //         return ResponseEntity.ok(response);
    //     } catch (IOException e) {
    //         return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
    //     }
    // }
}
