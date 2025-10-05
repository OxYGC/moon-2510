package com.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Moon2510ApplicationTests {

    @LocalServerPort
    int port;

    @Test
    void testOneMillionWishes() throws Exception {
        String url = "http://localhost:" + port + "/api/wish";
        var client = HttpClient.newHttpClient();

        // 使用 Java 25 的 StructuredTaskScope（已正式发布）
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            for (int i = 0; i < 1_000_000; i++) {
                final int id = i;
                scope.fork(() -> {
                    var request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Content-Type", "application/json")
                            .POST(BodyPublishers.ofString("""
                            {"content":"愿家人平安 %d"}
                            """.formatted(id)))
                            .build();

                    var response = client.send(request, BodyHandlers.ofString());
                    if (response.statusCode() != 200) {
                        throw new RuntimeException("Wish failed: " + response.statusCode());
                    }
                    return null;
                });
            }
            scope.joinUntil(Instant.now().plusSeconds(60)); // 60秒内完成
            scope.throwIfFailed();
        }

        System.out.println("🎉 100 万愿望已送达月宫！系统稳如满月 🌕");
    }

}
