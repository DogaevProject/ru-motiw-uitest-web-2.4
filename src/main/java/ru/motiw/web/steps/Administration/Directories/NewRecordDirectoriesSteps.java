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
import static ru.motiw.utils.WindowsUtil.newWindowForm;
import static ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields.NO;

/**
 * форма - Добавления записи справочника
 */

public class NewRecordDirectoriesSteps extends BaseSteps {

    private EditDirectoriesElements editDirectoriesElements = page(EditDirectoriesElements.class);

    /**
     * @return  вощвращаем стр. для дальнейшего взаимодействия
     * с елементами на странице
     */
    public NewRecordDirectoriesSteps goTo(Directories directories) {

        String parentWindowHandler = getWebDriver().getWindowHandle(); //Запоминаем окно в котором находимся

        //Переход по ссылке в созданный справочник
        $(By.xpath("//table//a[contains(text(),'" + directories.getObjectTypeName() + "')]")).scrollTo();
        $(By.xpath("//table//a[contains(text(),'" + directories.getObjectTypeName() + "')]")).click();
        getWebDriver().switchTo().window(new WebDriverWait(getWebDriver(), 10).until(newWindowForm(By.xpath("//*[contains(text(),'Всего')]"))));

        //Добавление записи справочника
        addAllFieldsTaskTypeList(directories.getDirectoriesFields());

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
     * Добавление записи справочника
     * Заполнение полей
     *
     * @param directoriesFields атрибуты объекта - запись справочника
     */
    public NewRecordDirectoriesSteps addAllFieldsTaskTypeList(DirectoriesField[] directoriesFields) {
        if (directoriesFields == null) {
            return this;
        }
        for (DirectoriesField directoriesItem : directoriesFields) {

            // СТРОКА
            if (directoriesItem.getFieldType() instanceof TypeListFieldsString) {
                editDirectoriesElements.getAddRecordButton().click(); // Добавить запись
                getFrameObject($(By.xpath("//iframe[contains(@src,'/user/dict_record_edit/')]")));
                selectTheTypeOfField(editDirectoriesElements.getTypeFieldString(directoriesItem.getFieldName()));
                sleep(500);
                TypeListFieldsString fieldString = (TypeListFieldsString) directoriesItem.getFieldType();
                if (fieldString.getIsListChoice() == SettingsForTaskTypeListFields.YES) {
                    // TODO
                   // selectionEnumerationFieldSettingsForTheTaskTypeList(fieldsElements.getSelectionFromList(), fieldString.getIsListChoice()); // Выбор из списка
                    //fieldsElements.getFieldListValue().setValue(fieldString.getValuesList()); // Список значений
                } else if (fieldString.getIsListChoice() == NO) {
                    editDirectoriesElements.getEditorField().setValue(directoriesItem.getDirectoriesItem()); // Ввод значений
                }
                $(By.xpath("//div[contains(text(),'" + directoriesItem.getFieldName() + "')]")).click(); //Убираем курсор с поля
                editDirectoriesElements.getSaveButton().click(); // Сохранить
                editDirectoriesElements.getTextOfSuccessefullSave().waitUntil(visible, 2000); // Окно после добавления записи "Запись успешно сохранена"
                editDirectoriesElements.getOkButton().click(); // "ОК" в форме подтверждения
                editDirectoriesElements.getRecordInGrid(directoriesItem.getDirectoriesItem()).waitUntil(visible, 1000); // Проверяем отображение в гриде
            }

        }
        return this;
    }

}