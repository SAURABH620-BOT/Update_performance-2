package com.magic_saurabh.controller;

import com.magic_saurabh.service.BookUploadService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;





@RestController
@RequestMapping("/books/upload")
public class BookUploadController {

    private final BookUploadService bookUploadService;

    public BookUploadController(BookUploadService bookUploadService) {
        this.bookUploadService = bookUploadService;
    }

    @PostMapping
    public Mono<Void> uploadFile(@Parameter(
                                        description = "The file to upload",
                                        required = true,
                                        schema = @Schema(type = "string", format = "binary"))
                                     @RequestPart("file") FilePart filePart) {
        return bookUploadService.uploadBooks(filePart);

    }
}
