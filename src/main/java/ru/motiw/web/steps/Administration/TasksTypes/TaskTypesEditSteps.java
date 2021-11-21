package ru.motiw.web.steps.Administration.TasksTypes;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.By;
import ru.motiw.web.elements.elementsweb.Administration.DirectionOfDateBiasingElements;
import ru.motiw.web.elements.elementsweb.Administration.SettingsECPElements;
import ru.motiw.web.elements.elementsweb.Administration.TasksTypes.TaskTypesEditElements;
import ru.motiw.web.model.Administration.TaskTypeListEditObject;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.CorrectionMethod;
import ru.motiw.web.model.OpenFilesForEdit;
import ru.motiw.web.model.ShiftDirection;
import ru.motiw.web.steps.Administration.TaskTypeListEditSteps;

import static com.codeborne.selenide.Selenide.*;
import static ru.motiw.utils.ElementUtil.offsetAndRangeOfValuesOnTheList;
import static ru.motiw.web.model.CorrectionMethod.*;
import static ru.motiw.web.model.ShiftDirection.*;
import static ru.motiw.web.steps.Administration.TasksTypes.TaskTypesSteps.goToURLTaskTypes;

/**
 * Форма редактирования - Типы Задач
 */
public class TaskTypesEditSteps extends TaskTypeListEditSteps {

    private TaskTypesEditElements taskTypesEditElements = page(TaskTypesEditElements.class);
    private SettingsECPElements settingsECPElements = page(SettingsECPElements.class);
    private DirectionOfDateBiasingElements directionOfDateBiasingElements = page(DirectionOfDateBiasingElements.class);


    /**
     * Направление смещения даты при попадании на нерабочее время
     */
    private TaskTypesEditSteps directionOfDisplacementOfTheDate(ShiftDirection shiftdirection) {
        if (shiftdirection == DATE_MOVES_BACK) {
            directionOfDateBiasingElements.getDateMovesBack().click();
        } else if (shiftdirection == DATE_DOES_NOT_MOVE) {
            directionOfDateBiasingElements.getDateDoesNotMove().click();
        } else if (shiftdirection == DATE_MOVES_FORWARD) {
            directionOfDateBiasingElements.getDateMovesForward().click();
        }

        return this;
    }

    /**
     * Использовать ECP
     *
     * @param useSignature передаем параметр использования ЭЦП
     * @return TaskTypesEditSteps
     */
    private TaskTypesEditSteps useEP(boolean useSignature) {
        if (useSignature) {
            settingsECPElements.getUseECP().click();
        }
        return this;
    }

    /**
     * Корректировка даты
     *
     * @param dateCorrection передаваемый период корректировки даты для Типа задач
     * @return TaskTypesEditSteps
     */
    private TaskTypesEditSteps dateCorrection(CorrectionMethod dateCorrection) {
        switch (dateCorrection) {
            case SET_TIME:
                offsetAndRangeOfValuesOnTheList(taskTypesEditElements.getTypeCorrection(), getCollectionOfListItems(),
                        SET_TIME.getNameOfTheEnumerationValues());
                break;
            case OFFSET_IN_THE_INTERVAL:
                offsetAndRangeOfValuesOnTheList(taskTypesEditElements.getTypeCorrection(), getCollectionOfListItems(),
                        OFFSET_IN_THE_INTERVAL.getNameOfTheEnumerationValues());
                break;
            case OFFSET_IN_THE_PERIOD: {
                offsetAndRangeOfValuesOnTheList(taskTypesEditElements.getTypeCorrection(), getCollectionOfListItems(),
                        OFFSET_IN_THE_PERIOD.getNameOfTheEnumerationValues());
                break;
            }
        }
        return this;
    }

    /**
     * Запретить изменение типа для созданной задачи
     */
    private TaskTypesEditSteps isTaskTypeChangeDisabled(boolean taskTypeChangeDisabled) {
        if (taskTypeChangeDisabled) {
            taskTypesEditElements.getTypeChangeDisabled().click();
        }
        return this;
    }

