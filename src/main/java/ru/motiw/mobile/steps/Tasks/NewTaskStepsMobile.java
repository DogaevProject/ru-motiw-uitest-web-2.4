package ru.motiw.mobile.steps.Tasks;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.motiw.mobile.elements.Internal.InternalElementsMobile;
import ru.motiw.mobile.elements.Login.LoginPageElementsMobile;
import ru.motiw.mobile.steps.InternalStepsMobile;
import ru.motiw.mobile.steps.Tasks.ValidationStepsTasks.*;
import ru.motiw.mobile.steps.ValidationStepsMobile.ValidateFilesStepsMobile;
import ru.motiw.web.model.Administration.Directories.DirectoriesField;
import ru.motiw.web.model.Administration.FieldsObject.*;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.Tasks.Task;

import java.io.File;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertTrue;
import static ru.motiw.mobile.model.Task.InnerGroupTabs.*;

/*
 Страница - Создать задачу
 */

public class NewTaskStepsMobile extends CardTaskStepsMobile {

    private InternalStepsMobile internalStepsMobile = page(InternalStepsMobile.class);
    private LoginPageElementsMobile loginPageElementsMobile = page(LoginPageElementsMobile.class);
    InternalElementsMobile internalElementsMobile = page(InternalElementsMobile.class);
    ValidateValuesOfFieldsStepsMobile validateValuesOfFieldsStepsMobile = page(ValidateValuesOfFieldsStepsMobile.class);
    ValidateFilesStepsMobile validateFilesStepsMobile = page(ValidateFilesStepsMobile.class);
    ValidateActionsStepsMobile validateActionsStepsMobile = page(ValidateActionsStepsMobile.class);
    ValidateGroupFieldsStepsMobile validateGroupFieldsStepsMobile = page(ValidateGroupFieldsStepsMobile.class);


    /**
     * Переходим в форму - Создать задачу
     *
     * @return
     */
    public NewTaskStepsMobile goToCreateNewTask() {
        //Если Меню не открыто - открываем его перед тем, как перейти в форму создания задачи
        if ($(internalElementsMobile.getCreateTaskMobile()).is(Condition.disappear)) {
            internalStepsMobile.goToInternalMenu();
            internalElementsMobile.getCreateTaskMobile().click();
        } else {
            internalElementsMobile.getCreateTaskMobile().click();
        }
        return page(NewTaskStepsMobile.class);
    }

    /**
     * Проверка Загрузки страницы - форма Создания задачи - проверка отображения и кол-ва элементов (все блоки компонетов - поля задачи) в форме задачи
     */
    private NewTaskStepsMobile ensurePageLoaded() {
        newTaskFormElementsMobile.getCollectionElementsFormOfTask().shouldHave(CollectionCondition.size(9), 5000);
        newTaskFormElementsMobile.getButtonCreateTask().shouldBe(visible);
        return this;
    }


    /**
     * Название задачи
     *
     * @param nameTasks name task for input
     * @return page NewTaskPag
     */
    NewTaskStepsMobile setTaskName(String nameTasks) {
        newTaskFormElementsMobile.getTaskName().click();
        newTaskFormElementsMobile.getTaskName().setValue(nameTasks);
        return this;
    }

    /**
     * Описание задачи
     *
     * @param descriptionTasks description task for input
     * @return page NewTaskPag
     */
    NewTaskStepsMobile setTasksDescription(String descriptionTasks) {
        if (descriptionTasks == null) {
            return this;
        } else {
            newTaskFormElementsMobile.getDescriptionTask().click();
            newTaskFormElementsMobile.getDescriptionTask().clear();
            newTaskFormElementsMobile.getDescriptionTask().setValue(descriptionTasks);
        }
        return this;
    }

    /**
     * Ввод даты начала
     */
    NewTaskStepsMobile setDateBegin(String begin) {
        if (begin == null) {
            return this;
        } else {
            newTaskFormElementsMobile.getBeginField().click();
            newTaskFormElementsMobile.getBeginField().setValue(begin);
        }
        return this;
    }

