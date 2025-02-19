package com.magic_saurabh.controllerTest;

import com.magic_saurabh.controller.BookUploadController;
import com.magic_saurabh.service.BookUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

class BookUploadControllerTest {

    @Mock
    private BookUploadService bookUploadService;

    @Mock
    private FilePart filePart;

    @InjectMocks
    private BookUploadController bookUploadController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(bookUploadController).build();
    }


    @Test
    void testUploadNoFileProvided() {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        webTestClient.post()
                .uri("/books/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(bodyBuilder.build())
                .exchange()
                .expectStatus().isBadRequest();
    }
}
