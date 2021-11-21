package ru.motiw.web.steps.Administration;

import org.openqa.selenium.By;
import ru.motiw.web.elements.elementsweb.Administration.TaskTypeListElements;
import ru.motiw.web.steps.BaseSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.sleep;


/**
 * Основной грид разделов - Типы Таблиц/Типы задач/Справочники, раздела Администрирование
 */
public abstract class TaskTypeListSteps extends BaseSteps {

    private TaskTypeListElements taskTypeListElements = page(TaskTypeListElements.class);

    /**
     * Добавить объект
     *
     * @param nameObject передаваемое имя объекта
     */
    public void addObjectTaskTypeList(String nameObject) {
        sleep(2000);
        taskTypeListElements.getAddTypesObject().click();
        $(taskTypeListElements.getNameObject()).shouldBe(visible).setValue(nameObject);
        taskTypeListElements.getOkAddObject().click();
    }

    /**
     * Редактировать объект
     *
     * @param nameObject передаваемое имя объекта
     */
    public void editObjectTaskTypeList(String nameObject) {
        clickTheObjectInGrid(nameObject);
        taskTypeListElements.getEditTypesObject().click();
    }

    /**
     * Удалить объект
     *
     * @param nameObject передаваемое имя объекта
     */
    public void removeObjectTaskTypeList(String nameObject) {
        $(By.xpath("//*[contains(text(),'" + nameObject + "')][ancestor::table]")).shouldBe(visible);
        $(By.xpath("//*[contains(text(),'" + nameObject + "')][ancestor::table]/../span")).click();
        taskTypeListElements.getRemoveTypesObject().click();
    }


}
