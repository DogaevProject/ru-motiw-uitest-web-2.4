package ru.motiw.mobile.steps.ValidationStepsMobile;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.AssertJUnit;
import ru.motiw.mobile.elements.Tasks.NewTaskFormElementsMobile;
import ru.motiw.mobile.elements.Tasks.TaskElementsMobile;
import ru.motiw.mobile.steps.CardStepsMobile;
import ru.motiw.mobile.steps.Tasks.CardTaskStepsMobile;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.testng.Assert.assertTrue;
import static ru.motiw.mobile.model.Task.TabsOfTask.FILES_TAB;


/**
 * Проверки файлов на странице - Форма задачи/документа
 */
public class ValidateFilesStepsMobile extends CardStepsMobile {

    private File folder = new File(Configuration.reportsFolder);
    private TaskElementsMobile taskElementsMobile = page(TaskElementsMobile.class);
    private NewTaskFormElementsMobile newTaskFormElementsMobile = page(NewTaskFormElementsMobile.class);
    private CardTaskStepsMobile cardTaskStepsMobile = page(CardTaskStepsMobile.class);

    /**
     * Проверка названий в списке прикрепленных файлов  в форме создания задачи и в форме созданной задачи
     * Проверка открытия файла в предпросмотре (по клику) на вкладке "Описание" в форме созданной задачи
     * проверка текста в просмотрике файлов в форме созданной задачи
     * todo и скачивание
     *
     * @param task объект Задача
     * @return
     */
    public ValidateFilesStepsMobile verifyFilesInTheList(Task task) {

        verifyNameOfAttachedFiles(task.getFileName()); // Названия файлов в списке поля "Файлы"

        //Выполняем проверку файлов в предпросмотре только в форме созданной задачи
        if (taskElementsMobile.getButtonOfDescriptionTab().is(visible)) {
            if (task.getFileName() == null) {
                return this;
            } else {
                for (String fileName : task.getFileName()) {
                    $(By.xpath("//div[contains(@id,'object') and not(contains(@class,\"x-hidden-display\"))]" +
                            "//i[contains(@class,'file')]/ancestor::div[contains(@class,\"x-body-wrap-el x-panel-body-wrap-el x-container-body-wrap-el x-component-body-wrap-el \")]" +
                            "//div[contains(text(),'" + fileName + "')]"))
                            .click(); //открытие файла в предпросмотре
                    sleep(500);
                    verifyTextInFilesInPreviewOnDescriptionPage(fileName); //Проверка наличия текста в просмотрщике файлов
                    $(By.xpath("//div[@class=\"x-component x-button x-round-button-floated x-has-icon x-icon-align-left x-arrow-align-right x-button-alt x-component-alt x-button-round x-component-round x-floating\"]"))
                            .click();  //закрытие предпросмотра
                    sleep(500);
                }
            }
        }
        return this;
    }

    /**
     * Проверка названий файлов в списке поля "Файлы"
     *
     * @param nameOfFiles
     */
    private void verifyNameOfAttachedFiles(String[] nameOfFiles) {
        if (nameOfFiles != null)
            for (String nameOfFile : nameOfFiles) {
                assertTrue(verifyNameFileInTheListFiles(nameOfFile), "Не пройдена проверка названий Файлов в списке прикрепленных файлов");
            }
    }


    /**
     * Проверка наличия текста в просмотрщике файлов на вкладке "Описание" задачи
     *
     * @param textInFile передаваемое Имя файла для скачивания - текст с Именем файла находится в прикрепляемых для теста файлах
     */
    public ValidateFilesStepsMobile verifyTextInFilesInPreviewOnDescriptionPage(String textInFile) {
        switchTo().frame($(By.xpath("(//iframe)[2]")));  //Переходим во фрейм просмотра файлов
        $(By.xpath("//div[@id=\"viewerContainer\"]//div[@class=\"textLayer\"]/div[contains(text(),'" + textInFile + "')]")).waitUntil(visible, 5000);
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
        return this;
    }