    /**
     * Создавать подзадачи ИРГ только родительского типа
     */
    private TaskTypesEditSteps onlyTheSameTypeIWG(boolean onlyTheSameTypeIWG) {
        if (onlyTheSameTypeIWG) {
            taskTypesEditElements.getSameTypeIWG().click();
        }
        return this;
    }

    /**
     * Запретить закрытие задач с неготовыми контрольными точками
     */
    private TaskTypesEditSteps isCloseTaskWithNotReadyCheckpointsDisabled(boolean notReadyCheckpointsDisabled) {
        if (notReadyCheckpointsDisabled) {
            taskTypesEditElements.getClosingTasksWithNotReadyCheckpoints().click();
        }
        return this;
    }

    /**
     * Открывать файлы для редактирования
     *
     * @param filesForEdit передаваемое зн-ие истенности выбора открытия файлов для редактирования
     * @return TaskTypesEditSteps
     */
    private TaskTypesEditSteps openFilesForEdit(OpenFilesForEdit filesForEdit) {
        if (filesForEdit == null) return this;
        switch (filesForEdit) {
            case VALUE_IS_NOT_DEFINED:
                offsetAndRangeOfValuesOnTheList(taskTypesEditElements.getFilesEdit(), getCollectionOfListItems(),
                        OpenFilesForEdit.VALUE_IS_NOT_DEFINED.getNameOfTheEnumerationValues());
                break;
            case NO:
                offsetAndRangeOfValuesOnTheList(taskTypesEditElements.getFilesEdit(), getCollectionOfListItems(),
                        OpenFilesForEdit.NO.getNameOfTheEnumerationValues());
                break;
            case YES:
                offsetAndRangeOfValuesOnTheList(taskTypesEditElements.getFilesEdit(), getCollectionOfListItems(),
                        OpenFilesForEdit.YES.getNameOfTheEnumerationValues());
                break;
        }
        return this;
    }

    /**
     * Attach files to tape performance goals and description of the problem
     * Actions OR Description
     *
     * @param value the condition is true
     */
    private TaskTypesEditSteps attachFilesAndDescriptionOfTheAction(boolean value, SelenideElement actionORDescription) {
        if (value) {
            actionORDescription.click();
        }
        return this;
    }

    /**
     * Направление смещения при попадании на нерабочее время
     *
     * @param displacementOfTheDate передаваемое значение смещения даты
     * @return TaskTypesEditSteps
     */
    public TaskTypesEditSteps setDirectionOfDisplacementOfTheDate(TasksTypes displacementOfTheDate) {
        taskTypesEditElements.getSettingsTab().click();
        directionOfDisplacementOfTheDate(displacementOfTheDate.getTaskTypeShiftDirection());
        return this;
    }

    /**
     * Использовать ЭЦП
     *
     * @param useEP передаваемое значение ЭП
     * @return TaskTypesEditSteps
     */
    public TaskTypesEditSteps setUseEP(TasksTypes useEP) {
        taskTypesEditElements.getSettingsTab().click();
        useEP(useEP.getUseECP());
        return this;
    }

    /**
     * Корректировка даты
     *
     * @param dateC передаваемое значение Корректировки даты
     * @return TaskTypesEditSteps
     */
    public TaskTypesEditSteps setDateCorrection(TasksTypes dateC) {
        taskTypesEditElements.getSettingsTab().click();
        dateCorrection(dateC.getTaskTypeCorrectionMethod());
        return this;
    }

    /**
     * Запретить изменение типа для созданной задачи
     *
     * @param taskTypeChangeDisabled передаваемое значение настройки
     * @return TaskTypesEditSteps
     */
    public TaskTypesEditSteps setIsTaskTypeChangeDisabled(TasksTypes taskTypeChangeDisabled) {
        taskTypesEditElements.getSettingsTab().click();
        isTaskTypeChangeDisabled(taskTypeChangeDisabled.getIsTaskTypeChangeDisabled());
        return this;
    }

