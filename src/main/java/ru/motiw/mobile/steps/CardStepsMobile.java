package ru.motiw.mobile.steps;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.UIAssertionError;
import ru.motiw.mobile.steps.Document.ResolutionStepsMobile;
import ru.motiw.web.model.Administration.Users.Employee;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.refresh;

/**
 *  Шаги в карточке задачи/документа
 */
public class CardStepsMobile extends BaseStepsMobile {


    /*
     * Открытие компонента выбора пользователей
     */
    protected void openFormSelectUser(SelenideElement fieldCustomRole) {
        fieldCustomRole.click(); //клик в само поле.
        formElementsMobile.getInputForSearchUsers().waitUntil(visible, 5000); // поле ввода
    }

    /**
     * Добавление/Удаление пользователей в роли задачи
     *
     * @param employees       передаваемые пользователи
     * @param fieldCustomRole выбираемая роль в задаче (Исполнители, Авторы и ОР)
     */
    protected void choiceUserOnTheRole(Employee[] employees, SelenideElement fieldCustomRole) {
        if (employees != null) {
            openFormSelectUser(fieldCustomRole);
            for (Employee employee : employees) {
                if (employee.getLastName() != null) {
                    //Очищаем поле, если содержит ранее введенные значения
                    if (formElementsMobile.getClearTriggerInputForSearchUsers().isDisplayed()) {
                        formElementsMobile.getClearTriggerInputForSearchUsers().click();
                    }

                    formElementsMobile.getInputForSearchUsers().setValue(employee.getLastName()); // вводим в поле ввода Фамилию пользователя
                    formElementsMobile.getListOfUsers().shouldBe(CollectionCondition.size(1), 10000); //ожидание когда будет найден один пользователь. Это с учетом того, что у нас доступно для выбора больше одного пользователя.

                    //Выбор пользователя в списке
                    formElementsMobile.getUserFromList(employee.getLastName()).shouldBe(visible).click();
                    //formElementsMobile.getListOfUsers().shouldBe(CollectionCondition.sizeGreaterThan(1), 5000); //ожидание когда загрузится список пользователей. Это с учетом того, что у нас доступно для выбора больше одного пользователя.
                }
            }
            formElementsMobile.getButtonAppointUsers().click(); //кнопка "Назначить"
            formElementsMobile.getInputForSearchUsers().waitUntil(not(visible), 2000); // Ожидание закрытия формы
        }
    }

    /**
     * Ожидание Тулбара меню Формы задачи/документа
     */
    public CardStepsMobile waitToolbarOfMenu() {
        try {
            formElementsMobile.getToolbarOfMenu().waitUntil(visible, 15000);
        } catch (UIAssertionError elementNotFound) {
            refresh();
            formElementsMobile.getToolbarOfMenu().waitUntil(visible, 15000);
        }
        return this;
    }

}