    /**
     * Проверка названий Файлов в списке прикрепленных файлов
     *
     * @param nameOfFile названия файлов
     *                   false  если среди файлов в списке не находим название файла
     *                   true   если среди файлов в списке находим название файла
     */

    public boolean verifyNameFileInTheListFiles(String nameOfFile) {
        List<SelenideElement> nameFileInTheList = new ArrayList<>(newTaskFormElementsMobile.getListOfNameFiles());

        for (SelenideElement nameFile : nameFileInTheList) {
            if ($(nameFile).is((text(nameOfFile)))) {
                return true;
            }
        }
        return false;
    }


    /**
     * Скачивание файла в просмотрщике файлов формы задачи
     * Проверяем имя скаченного файла и наличие текста ( текст содержит имя файла) в просмотрщике файла.
     *
     * @param nameFiles передаваемое Имя файла для скачивания
     * @return TaskStepsMobile форма задачи
     * @throws FileNotFoundException в том случае, если файл не будет загружен метод .download() выкинет FileNotFoundException
     */
    public ValidateFilesStepsMobile verifyFilesInPreview(String[] nameFiles, int numbersOfFiles) throws FileNotFoundException {

        for (int numberOfCurrentFile = 1; numberOfCurrentFile < numbersOfFiles + 1; numberOfCurrentFile++) {
            File downloadedFile =
                    $(By.xpath("//div[contains(@class,\"x-container x-component x-titlebar-right x-size-monitored\")]//a[@href]")).download();

            // Проверяем имя скаченного файла и текст в просмотрщике файла.
            assertTrue(verifyNameAndTextOfFile(downloadedFile.getName(), nameFiles), "Название скаченного файла не совпадает с набором названий файлов прикрепляемых к задаче!");
            AssertJUnit.assertTrue(downloadedFile.getAbsolutePath().startsWith(folder.getAbsolutePath()));
            switchToNextFile(numberOfCurrentFile, numbersOfFiles); //Переход к следующему файлу через кнопку "Вперед" в просмотрщике файлов
        }
        return this;
    }

    /**
     * Сравнение имени скаченного файла с набором названий файлов прикрепляемых к задаче
     * Проверяем наличие текста в просмотрщике файла.
     *
     * @param nameOfDownloadedFile имя скаченного файла
     * @param nameFiles            набор названий файлов прикрепляемых к документу
     */

    private boolean verifyNameAndTextOfFile(String nameOfDownloadedFile, String[] nameFiles) {
        for (String nameFile : nameFiles) {
            if (nameOfDownloadedFile.equals(nameFile)) {
                verifyTextInFilesInPreview(nameFile); // Проверяем наличие текста в просмотрщике файла.
                return true;
            }
        }
        return false;
    }

    /**
     * Проверка наличия текста в просмотрщике файлов формы задачи
     *
     * @param textInFile передаваемое Имя файла для скачивания - текст с Именем файла находится в прикрепляемых для теста файлах
     */
    public void verifyTextInFilesInPreview(String textInFile) {
        switchTo().frame($(By.xpath("//iframe")));  //Переходим во фрейм просмотра файлов
        $(By.xpath("//div[@id=\"viewerContainer\"]//div[@class=\"textLayer\"]/div[contains(text(),'" + textInFile + "')]")).shouldBe(visible);
        switchTo().defaultContent();  //Уходим из фрейма просмотра файлов
    }

    /**
     * Переход к следующему файлу через кнопку "Вперед" в просмотрщике файлов
     *
     * @param numberOfCurrentFile Номер текущего файла открытого в предпросмотре
     * @param numbersOfFiles      Число всех прикрепленных файлов
     */

