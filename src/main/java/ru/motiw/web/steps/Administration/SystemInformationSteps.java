package ru.motiw.web.steps.Administration;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import ru.motiw.web.steps.BaseSteps;

import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertFalse;
import static ru.motiw.web.model.URLMenu.SYSTEM_INFO;

/**
 * Информация о системе
 */
public class SystemInformationSteps extends BaseSteps {

    /**
     * Администрирование - Информация о системе
     */
    public static SystemInformationSteps goToURLSystemInfo() {
        openSectionOnURL(SYSTEM_INFO.getMenuURL());
        return page(SystemInformationSteps.class);
    }

    /**
     * Проверка отсутствия красных значений
     */
    public SystemInformationSteps assertNotRedElement() {
        assertFalse($(By
                .xpath("/*//*[contains (@style, '#F83838') and not(contains(text(),'Несоответствие версий базы данных')) and not(descendant::b[contains(text(),'Нет настроенных STUN серверов')])]")).exists());
        return this;
    }

    /**
     * Проверка Загрузки страницы - ожидание наличия кнопки чейнджллога
     */
    public SystemInformationSteps ensurePageLoaded() {
        sleep(2000);
        $(By.xpath("//input[@type='button' and @value='ChangeLog']")).shouldBe(Condition.visible);
        return this;
    }

    /**
     * Проверяем отсутствия красных элеметов (Работоспособность запущенных служб)
     */
    public boolean checkingOfSystemServices() {
        ensurePageLoaded();
        try {
            assertNotRedElement();
            return true;
        } catch (TimeoutException to) {
            return false;

        }
    }
}