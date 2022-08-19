package org.dbilik.fileServer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DownloadFileTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test_inputStreamResource() throws Exception {
        ResponseEntity<ByteArrayResource> responseEntity = restTemplate.getForEntity("/v1/file/toResponse/" + FileTestUtils.TXT_FILE_NAME, ByteArrayResource.class);

        printResponseEntity(responseEntity);

        Assertions.assertTrue(responseEntity.getStatusCode().value() >= 200 && responseEntity.getStatusCode().value() < 300);
        Assertions.assertTrue(responseEntity.getBody().contentLength() > 1);
    }

    //@Test
    void tttt() {
        ResponseEntity<InputStream> responseEntity = restTemplate.getForEntity("/v1/file/toResponse/" + FileTestUtils.TXT_FILE_NAME, InputStream.class);


        printResponseEntity(responseEntity);
    }

    private void printResponseEntity(ResponseEntity responseEntity) {
        System.out.println("Status Code: " + responseEntity.getStatusCode());
        System.out.println("Headers:");
        System.out.println(
                responseEntity.getHeaders().entrySet().stream()
                        .map(entry -> {
                            List<String> values = entry.getValue();
                            return entry.getKey() + ": " + (values.size() == 1 ?
                                    "\"" + values.get(0) + "\"" :
                                    values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
                        })
                        .collect(Collectors.joining("\n", "[", "]"))
        );
        System.out.println("Body:");
        System.out.println("instance of body: " + responseEntity.getBody());
        System.out.println(responseEntity.getBody().toString());
    }

}
