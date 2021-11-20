package ru.motiw.web.elements.elementsweb.Tasks.UnionTasks;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

/**
 * Элементы - Форма редактирования - ПАПКА
 */
public class EditFormFoldersElements {

    @FindBy(css = "#tfolder_is_smart-displayEl")
    private SelenideElement checkUseFilter;

    @FindBy(css = "#tfolder_is_shared-displayEl")
    private SelenideElement checkFolderSharedFilter;

    @FindBy(css = "#tfolder_addforall-displayEl")
    private SelenideElement checkFolderAddForAll;

    @FindBy(css = "#tfolder_fornewuser-displayEl")
    private SelenideElement checkAddSharedFolderForNewUsers;

    @FindBy(xpath = "(//*[contains(@id,'btnIconEl') and contains(@id,'button')])[2]/..")
    private SelenideElement saveСhangesInTheCustomFolder;

    @FindBy(xpath = "//input[contains(@id,'nameedit') and @type='text']")
    private SelenideElement folderName;


    /**
     * Использовать фильтр
     */
    public SelenideElement getCheckUseFilter() {
        return checkUseFilter;
    }

    /**
     * Общая папка
     */
    public SelenideElement getCheckFolderSharedFilter() {
        return checkFolderSharedFilter;
    }

    /**
     * Добавить всем
     */
    public SelenideElement getCheckFolderAddForAll() {
        return checkFolderAddForAll;
    }

    /**
     * Добавлять для новых пользователей
     */
    public SelenideElement getCheckAddSharedFolderForNewUsers() {
        return checkAddSharedFolderForNewUsers;
    }

    /**
     * ОК (Подтверждение изменений в форме редактирования папки)
     */
    public SelenideElement getSaveСhangesInTheCustomFolder() {
        return saveСhangesInTheCustomFolder;
    }

    /**
     * Название папки
     */
    public SelenideElement getFolderName() {
        return folderName;
    }

    /**
     * Кнопка закрытия Формы редактирования папки
     */
    public SelenideElement getCloseEditFormOfFolder() {
        return $(By.xpath("//div[@class=\"x-tool-tool-el x-tool-img x-tool-close \"]"));
    }

}