    /**
     * Создавать подзадачи ИРГ только родительского типа
     *
     * @param value передаваемое значение настройки
     * @return TaskTypesEditSteps
     */
    public TaskTypesEditSteps setOnlyTheSameTypeIWG(TasksTypes value) {
        taskTypesEditElements.getSettingsTab().click();
        onlyTheSameTypeIWG(value.getOnlyTheSameTypeIWG());
        return this;
    }

    /**
     * Запретить закрытие задач с неготовыми контрольными точками
     *
     * @param valueSetting передаваемое значение настройки
     * @return TaskTypesEditSteps
     */
    public TaskTypesEditSteps setIsCloseTaskWithNotReadyCheckpointsDisabled(TasksTypes valueSetting) {
        taskTypesEditElements.getSettingsTab().click();
        isCloseTaskWithNotReadyCheckpointsDisabled(valueSetting.getIsCloseTaskWithNotReadyCheckpointsDisabled());
        return this;
    }

    /**
     * Открывать файлы для редактирования
     *
     * @param valueSetting передаваемое значение настройки
     * @return TaskTypesEditSteps
     */
    public TaskTypesEditSteps setOpenFilesForEdit(TasksTypes valueSetting) {
        taskTypesEditElements.getSettingsTab().click();
        openFilesForEdit(valueSetting.getOpenFilesForEdit());
        return this;
    }

    /**
     * Прикреплять файлы:
     * Лента действий
     *
     * @param valueAttachFiles передаваемое значение настройки
     * @return TaskTypesEditSteps
     */
    public TaskTypesEditSteps setAttachFilesAndDescriptionOfTheAction(TasksTypes valueAttachFiles) {
        taskTypesEditElements.getSettingsTab().click();
        attachFilesAndDescriptionOfTheAction(valueAttachFiles.getIsAttachFilesToActionLine(),
                taskTypesEditElements.getAttachFilesActionLine());
        attachFilesAndDescriptionOfTheAction(valueAttachFiles.getIsAttachFilesToDescription(),
                taskTypesEditElements.getAttachFilesDescription()); // Добавлять файлы в описание
        return this;
    }

    /**
     * Добавление типов полей
     *
     * @param tasksTypes атрибуты полей объекта
     */
    @Override
    public TaskTypeListEditSteps addAllFieldsTaskTypeList(TaskTypeListEditObject tasksTypes) {
        verifyThatTaskTypeOpen(tasksTypes);
        taskTypesEditElements.getFieldsTab().click(); // Вкладка - Поля
        super.addAllFieldsTaskTypeList(tasksTypes); // Добавление полей
        return this;
    }

    /**
     * Проверка что форма добавления/редактирования Типа задачи открылась
     */
    private void verifyThatTaskTypeOpen(TaskTypeListEditObject tasksTypes) {
        try {
            $(By.xpath("//a/ancestor::div[contains(@id,'tabbar')]//a")).waitUntil(Condition.visible, 10000);
        } catch (ElementNotFound e) {
            // Обработка случая зависания ответа сервера после сохранения, пробуем открыть форму Типа задачи повторно
            goToURLTaskTypes().editObjectTaskTypeList(tasksTypes.getObjectTypeName());
        }
        checkDisplayedTabsInTheShapeOfAnObject(By.xpath("//a/ancestor::div[contains(@id,'tabbar')]//a"), 8,
                By.xpath("//a/ancestor::div[contains(@id,'tabbar')]//a//span[text()]"), new String[]{"Настройки", "Поля", "Обработчики", "Настройки почтовых уведомлений",
                        "Дополнительно", "Доступ", "Форматы", "Шаблоны отображения"});
    }


}