    public void switchToNextFile(int numberOfCurrentFile, int numbersOfFiles) {
        taskElementsMobile.getNumbersOnElementCounterFiles().waitUntil(Condition.text((numberOfCurrentFile) + " / " + numbersOfFiles), 2000); //проверка числа файлов в счетчике

        if (numbersOfFiles > 1) {
            $(By.xpath("//div[@class=\"x-icon-el x-font-icon x-mi mi-chevron-right\"]/ancestor::div[contains(@id,\"ext-filesnavigationbtn\")]")).click(); //переходим к следующему файлу в карусели
            sleep(500);
        }

        //Проверка изменения числа в счетчике после перехода к следующему файлу в просмотрщике
        if (numberOfCurrentFile != numbersOfFiles) {
            taskElementsMobile.getNumbersOnElementCounterFiles().waitUntil(Condition.text((numberOfCurrentFile + 1) + " / " + numbersOfFiles), 2000); //изменение числа в счетчике после переключения между файлами в просмотрщике
        }

        //В случае переключения "Вперед" с последнего файла, происходит переход к первому файлу, соответственно число в счетчике изменяется на 1
        if (numberOfCurrentFile == numbersOfFiles) {
            taskElementsMobile.getNumbersOnElementCounterFiles().waitUntil(Condition.text((1) + " / " + numbersOfFiles), 2000); //изменение числа в счетчике после переключения между файлами в просмотрщике
        }

    }

    /**
     * Удаление файлов через ДнД (свайп влево по плашке с файлом)
     *
     * @param nameOfFiles названия прикрепляемых файлов в объекте task
     * todo удалять файлы через открытие панели кнопок для взаимодействия с файлами (открытие панели кнопок через drag and drop)
     */
    public void deleteFile(String[] nameOfFiles) {
        for (String nameOfFile : nameOfFiles) {
            SelenideElement sourceElement = $(By
                    .xpath("//div[contains(@id,'object') and not(contains(@class,\"x-hidden-display\"))]//div[contains(text(), '" + nameOfFile + "')]/ancestor::div[contains(@class,\"x-listitem x-component\")]"));
            SelenideElement targetElement = $(taskElementsMobile.getElementAmongButtonsOfMenu());

            sleep(200);
            Actions builder = new Actions(getWebDriver());
            Action drag = builder.clickAndHold(sourceElement).build();
            Action drop = builder.moveToElement(targetElement)
                    .release(targetElement).build();
            drag.perform();
            drop.perform();
            sleep(500);
        }
    }

    /**
     * Проверка кол-во прикрепленных файлов
     * Проверка скачивания файлов и текста в просмотрике файлов (каруселе)
     *
     * @param task
     * @return TaskStepsMobile
     * @throws Exception
     */
    public ValidateFilesStepsMobile verifyFilesInTask(Task task) throws Exception {
        if (task.getFileName() == null) {
            return this;
        } else {
            // сравниваем кол-во прикрепленных файлов с числом отображаемым в элементе-переключтеле файлов.
            taskElementsMobile.getNumbersOnElementCounterFiles().shouldHave(text("1 / " + task.getNumberOfFiles()));
            //Проверка файлов в каруселе
            cardTaskStepsMobile.openTab(FILES_TAB);
            verifyFilesInPreview(task.getFileName(), task.getNumberOfFiles());
        }
        return this;
    }


    /**
     * Проверка кол-во прикрепленных файлов
     * Проверка файлов в каруселе
     *
     * @param document
     * @return TaskStepsMobile
     * @throws Exception
     */
    public ValidateFilesStepsMobile verifyFilesInDocument(Document document) throws Exception {
        if (document.getValueFiles() == null) {
            return this;
        } else {
            // сравниваем кол-во прикрепленных файлов с числом отображаемым в элементе-переключтеле файлов.
            taskElementsMobile.getNumbersOnElementCounterFiles().waitUntil(visible, 20000).shouldHave(text("1 / " + document.getNumberOfFiles()));
            //Проверка файлов в каруселе
            verifyFilesInPreview(document.getValueFiles(), document.getNumberOfFiles());
        }
        return this;
    }

}