    /**
     * Ввод даты окончания задачи
     */
    NewTaskStepsMobile setDateEnd(String end) {
        if (end == null) {
            return this;
        } else {
            newTaskFormElementsMobile.getEndField().click();
            newTaskFormElementsMobile.getEndField().setValue(end);

        }
        return this;
    }

    /**
     * Выбор булевой настройки в форме задачи
     * <p>
     * Например, признак "Важная задача", "Секретная задача", "С докладом"
     *
     * @param stateOfCheckbox состояние установленной настройки
     * @param inputCheckbox   совершить действие (снять/установить настройку)
     */

    private void rangeOfValuesFromTheCheckbox(boolean stateOfCheckbox, SelenideElement inputCheckbox) {
        if (stateOfCheckbox) {
            inputCheckbox.click();
        } else if ($(inputCheckbox).is(selected)) {
            inputCheckbox.click();
        }
    }


    /**
     * Выбор булевой настройки в форме задачи в зависимости от состояния чекбокса.
     * Например, признак "Важная задача", "Секретная задача", "С докладом"
     *
     * @param stateOfCheckbox      состояние установленной настройки
     * @param inputCheckbox        совершить действие (снять/установить настройку)
     * @param stateInputOfCheckbox элемент для проверки состояния чекбокса (снят/установлен)
     */

    void rangeOfValuesFromTheCheckbox(boolean stateOfCheckbox, SelenideElement inputCheckbox, SelenideElement stateInputOfCheckbox) {
        if (stateOfCheckbox) {
            inputCheckbox.click();
        } else if ($(stateInputOfCheckbox).is(selected)) //если stateOfCheckbox не true, а чекбокс уже установлен, то снимаем.
        {
            inputCheckbox.click();
        }
    }


    /**
     * Установка признака важности
     */
    private NewTaskStepsMobile setImportance(boolean isImportant) {
        newTaskFormElementsMobile.getPriority().click();

        if (isImportant) {
            newTaskFormElementsMobile.getImportantTask().click();
        } else {
            newTaskFormElementsMobile.getSimpleTask().click();
        }
        return this;
    }


    /**
     * Выбор значений из выпадающего списка типа задачи
     *
     * @param taskType передаваемое имя типа задачи
     */
    private NewTaskStepsMobile setTaskType(TasksTypes taskType) {
        if (taskType == null) {
            return this;
        } else {
            $(newTaskFormElementsMobile.getTriggerInUserField("Тип задачи")).shouldBe(Condition.visible);
        }
        newTaskFormElementsMobile.getTriggerInUserField("Тип задачи").click();
        newTaskFormElementsMobile.getValueInTheListOfUserField(taskType.getObjectTypeName()).click();
        return this;

    }

    /**
     * Общий метод заполнения пользовательских полей типа Строка, Целое, Вещественное, Дата, Нумератор задачи
     *
     * @param nameField имя поля документа для заполнения
     * @param valueLine передаваемое значение для заполнения
     */
    private NewTaskStepsMobile enterValueInFieldInput(String nameField, String valueLine) {
        if (valueLine == null) {
            return this;
        } else {
            newTaskFormElementsMobile.getInputInUserField(nameField).click(); // находим поле по Названию
            newTaskFormElementsMobile.getInputInUserField(nameField).setValue(valueLine);
        }
        return this;
    }


    /**
     * Общий метод заполнения пользовательских полей типа Текст
     *
     * @param nameField имя поля документа для заполнения
     * @param valueLine передаваемое значение для заполнения
     */
    private NewTaskStepsMobile enterValueInFieldText(String nameField, String valueLine) {
        if (valueLine == null) {
            return this;
        } else {
            newTaskFormElementsMobile.getTextAreaInCustomField(nameField).click(); // находим поле по Названию
            newTaskFormElementsMobile.getTextAreaInCustomField(nameField).setValue(valueLine);
        }
        return this;
    }


