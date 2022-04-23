package Servers;

import Managers.*;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer { // класс HttpTask-сервера
    private final int PORT = 8080;
    private HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

    {
        httpServer.createContext("/tasks", new TaskHandler());
    }

    public static TaskManager manager = Managers.getDefaultHttpTask();
    public static Gson gson = new GsonBuilder()
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


    public HttpTaskServer() throws IOException {

    }

    public void startServer(){
        httpServer.start();
    } //метод для запуска HttpTask-сервера

    public void stopServer(){
        httpServer.stop(5);
    } //метод для остановки HttpTask-сервера

    static class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            String[] pathElements = path.split("/");
            int taskId = -1;
            String response = "";
            if (query != null) {
                taskId = Integer.parseInt(query.split("=")[1]);
            }
            switch (method) {
                case "GET":
                    if (taskId == -1) { //возвращаем
                        if (pathElements.length == 2) {
                            response = gson.toJson(manager.getTreeSet()); //возвращаем список приоритезированных задач
                        } else {
                            switch (pathElements[2]) {
                                case "history": //возвращаем историю
                                    response = gson.toJson(manager.history());
                                    break;
                                case "task": //возвращаем все таски
                                    response = gson.toJson(manager.getListOfTasks());
                                    break;
                                case "subtask": //возвращаем все сабтаски
                                    response = gson.toJson(manager.getListOfSubTasks());
                                    break;
                                case "epic": //возвращаем все эпики
                                    response = gson.toJson(manager.getListOfEpics());
                                    break;
                                default:
                                    exchange.sendResponseHeaders(400, 0);
                                    break;
                            }
                        }
                    } else {
                        if (pathElements.length == 4 && pathElements[2].equals("subtask") && pathElements[3].equals("epic")) {
                            //возвращаем сабтаски по id эпика
                            response = gson.toJson(manager.getListOfSubtasksOfEpics(taskId));
                        } else if (pathElements.length == 3 && pathElements[1].equals("tasks")) {
                            //возвращаем задачу по id
                            response = gson.toJson(manager.getTaskByID(taskId));
                        } else {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    if (pathElements.length == 2) {
                        exchange.sendResponseHeaders(400, 0);
                        break;
                    } else {
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        if (taskId == -1) {
                            //записываем новую задачу
                            manager.putTask(taskDeserialization(body));
                        } else {
                            if (taskId == taskDeserialization(body).getId()) {
                                //обновляем имеющуюся задачу
                                manager.updateTask(taskDeserialization(body), taskId);
                            } else {
                                throw new IllegalArgumentException("Id в параметре и теле запроса не совпадают");
                            }
                        }
                    }
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case "DELETE":
                    if (pathElements.length == 2) {
                        exchange.sendResponseHeaders(400, 0);
                        break;
                    } else {
                        if (taskId == -1) {
                            switch (pathElements[2]) {
                                case "task": //удаляем все таски
                                    manager.deleteAllTasks();
                                    break;
                                case "subtask": //удаляем все сабтаски
                                    manager.deleteAllSubtasks();
                                    break;
                                case "epic": //удаляем все эпики
                                    manager.deleteAllEpics();
                                    break;
                                default:
                                    exchange.sendResponseHeaders(400, 0);
                                    break;
                            }
                        } else {
                            //удаляем задачу по id
                                manager.deleteTaskById(taskId);
                        }
                    }
                    exchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    exchange.sendResponseHeaders(400, 0);
            }
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());

            }
        }

        private Task taskDeserialization(String body) { // метод для десериализации задач
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) {
                throw new IllegalArgumentException("Тело запроса не является объектом");
            }
            if (body.contains("subTasksOfEpic")){
                return gson.fromJson(body, Epic.class);
            }else if (body.contains("epic")){
                return gson.fromJson(body, SubTask.class);
            }else{
                return gson.fromJson(body, Task.class);
            }
        }
    }
}


