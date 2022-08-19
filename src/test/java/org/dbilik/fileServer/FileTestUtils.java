package org.dbilik.fileServer;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class FileTestUtils {

    public static final String TXT_FILE_NAME = "download.txt";
    public static final FilePointer TXT_FILE = txtFile();
    public static final String NOT_FOUND_FILE = "random.txt";

    private static SystemFilePointer txtFile() {
        final URL url = FileTestUtils.class.getClassLoader().getResource(TXT_FILE_NAME);
        final File file = new File(url.getFile());
        return new SystemFilePointer(file, TXT_FILE_NAME);
    }

    public static void printResponseEntity(ResponseEntity responseEntity) {
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