    /**
     * Общий метод заполнения пользовательских строковых полей с выбором из списка
     *
     * @param nameField имя поля документа для заполнения
     * @param valueLine передаваемое значение для заполнения
     */
    private NewTaskStepsMobile choiceValueInFieldWithListDirectory(String nameField, String valueLine) {
        if (valueLine == null) {
            return this;
        } else {
            $(newTaskFormElementsMobile.getTriggerInUserField(nameField)).shouldBe(Condition.visible);
        }
        newTaskFormElementsMobile.getTriggerInUserField(nameField).click();
        newTaskFormElementsMobile.getValueInTheListOfUserField(valueLine).click();
        return this;
    }


    /**
     * Общий метод заполнения пользовательских полей типа "Справочник" с выбором из списка
     *
     * @param nameField         имя поля документа для заполнения
     * @param directoriesFields массив полей объекта, который содержит передаваемое значение для заполнения
     */
    private NewTaskStepsMobile choiceValueInFieldWithListDirectory(String nameField, DirectoriesField[] directoriesFields) {
        if (directoriesFields == null) {
            return this;
        } else {
            $(newTaskFormElementsMobile.getTriggerInUserField(nameField)).shouldBe(Condition.visible);
        }
        for (DirectoriesField valueLine : directoriesFields) {
            newTaskFormElementsMobile.getTriggerInUserField(nameField).click();
            newTaskFormElementsMobile.getValueInTheListOfUserField(valueLine.getDirectoriesItem()).click();
        }
        return this;
    }


    /**
     * Удаления значений в пользовательских полях
     * требуется при редактировании ранее созданной задачи
     */
    NewTaskStepsMobile removeValueInCustomFields(FieldObject[] fieldObjects) {

        if (fieldObjects == null) {
            return this;
        }
        for (FieldObject fieldObject : fieldObjects) {
            // СОТРУДНИК
            if (fieldObject.getFieldType() instanceof TypeListFieldsEmployee) {
                //------------- Проверка удаления ранее выбранных пользователей
                choiceUserOnTheRole(fieldObject.getValueEmployeeField(), newTaskFormElementsMobile.getInputInUserFieldTypeEmployee(fieldObject.getFieldName())); // удаляем пользователя выбранного при создании задачи
            }
        }
        return this;
    }


    /**
     * Заполнение пользовательских полей
     */
    NewTaskStepsMobile setValueInCustomFields(FieldObject[] fieldObjects) {

        if (fieldObjects == null) {
            return this;
        }
        for (FieldObject fieldObject : fieldObjects) {

            // СТРОКА
            if (fieldObject.getFieldType() instanceof TypeListFieldsString) {
                enterValueInFieldInput(fieldObject.getFieldName(), fieldObject.getValueField());
            }

            // ТЕКСТ
            if (fieldObject.getFieldType() instanceof TypeListFieldsText) {
                enterValueInFieldText(fieldObject.getFieldName(), fieldObject.getValueField());
            }

            // Целое
            if (fieldObject.getFieldType() instanceof TypeListFieldsInteger) {
                enterValueInFieldInput(fieldObject.getFieldName(), fieldObject.getValueField());
            }

            // ВЕЩЕСТВЕННОЕ
            if (fieldObject.getFieldType() instanceof TypeListFieldsDouble) {
                enterValueInFieldInput(fieldObject.getFieldName(), fieldObject.getValueField());
            }

            // ДАТА
            if (fieldObject.getFieldType() instanceof TypeListFieldsDate) {
                enterValueInFieldInput(fieldObject.getFieldName(), fieldObject.getValueField());
            }

            // ЛОГИЧЕСКИЙ
            if (fieldObject.getFieldType() instanceof TypeListFieldsBoolean) {
                rangeOfValuesFromTheCheckbox(fieldObject.getValueBooleanField(), newTaskFormElementsMobile.getCheckboxInUserField(fieldObject.getFieldName()));
            }

            // НУМЕРАТОР
            if (fieldObject.getFieldType() instanceof TypeListFieldsNumerator) {
                enterValueInFieldInput(fieldObject.getFieldName(), fieldObject.getValueField());
            }

            // ПОДРАЗДЕЛЕНИЕ
            if (fieldObject.getFieldType() instanceof TypeListFieldsDepartment) {
                choiceValueInFieldWithListDirectory(fieldObject.getFieldName(), fieldObject.getValueField());
            }

            // СОТРУДНИК
            if (fieldObject.getFieldType() instanceof TypeListFieldsEmployee) {
                choiceUserOnTheRole(fieldObject.getValueEmployeeField(), newTaskFormElementsMobile.getInputInUserFieldTypeEmployee(fieldObject.getFieldName()));
            }

            //СПРАВОЧНИК
            if (fieldObject.getFieldType() instanceof TypeListFieldsDirectory) {
                choiceValueInFieldWithListDirectory(fieldObject.getFieldName(), fieldObject.getValueDirectoriesField());
            }

            // МН.СПРАВОЧНИК
            if (fieldObject.getFieldType() instanceof TypeListFieldsMultiDirectory) {
                choiceValueInFieldWithListDirectory(fieldObject.getFieldName(), fieldObject.getValueDirectoriesField());
            }
        }
        return this;
    }


