package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.pictures.ImageDownloadModel;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.service.ImageService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class DownloadController {

  private final ImageService imageService;

  public DownloadController(ImageService imageService) {
    this.imageService = imageService;
  }


  @GetMapping("/show/{id}")
  public String show(@PathVariable("id") long id, Model model) {

    model.addAttribute("id", id);
    return "/img/show";
  }

  @GetMapping("/download/{id}")
  public HttpEntity<byte[]> download(@PathVariable("id") long imageId) {

    var imageDownloadModel = imageService.getFileById(imageId).orElseThrow();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType(MimeTypeUtils.parseMimeType(imageDownloadModel.getContentType())));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+imageDownloadModel.getFileName());
    headers.setContentLength(imageDownloadModel.getFileData().length);

    return new HttpEntity<>(imageDownloadModel.getFileData(), headers);
  }

//  @RequestMapping(value = "/images/", method = RequestMethod.GET)
//  public void showImage(@RequestParam("id") Long imageId, HttpServletResponse response, HttpServletRequest request)
//          throws ServletException, IOException {
//
//
//    ImageEntity image = imageService.findById(imageId).orElseThrow();
//    response.setContentType("image/jpeg" +  "image/jpg, image/png, image/gif");
//    response.getOutputStream().write(image.getData());
//
//
//    response.getOutputStream().close();
//  }

}
