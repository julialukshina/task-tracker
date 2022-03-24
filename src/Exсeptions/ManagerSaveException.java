package Exсeptions; // создан пакет для исключений

public class ManagerSaveException extends RuntimeException { //предусмотрено свое исключение ManagerSaveException
    public ManagerSaveException(String message) {
        super(message);
    }
}
