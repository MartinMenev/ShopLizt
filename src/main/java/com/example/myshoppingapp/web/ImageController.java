package com.example.myshoppingapp.web;

import com.example.myshoppingapp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/db-resources/images")
public class ImageController {


    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/{id}")
    public void writePicture(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            var imageDownloadModel = imageService.getFileById(id).orElseThrow();
            response.setContentType("image/jpeg");
            response.setContentLength(imageDownloadModel.getFileData().length);


            response.getOutputStream().write(imageDownloadModel.getFileData());
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch(Exception e){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404. Add specific catch for specific errors
        }
    }



}