    /**
     * Создание задачи
     *
     * @param task передаваемые атрибуты задачи
     */
    public NewTaskStepsMobile creatingTask(Task task) {
        ensurePageLoaded();
        //--------------------------------- группа полей  "Название"
        // Разворачиваем  группу полей "Название"
        selectGroupTab(NAME);
        //Заполняем Название задачи
        setTaskName(task.getTaskName())
                .setTasksDescription(task.getDescription());
        // Закрываем  группу полей  "Название"
        selectGroupTab(NAME);
        //--------------------------------- группа полей  "Кому"
        // Открываем  группу полей "Кому"
        selectGroupTab(TO_WHOM);
        // выбор пользователя по ФИО - через searchlive
        validateValuesOfFieldsStepsMobile.currentUserSelectedInTheRole(task.getAuthorDefault(), newTaskFormElementsMobile.getAuthorsField()); // - по умолчанию Автор задачи текущий пользователь (admin)
        choiceUserOnTheRole(task.getAuthors(), newTaskFormElementsMobile.getAuthorsField()); // вводим - Авторы задачи
        choiceUserOnTheRole(task.getControllers(), newTaskFormElementsMobile.getСontrollersField()); // вводим - Контролеры задачи
        choiceUserOnTheRole(task.getExecutiveManagers(), newTaskFormElementsMobile.getResponsiblesField()); // вводим - Ответственные руководители
        choiceUserOnTheRole(task.getWorkers(), newTaskFormElementsMobile.getWorkersField()); // вводим - Исполнители задачи
        // Закрываем  группу полей  "Кому"
        selectGroupTab(TO_WHOM);

        //--------------------------------- группа полей "Срок"
        // Открываем  группу полей "Срок"
        selectGroupTab(DATE);
        //Заполняем Даты
        setDateBegin(task.getDateBegin())
                .setDateEnd(task.getDateEnd());
        setImportance(task.getIsImportant()); // "Приоритет" - выбираем - Важная задача
        // Закрываем  группу полей  "Срок"
        selectGroupTab(DATE);

        //--------------------------------- группа полей "Тип задачи"
        $(By.xpath("//div[contains(@id,\"object\")]//input[@name='id_tasktype']")).shouldNotBe(empty);//Проверка поля названия при закрытой группы полей "Название" - проверяет, что поле не пустое, т.к должно быть значение по умолчанию.
        // Открываем  группу полей  "Тип задачи"
        selectGroupTab(TYPE_TASK);
        $(By.xpath("//div[contains(@id,\"object\")]//input[@name='id_tasktype']")).shouldNotBe(empty);//Проверка поля названия при открытой группы полей "Название" - проверяет, что поле не пустое, т.к должно быть значение по умолчанию.
        setTaskType(task.getTaskType()); //Тип задачи
        // если у пользователя была создана задача только с типом "Обычный"(по умолчанию), то баг в АРМе - у него нет поля Тип задачи.
        // Поэтому все что связано с этим полем  можно заккоментировать. или найти решение как обходить эту проблему.

        // Заполнение пользовательских полей
        setValueInCustomFields(task.getTaskFields());
        // Закрываем  группу полей  "Тип задачи"
        selectGroupTab(TYPE_TASK);

        //--------------------------------- группа полей "Файлы"
        // Открываем группу полей "Файлы"
        selectGroupTab(FILES);
        addAttachFiles(task.getFileName());
        // Закрываем  группу полей "Файлы"
        selectGroupTab(FILES);

        //--------------------------------- группа полей "Ещё"
        // Открываем группу полей "Ещё"
        selectGroupTab(MORE);
        newTaskFormElementsMobile.getReportRequired().shouldBe(selected); // Признак - С Докладом всегда по умолчанию должен быть выбран.
        rangeOfValuesFromTheCheckbox(task.getIsSecret(), newTaskFormElementsMobile.getCheckboxIsSecret()); // признак - Секретная
        rangeOfValuesFromTheCheckbox(task.getIsForReview(), newTaskFormElementsMobile.getCheckboxIsForReview()); // Признак - "Для ознакомления"
        // Закрываем  группу полей "Ещё"
        selectGroupTab(MORE);
        return this;
    }

