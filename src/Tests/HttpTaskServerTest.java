package Tests;

import Managers.Managers;
import Managers.TaskManager;
import Servers.HttpTaskServer;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskServerTest {//класс для тестирования HttpTaskManager
    private HttpTaskServer server;
    HttpClient client = HttpClient.newHttpClient();
    private final String URL = "http://localhost:8080/tasks";
    private HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    private final String TASK_JSON = "{\"name\":\"name\",\"description\":\"description\",\"status\":\"NEW\",\"id\":1," +
            "\"duration\":{\"seconds\":1800,\"nanos\":0},\"startTime\":{\"date\":{\"year\":-999999999,\"month\":1,\"" +
            "day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\"" +
            ":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":30,\"second\":0,\"nano\":0}}}";
    private final String EPIC_JSON = "{\"subTasksOfEpic\":[{\"epic\":2,\"name\":\"Поход к врачу\",\"description\":" +
            "\"до 15 февраля\",\"status\":\"NEW\",\"id\":3}],\"name\":\"Поход к врачу\",\"description\":" +
            "\"до 15 февраля\",\"status\":\"NEW\",\"id\":2}";
    private final String SUBTASK_JSON = "{\"epic\":2,\"name\":\"Поход к врачу\",\"description\":\"до 15 февраля\"," +
            "\"status\":\"NEW\",\"id\":3}";
    private final String HISTORY_JSON = "[{\"name\":\"name\",\"description\":\"description\",\"status\":\"NEW\"," +
            "\"id\":1,\"duration\":{\"seconds\":1800,\"nanos\":0},\"startTime\":{\"date\":{\"year\":-999999999,\"" +
            "month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\"" +
            ":{\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":30,\"second\":0,\"nano\":0}}}" +
            ",{\"subTasksOfEpic\":[{\"epic\":2,\"name\":\"Поход к врачу\",\"description\":\"до 15 февраля\",\"status\"" +
            ":\"NEW\",\"id\":3}],\"name\":\"Поход к врачу\",\"description\":\"до 15 февраля\",\"status\":\"NEW\"," +
            "\"id\":2},{\"epic\":2,\"name\":\"Поход к врачу\",\"description\":\"до 15 февраля\",\"status\":\"NEW\"," +
            "\"id\":3}]";
    private final String ALL_TASKS_JSON = "[{\"name\":\"name\",\"description\":\"description\",\"status\":\"NEW\"," +
            "\"id\":1,\"duration\":{\"seconds\":1800,\"nanos\":0},\"startTime\":{\"date\":{\"year\":-999999999,\"" +
            "month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\"" +
            ":{\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":30,\"second\":0,\"nano\":0}}}" +
            ",{\"epic\":2,\"name\":\"Поход к врачу\",\"description\":\"до 15 февраля\",\"status\":\"NEW\",\"id\":3}]";
    Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    if(f.getName().equals("TYPE")){
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).create();
    private Task task;
    private Epic epic;
    private SubTask subtask;

    @BeforeEach
    void createServer() throws IOException {
        server = new HttpTaskServer();
        server.startServer();
    }

    @AfterEach
    void stop(){
        server.stopServer();
    }

    @Test
    void correctResponseTasks() throws IOException, InterruptedException {//тест корректного ответа сервера на запросы post и delete в tasks
                URI uri = URI.create(URL);
        HttpRequest requestPost = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("Не обработается"))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse <String> response = client.send(requestPost, handler);
        assertEquals(400, response.statusCode());
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestDelete, handler);
        assertEquals(400, response.statusCode());
    }

    @Test
    void methodGetEmpty() throws IOException, InterruptedException {
        URI uri = URI.create(URL);
        HttpRequest requestGetTreeSet = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse <String> response = client.send(requestGetTreeSet, handler);
        assertEquals("[]", response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetHistory = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/history"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetHistory, handler);
        assertEquals("[]", response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetTasks = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetTasks, handler);
        assertEquals("[]", response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetSubTasks = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetSubTasks, handler);
        assertEquals("[]", response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetEpics = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetEpics, handler);
        assertEquals("[]", response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void methodsPostGetDelete() throws IOException, InterruptedException {
        createTasks();
        //тестируем метод POST для каждого из типов задач
        HttpRequest requestPostTask = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create(URL+ "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse <String> response = client.send(requestPostTask, handler);
        assertEquals(200, response.statusCode());
        HttpRequest requestPostEpic = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create(URL+ "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestPostEpic, handler);
        assertEquals(200, response.statusCode());
        HttpRequest requestPostSubtask = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .uri(URI.create(URL+ "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestPostSubtask, handler);
        assertEquals(200, response.statusCode());

        //тестируем метод GET для всех задач и для каждого из типов задач
        URI uri = URI.create(URL);
        HttpRequest requestGetTreeSet = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetTreeSet, handler);
        assertEquals(ALL_TASKS_JSON, response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetTasks = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetTasks, handler);
        assertEquals("["+TASK_JSON+"]", response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetEpics = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetEpics, handler);
        assertEquals("["+EPIC_JSON+"]", response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetSubTasks = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetSubTasks, handler);
        assertEquals("["+SUBTASK_JSON+"]", response.body());
        assertEquals(200, response.statusCode());

        //тестируем get по id
        HttpRequest requestGetTasksNumber = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/task/?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetTasksNumber, handler);
        assertEquals(TASK_JSON, response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetEpicsNumber = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic/?id=2"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetEpicsNumber, handler);
        assertEquals(EPIC_JSON, response.body());
        assertEquals(200, response.statusCode());
        HttpRequest requestGetSubTasksNumber = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/subtask/?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetSubTasksNumber, handler);
        assertEquals(SUBTASK_JSON, response.body());
        assertEquals(200, response.statusCode());

        //тестируем историю
        HttpRequest requestGetHistory = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/history"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestGetHistory, handler);
        assertEquals(HISTORY_JSON, response.body());
        assertEquals(200, response.statusCode());

        //тестируем удаление
        HttpRequest requestDeleteEpicsNumber = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(URL + "/epic/?id=2"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestDeleteEpicsNumber, handler);
        assertEquals(200, response.statusCode());
        HttpRequest requestDeleteTasks = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(URL + "/task"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(requestDeleteTasks, handler);
        assertEquals(200, response.statusCode());

        response = client.send(requestGetHistory, handler);
        assertEquals("[]", response.body());
        assertEquals(200, response.statusCode());
    }

    void createTasks(){
        LocalDateTime now = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(30);
        task = new Task("name", "description", duration, now);
        epic=new Epic("Поход к врачу", "до 15 февраля");
        subtask = new SubTask("Поход к врачу", "до 15 февраля", 2);
    }
}
