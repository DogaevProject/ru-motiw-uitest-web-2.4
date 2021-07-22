package ru.motiw.mobile.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.UIAssertionError;
import org.openqa.selenium.NoSuchElementException;
import ru.motiw.mobile.elements.Internal.InternalElementsMobile;
import ru.motiw.mobile.elements.Login.LoginPageElementsMobile;
import ru.motiw.web.steps.BaseSteps;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.testng.Assert.assertTrue;

/**
 * Внутреняя страница системы
 */
public class InternalStepsMobile extends BaseSteps {

    protected InternalElementsMobile internalElementsMobile = page(InternalElementsMobile.class);
    private LoginStepsMobile loginStepsMobile = page(LoginStepsMobile.class);
    private LoginPageElementsMobile loginPageElementsMobile = page(LoginPageElementsMobile.class);

    /**
     * Открываем главное меню
     *
     * @return results internal page instance
     */
    public InternalStepsMobile goToInternalMenu() {
        internalElementsMobile.getButtonMainMenu().click();
        return page(InternalStepsMobile.class);
    }

    /**
     * Домой (возврат на основную стр-цу)
     */
    public BaseSteps goToHome() {
        internalElementsMobile.getButtonGoHome().click();
        return this;
    }


    /**
     * Проверяем отображение меню на внутренней странице
     *
     * @return information about the number of the menu on the main page
     */
    public boolean hasMenuUserComplete() {
        internalElementsMobile.getMenuElementsMobile().shouldHaveSize(9);
        return !internalElementsMobile.getMenuElementsMobile().isEmpty();
    }

    /**
     * Универсальный выход из системы (где бы не находился пользователь)
     */
    public void logout() {
        sleep(500);
        if (internalElementsMobile.getButtonMainMenu().is(Condition.not(Condition.exist))) {
            goToHome(); // Если кнопка открытия главного меню недоступна, то переходим в папки
        }
        goToInternalMenu(); // Открываем главное меню
        internalElementsMobile.getLogout().click(); // Выход
        assertTrue(loginStepsMobile.isNotLoggedInMobile());// Проверяем то, что мы разлогинены
        clearBrowserCache();
        refresh(); // Очитска кэша и перезагрузка страницы т.к после логаута могут быть проблемы с повторной автворизацией.
        waitLoginForm(); // Ждем появление формы авторизации (страница приведена в состояние пригодное к дальнейшему взаимодействию)
    }

    /**
     * Ждем появление формы авторизации
     */
    private void waitLoginForm() {
        // обработка на тот случай, если загрузка страницы полностью не произойдет, то обновляем страницу
        try {
            loginPageElementsMobile.getLogon().waitUntil(Condition.visible, 10000); // Ждем появление формы авторизации
        } catch (ElementNotFound e) {
            refresh();
            loginPageElementsMobile.getLogon().waitUntil(Condition.visible, 10000);
        }
    }

    /**
     * Выйти из системы, если по какой-то причине (например, падение предыдущего теста) не вышли из системы.
     */
    public void goToAuthorizationPage() {
        if (!loginPageElementsMobile.getLogon().is(Condition.visible)) {
            logout();
        }
    }

}
