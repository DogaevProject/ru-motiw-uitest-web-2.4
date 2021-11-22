package ru.motiw.web.steps.Administration.Directories;

import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.By;
import ru.motiw.web.elements.elementsweb.Administration.TaskTypeListElements;
import ru.motiw.web.steps.Administration.TaskTypeListSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ru.motiw.web.model.URLMenu.DICTIONARY;

/**
 * Справочники
 */
public class DirectoriesSteps extends TaskTypeListSteps {

    private TaskTypeListElements taskTypeListElements = page(TaskTypeListElements.class);

    /**
     * Переход в меню - Администрирование/Справочники
     */
    public static DirectoriesSteps goToURLDirectories() {
        openSectionOnURL(DICTIONARY.getMenuURL());
        return page(DirectoriesSteps.class);
    }

    /**
     * Добавление объекты - Справочники
     *
     * @param nameDirectories передаваемое название для объекта
     */
    @Override
    public void addObjectTaskTypeList(String nameDirectories) {
        super.addObjectTaskTypeList(nameDirectories);
    }

    /**
     * Редактирование - Справочники
     *
     * @param nameDirectories передаваемое название для объекта
     */
    @Override
    public void editObjectTaskTypeList(String nameDirectories) {
        super.editObjectTaskTypeList(nameDirectories);
    }

    /**
     * Удаление - Справочники
     *
     * @param directories передаваемое название для объекта
     */
    @Override
    public void removeObjectTaskTypeList(String directories) {
        super.removeObjectTaskTypeList(directories);
        checkingMessagesConfirmationOfTheObject($(By.xpath("//div[contains(@id,'messagebox')]/ancestor::div[contains(@id,'container')]//div[text()]")),
                "Вы действительно хотите удалить справочник \"" + directories + "" + "\"?",
                taskTypeListElements.getConfirmationButtonObjectDeletion());
        $(By.xpath("//*[contains(text(),'" + directories + "')][ancestor::table]/..")).shouldNotBe(visible);
    }

}
