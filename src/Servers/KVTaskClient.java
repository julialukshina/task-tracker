package Servers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String URL;
    private String API_KEY;
    private HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String URL) {
        this.URL = URL;
        registration();
    }

    private void registration() {// метод регистрации
        URI uri = URI.create(URL + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = null;
        try {
          response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Регистрация не пройдена. Повторите попытку");
        }

        API_KEY=response.body();
    }

public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create(URL+ "/save/" +key + "?API_KEY="+ API_KEY);
    HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .uri(uri)
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    HttpResponse<String> response = client.send(request, handler);
    if(response.statusCode()==400){
        throw new IllegalArgumentException("Ключ или значение не введены");
    }
    if(response.statusCode()==403){
        throw new IllegalStateException("Регистрация не пройдена");
    }
    if(response.statusCode()==405){
        throw new IllegalArgumentException("Направлен некорректный запрос");
    }
}

    String load (String key) throws IOException, InterruptedException {
        URI uri = URI.create(URL+ "/load/" +key + "?API_KEY="+ API_KEY);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        if(response.statusCode()==400){
            throw new IllegalArgumentException("Ключ не введен");
        }
        if(response.statusCode()==403){
            throw new IllegalStateException("Регистрация не пройдена");
        }
        if(response.statusCode()==404){
            throw new IllegalStateException("По указанному ключу данные отсутствуют");
        }
        if(response.statusCode()==405){
            throw new IllegalArgumentException("Направлен некорректный запрос");
        }
return response.body();
    }

}
