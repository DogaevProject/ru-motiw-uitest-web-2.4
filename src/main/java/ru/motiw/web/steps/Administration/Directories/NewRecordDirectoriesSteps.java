package ru.motiw.web.steps.Administration.Directories;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.motiw.web.elements.elementsweb.Administration.Directories.EditDirectoriesElements;
import ru.motiw.web.model.Administration.Directories.Directories;
import ru.motiw.web.model.Administration.Directories.DirectoriesField;
import ru.motiw.web.model.Administration.FieldsObject.TypeListFieldsString;
import ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields;
import ru.motiw.web.steps.BaseSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.testng.FileAssert.fail;
import static ru.motiw.utils.WindowsUtil.newWindowForm;
import static ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields.NO;

/**
 * форма - Добавления записи справочника
 */

public class NewRecordDirectoriesSteps extends BaseSteps {

    private EditDirectoriesElements editDirectoriesElements = page(EditDirectoriesElements.class);

    /**
     * Создание новой записи
     *
     * @return вощвращаем стр. для дальнейшего взаимодействия
     * с елементами на странице
     */
    public NewRecordDirectoriesSteps addNewRecord(Directories directories) {

        String parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся

        //Переход по ссылке в созданный справочник
        $(By.xpath("//table//a[contains(text(),'" + directories.getObjectTypeName() + "')]")).scrollTo();
        $(By.xpath("//table//a[contains(text(),'" + directories.getObjectTypeName() + "')]")).click();
        getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//*[contains(text(),'Всего')]"))));

        //Добавление записи справочника
        addFields(directories.getDirectoriesFields());

        getWebDriver().close(); // Закрываем окно страницы
        getWebDriver().switchTo().window(parentWindowHandler); // Возращаемся в окно страницы 
        return page(NewRecordDirectoriesSteps.class);
    }


    /**
     * Выбор типа поля объекта
     *
     * @param typeOfField передаваемое значение типа поля
     */
    private void selectTheTypeOfField(SelenideElement typeOfField) {
        typeOfField.click();
    }

    /**
     * Заполнение полей при добавлении записи справочника
     *
     * @param directoriesFields атрибуты объекта - запись справочника
     */
    private void addFields(DirectoriesField[] directoriesFields) {
        if (directoriesFields == null) {
            return;
        }
        for (DirectoriesField directoriesItem : directoriesFields) {
            // СТРОКА
            if (directoriesItem.getFieldType() instanceof TypeListFieldsString) {
                try {
                    editDirectoriesElements.getAddRecordButton().click(); // Добавить запись
                } catch (Error ignored) {
                    if (editDirectoriesElements.getAddRecordButton().isDisplayed()) {
                        editDirectoriesElements.getAddRecordButton().click(); // Добавить запись
                    } else fail();
                }
                getFrameObject($(By.xpath("//iframe[contains(@src,'/user/dict_record_edit/')]")));
                sleep(2000);
                selectTheTypeOfField(editDirectoriesElements.getTypeFieldString(directoriesItem.getFieldName()));
                sleep(500);
                TypeListFieldsString fieldString = (TypeListFieldsString) directoriesItem.getFieldType();
                if (fieldString.getIsListChoice() == SettingsForTaskTypeListFields.YES) {
                    // TODO Выбор из списка
                } else if (fieldString.getIsListChoice() == NO) {
                    editDirectoriesElements.getEditorField().setValue(directoriesItem.getDirectoriesItem()); // Ввод значений
                }
                $(By.xpath("//div[contains(text(),'" + directoriesItem.getFieldName() + "')]")).click(); //Убираем курсор с поля
                editDirectoriesElements.getSaveButton().click(); // Сохранить
                switchTo().defaultContent();
                editDirectoriesElements.getTextOfSuccessefullSave().waitUntil(visible, 30000); // Окно после добавления записи "Запись успешно сохранена"
                editDirectoriesElements.getOkButton().click(); // "ОК" в форме подтверждения
                editDirectoriesElements.getRecordInGrid(directoriesItem.getDirectoriesItem()).waitUntil(visible, 10000); // Проверяем отображение в гриде
            }
        }
    }
}
