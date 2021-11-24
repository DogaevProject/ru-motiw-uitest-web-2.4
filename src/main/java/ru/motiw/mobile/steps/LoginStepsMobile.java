package ru.motiw.mobile.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.UIAssertionError;
import org.openqa.selenium.TimeoutException;
import ru.motiw.mobile.elements.Internal.InternalElementsMobile;
import ru.motiw.mobile.elements.Login.LoginPageElementsMobile;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.steps.BaseSteps;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;

/*
 * Страница авторизации - Mobile
 */
public class LoginStepsMobile extends BaseSteps {

    private LoginPageElementsMobile loginPageElementsMobile = page(LoginPageElementsMobile.class);
    private InternalElementsMobile internalElementsMobile = page(InternalElementsMobile.class);

    /**
     * Вводим Login пользователя
     *
     * @param login input text login
     */
    public LoginStepsMobile setLoginField(String login) {
        loginPageElementsMobile.getLogin().setValue(login);
        return this;
    }

    /**
     * Вводим пароль пользователя
     *
     * @param password input text password
     */
    public LoginStepsMobile setPasswordField(String password) {
        loginPageElementsMobile.getPassword().setValue(password);
        return this;
    }

    /**
     * Авторизация под пользователем
     */
    public LoginStepsMobile loginAs(Employee user) {
        if (loginPageElementsMobile.getLinkToMobile().is(Condition.visible)) {
            //Переход в мобильную версию по ссылке в форме авторизации полной версии
            loginPageElementsMobile.getLinkToMobile().click();
            loginPageElementsMobile.getLogin().waitUntil(Condition.visible, 20000);
        }

        setLoginField(user.getLoginName());
        setPasswordField(user.getPassword());
        loginPageElementsMobile.getLogon().click();
        return this;
    }

    /**
     * Ожидание открытия главной страницы после авторизации
     */
    public void waitLoadMainPage(Employee employee) {
        // Ожидание появления маски загрузки
        loginPageElementsMobile.getMaskOfLoading().waitUntil(Condition.appear, 5000);
        try {
            // Ожидание скрытия маски загрузки
            loginPageElementsMobile.getMaskOfLoading().waitUntil(Condition.disappear, 10000);
            // Ожидание кнопки Главного Меню
            internalElementsMobile.getButtonMainMenu().waitUntil(Condition.visible, 10000);
        } catch (UIAssertionError e) {
            clearBrowserCache();
            open(Configuration.baseUrl);
            loginAs(employee);
            // Ожидание кнопки Главного Меню
            internalElementsMobile.getButtonMainMenu().waitUntil(Condition.visible, 10000);
        }
    }


    /**
     * Проверяем то, что мы разлогинены - АРМ-интерфейс
     */
    public boolean isNotLoggedInMobile() {
        try {
            loginPageElementsMobile.getLogon().waitUntil(visible, 20000);
            loginPageElementsMobile.getPassword().shouldBe(Condition.visible);
            loginPageElementsMobile.getLogin().shouldBe(Condition.visible);
            return true;
        } catch (TimeoutException to) {
            return false;
        }
    }


}
