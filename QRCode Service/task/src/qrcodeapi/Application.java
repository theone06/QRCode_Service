package qrcodeapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.Map;

@SpringBootApplication
public class Application {

   ObjectMapper objectMapper = new ObjectMapper();
   String typeError = objectMapper.writeValueAsString(Map.of("error", "Only png, jpeg and gif" +
           " image types are supported"));
   String sizeError = objectMapper.writeValueAsString(Map.of("error", "Image size must be between " +
           "150 and 350 pixels"));

    public Application() throws JsonProcessingException {
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public BufferedImage createQrCodeImage(String data, int size) {
        QRCodeWriter writer = new QRCodeWriter();
        BufferedImage bufferedImage = null;
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size);
            bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            System.out.println(e.getMessage());
        }
        return bufferedImage;
    }


    public boolean isValidPixelSize(int input) {
       return input >= 150 && input <= 350;
    }

    public boolean isValidMediaType(MediaType type) {
        return type.equals(MediaType.IMAGE_PNG)
                || type.equals(MediaType.IMAGE_JPEG)
                || type.equals(MediaType.IMAGE_GIF);
    }


    @RestController
    public class QrCodeController {
        @GetMapping(path = "/api/health")
        public ResponseEntity<String> getHealth() {
            return ResponseEntity
                    .ok()
                    .body("200 OK");
        }

        @GetMapping(path = "/api/qrcode")
        public ResponseEntity<?> getImage(@RequestParam(required = true) String contents, int size, @RequestParam(required = true) String type) throws JsonProcessingException {
            MediaType mediaType = MediaType.parseMediaType("Image/" + type);
            if (contents == null || contents.isBlank()) {
                return ResponseEntity
                        .badRequest()
                        .body(objectMapper.writeValueAsString(Map.of("error", "Contents cannot be null or blank")));
            }
            if (isValidPixelSize(size)) {
                if (isValidMediaType(mediaType)) {
                    BufferedImage bufferedImage = createQrCodeImage(contents, size);
                    return ResponseEntity
                            .ok()
                            .headers(httpHeaders -> httpHeaders.setContentType(mediaType))
                            .body(bufferedImage);
                } else {
                    return ResponseEntity
                            .badRequest()
                            .body(typeError);
                }
            }
            return ResponseEntity
                    .badRequest()
                    .body(sizeError);
        }
    }

    @Bean
    public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

}

