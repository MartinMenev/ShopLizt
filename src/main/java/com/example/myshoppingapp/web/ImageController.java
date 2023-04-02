package com.example.myshoppingapp.web;

import com.example.myshoppingapp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequestMapping(value = "/db-resources/images")
public class ImageController {


    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/{id}")
    public void writePicture(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        try{
            var imageDownloadModel = imageService.getFileById(id).orElseThrow();
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.setContentLength(imageDownloadModel.getFileData().length);


            response.getOutputStream().write(imageDownloadModel.getFileData());
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch(Exception e){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404. Add specific catch for specific errors
        }
    }

    @DeleteMapping("/remove-picture/{id}/{recipeId}")
    public String deleteComment(@PathVariable(value = "id") long id,
                                @PathVariable(value = "recipeId") long recipeId,
                                Principal user) {
        this.imageService.deleteImage(id, user.getName(), recipeId);

        return "redirect:/recipe/" + recipeId;
    }

}
