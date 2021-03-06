package ru.motiw.web.model.Document;

import ru.motiw.mobile.model.Document.TypeOperationsOfDocument;
import ru.motiw.web.model.Administration.Users.Employee;

/**
 * Работа с карточкой документа
 * Выполняемые операции под разными пользователями на разных этамах МС (Для операций задается различный порядок выполнения)
 */
public class ExecutionOfDocument {

    private Integer executionNumber;
    private Employee executiveUser;
    private TypeOperationsOfDocument typeExecutionOperation;


    /**
     * Порядковый номер выполнения операции
     *
     * @return
     */
    public Integer getExecutionNumber() {
        return executionNumber;
    }

    /**
     * Пользователь выполняющий действия
     *
     * @return
     */
    public Employee getExecutiveUser() {
        return executiveUser;
    }

    /**
     * Тип выполняемой операции
     *
     * @return
     */
    public TypeOperationsOfDocument getTypeExecutionOperation() {
        return typeExecutionOperation;
    }

    /**
     * Установка параметров выполнения операции
     *
     * @param executionNumber   Порядковый номер выполнения операции
     * @param executiveUser Пользователь выполняющий действия
     * @param typeExecutionOperation Тип выполняемой операции
     * @return
     */
    public ExecutionOfDocument (Integer executionNumber, Employee executiveUser, TypeOperationsOfDocument typeExecutionOperation) {
        this.executionNumber = executionNumber;
        this.executiveUser = executiveUser;
        this.typeExecutionOperation = typeExecutionOperation;

    }
}