    /**
     * Сохранить задачу
     * Ждем появление маски загрузки
     * Ждем пока исчезнит маска загрузки
     */
    public NewTaskStepsMobile saveTask() {
        newTaskFormElementsMobile.getButtonCreateTask().click();
        //Ждем появление маски загрузки
        loginPageElementsMobile.getMaskOfLoading().waitUntil(Condition.visible, 1000);
        //Ждем пока исчезнит маска загрузки
        loginPageElementsMobile.getMaskOfLoading().waitUntil(Condition.disappear, 10000);
        // Иногда после первого нажатия на операцию сохранения происходит "ошибка при создании задачи" - пробуем выполнить операцию еще раз
        try {
            verifyThatToastOfNewTaskAppear();
        } catch (Error e) {
            newTaskFormElementsMobile.getButtonCreateTask().click();
            verifyThatToastOfNewTaskAppear();
        }
        return page(NewTaskStepsMobile.class);

    }


    /**
     * Переходим по ссылке в появившемся toast в созданную задачу
     */
    public NewTaskStepsMobile goToNewTaskViaToast() {
        verifyThatToastOfNewTaskAppear();
        //Переходим по ссылке в появившемся toast в созданную задачу
        loginPageElementsMobile.getTextOnToastOfNewTask().click();
        return page(NewTaskStepsMobile.class);
    }

    /**
     * Проверяем появление toast "Создана задача"
     */
    public NewTaskStepsMobile verifyThatToastOfNewTaskAppear() {
        loginPageElementsMobile.getToastOfNewTask().waitUntil(Condition.visible, 10000);
        loginPageElementsMobile.getTextOnToastOfNewTask().shouldHave(Condition.text("Создана задача №"));
        return page(NewTaskStepsMobile.class);
    }


    /**
     * Аттачминг файлов в форме задачи
     *
     * @param nameOfFiles названия файлов
     */
    NewTaskStepsMobile addAttachFiles(String[] nameOfFiles) {
        if (nameOfFiles != null)
            for (String nameOfFile : nameOfFiles) {
                String mainFilePath = "src" + File.separator + "main" + File.separator +
                        "resources" + File.separator + "attachfiles" + File.separator;
                File file = newTaskFormElementsMobile.getInputFiles()
                        .uploadFile(new File(mainFilePath, nameOfFile));
                assertTrue(file.exists()); // наверное, проверяет, что создан объект File.
                assertTrue(file.getPath().replace(File.separatorChar, '/').endsWith("src/main/resources/attachfiles/" + nameOfFile + ""));
                assertTrue(validateFilesStepsMobile.verifyNameFileInTheListFiles(nameOfFile), "Не пройдена проверка названий Файлов в списке прикрепленных файлов");
            }
        sleep(2000);
        return this;
    }

    /**
     * Проверки групп полей
     *
     * @return ValidateGroupFieldsStepsMobile
     */
    public ValidateGroupFieldsStepsMobile validateThatInGroupFields() {
        return page(ValidateGroupFieldsStepsMobile.class);
    }

}