package ru.motiw.web.steps.Documents.Resolution;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.motiw.web.elements.elementsweb.Documents.Resolution.CreateResolution.CreateResolutionFormElements;
import ru.motiw.web.elements.elementsweb.Tasks.TaskForm.UsersSelectTheFormElements;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Document.Resolution;
import ru.motiw.web.steps.BaseSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ru.motiw.utils.WindowsUtil.newWindowForm;


/**
 * Работа с резолюциями
 */
public class ResolutionSteps extends BaseSteps {

    private UsersSelectTheFormElements usersSelectTheFormElements = page(UsersSelectTheFormElements.class);
    private CreateResolutionFormElements createResolutionFormElements = page(CreateResolutionFormElements.class);

    /**
     * Добавление в поле "Авторы"
     */
    public ResolutionSteps addAuthor(Employee[] users) {
        choiceUsersThroughTheSearchLiveSurname(users, createResolutionFormElements.fieldToAddAuthorField());
        return this;
    }

    /**
     * Удаление в поле "Авторы" через кнопку
     */
    public ResolutionSteps deleteAuthor(Employee user) {
        createResolutionFormElements.buttonToDeleteUserInAuthorField(user.getLastName()).click();
        return this;
    }

    /**
     * Добавление в поле "Ответственные руководители"
     */
    public ResolutionSteps addExecutiveManager(Employee[] users) {
        choiceUsersThroughTheSearchLiveSurname(users, createResolutionFormElements.fieldToAddExecutiveManagerField());
        return this;
    }

    /**
     * Добавление в поле "Текст резолюций"
     */
    public ResolutionSteps addTextOfResolution(String text) {
        createResolutionFormElements.fieldToActivateTextOfResolution().click();
        createResolutionFormElements.fieldToAddTextOfResolution().setValue(text);
        return this;
    }


    /**
     * TODO Добавить пользователя в роль через форму выбора пользователей
     *
     * @param addAUserToARole элемент для выбора пользователя (кнопка - Добавить пользователя)
     */
  /*
    private void toAddaUserToARole(SelenideElement addAUserToARole,
                                   Employee[] users) {
        for (Employee user : users) {
            addAUserToARole.click(); // Добавить пользователя
            switchTo().frame($(By.cssSelector("#selectemployers")));
            usersSelectTheFormElements.getUserSearchField().waitUntil(visible, 5000).setValue(user.getLastName());
            usersSelectTheFormElements.getUserSearchField().pressEnter();
            if (usersSelectTheFormElements.getUserSaveButton().exists()) {
                usersSelectTheFormElements.getAddTheUserInTheFormOfChoice(user).click(); // Добавить пользователя
                usersSelectTheFormElements.getUserSaveButton().click(); // Сохранить выбранных пользователей
            } else
                usersSelectTheFormElements.getAddTheUserInTheFormOfChoice(user).click(); // Добавить пользователя
            switchTo().frame($(By.cssSelector("#resolution_window")));
        }
    }*/


    /**
     * Добавление пользователей в роль, через livesearch - Поиск по фамилии
     *
     * @param employees       массив передаваемых пользователей (Фамилия пользователя)
     * @param fieldCustomRole передаваемая выбираемая роль
     */
    private void choiceUsersThroughTheSearchLiveSurname(Employee[] employees, SelenideElement fieldCustomRole) {
        if (employees != null) {
            for (Employee employee : employees) {
                $(fieldCustomRole).shouldNotBe(Condition.disabled);
                fieldCustomRole.click();
                $(By.xpath("//*[contains (@class, 'x-editor')][not(contains (@style, 'none'))]//input")).setValue(employee.getLastName());
                $(By.xpath("//div[@class=\"x-boundlist x-boundlist-floating x-layer x-boundlist-default x-border-box\"]//li[contains(text(), '" + employee
                        .getLastName() + "')]")).shouldBe(Condition.visible).click();
                $(By.xpath("(//div[@id=\"resolution-form-body\"]//div[text()=\"Проект\"]/ancestor::table//div[@class=\"x-grid-cell-inner \"])[1]")).click();
            }
        }
    }
}
