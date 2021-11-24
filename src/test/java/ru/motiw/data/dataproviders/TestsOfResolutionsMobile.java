package ru.motiw.data.dataproviders;

import org.testng.annotations.DataProvider;
import ru.motiw.data.BaseTest;
import ru.motiw.mobile.model.AuthorOfAnnotation;
import ru.motiw.mobile.model.FilesForAttachment;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.DocRegisterCards;
import ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.RouteSchemeEditor;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.Document.ExecutionOfDocument;
import ru.motiw.web.model.Document.Resolution;
import ru.motiw.web.model.Tasks.Folder;

import static ru.motiw.data.dataproviders.Administration.getRandomDepartment;
import static ru.motiw.data.dataproviders.Administration.getRandomEmployer;
import static ru.motiw.data.dataproviders.Tasks.getRandomProject;
import static ru.motiw.mobile.model.AuthorOfAnnotation.NumbersOfAnnotation.FirstAnnotation;
import static ru.motiw.mobile.model.AuthorOfAnnotation.NumbersOfAnnotation.SecondAnnotation;
import static ru.motiw.mobile.model.Document.TypeOperationsOfDocument.*;


/**
 * Данные для тестов резолюций документов в АРМ
 */
public abstract class TestsOfResolutionsMobile extends BaseTest {

    // Инициализация объекта - Названия Файлов задачи
    private String[] file = new String[]{
            FilesForAttachment.FILE_1.getNameFile(),
            FilesForAttachment.FILE_2.getNameFile(),
            FilesForAttachment.FILE_3.getNameFile(),
    };

    //---------------------------------------------------------------------------------------------------------- Инициализируем объект - Подразделение и Пользователь
    private Department[] department = new Department[]{getRandomDepartment()};

    private Employee[] employee = new Employee[]{getRandomEmployer(), getRandomEmployer(), getRandomEmployer(), getRandomEmployer()};

    // Инициализация РКД и её настроек
    private DocRegisterCards registerCards = new DocRegisterCards("wD_Тестовая карточка " + randomString(20))
            .setCheckBoxUseAllPossibleRoutes(true); // Использовать все возможные маршруты

    //----------------------------------------------------------------------------------------------------------- Инициализация Документа
    private Document document = new Document()

            .setDocumentType(registerCards) // Тип документа
            .setAuthorOfDocument(EMPLOYEE_ADMIN)
            .setDateRegistration(randomDateTime()) // Дата регистрации
            .setProject(getRandomProject()) // Инициализируем проект документа
            .setValueFiles(new String[]{file[0]})
            .setRouteSchemeForDocument(new RouteSchemeEditor()
                    .setRouteScheme("Согласование входящей корреспонденции - Постановка задачи")
                    .setReviewDuration(randomInt(999))
                    .setUserRoute(new Employee[]{employee[0]}) // Добавляем в маршрут созданного пользователя
            )
            .setResolutionOfDocument(new Resolution[]{
                            (Resolution) new Resolution()
                                    .setTextOfResolution(randomString(20))
                                    .setAuthorDefault(EMPLOYEE_ADMIN)
                                    .setAuthors(new Employee[]{employee[0]})
                                    .setExecutiveManagers(new Employee[]{employee[1]}),
                            (Resolution) new Resolution()
                                    .setTextOfResolution(randomString(20))
                                    .setAuthorDefault(EMPLOYEE_ADMIN)
                                    .setAuthors(new Employee[]{employee[1]})
                                    .setExecutiveManagers(new Employee[]{employee[2]}),
                            (Resolution) new Resolution()
                                    .setTextOfResolution(randomString(20))
                                    .setAuthorDefault(EMPLOYEE_ADMIN)
                                    .setAuthors(new Employee[]{employee[2]})
                                    .setExecutiveManagers(new Employee[]{employee[3]})

                    } // Резолюция
            );

    // ----------------------------------------------------------------------------------------------------------- Инициализация Папки
    private Folder[] folder = getRandomArrayFolders();

    /**
     * Метод создания полностью случайного массива объектов - "Папка"
     *
     * @return folders с атрибутами полей объекта - Папка
     */
    public static Folder[] getRandomArrayFolders() {
        return new Folder[]{
                new Folder()
                        .setNameFolder("wD_Box " + randomString(10)) // Зн-ие НЕ изменять - используется в проверке - checkDisplayCreateAFolderInTheGrid()
                        .setUseFilter(randomBoolean())
                        .setChooseRelativeValue(randomBoolean())
                        .setSharedFolder(randomBoolean())
                        .setAddSharedFolderForAll(randomBoolean())
                        .setAddSharedFolderForNewUsers(randomBoolean()),
        };
    }

    /**
     * Параметризация - Инициализируем модель - Объекты необходимые для  проверки создания документа
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataForCreateProjectsInWeb() {
        return new Object[][]{
                {
                        //переменная объекта - ПОДРАЗДЕЛЕНИЕ
                        department,
                        //переменная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee,
                        //переменная объекта - РКД
                        registerCards,
                        //переменная объекта - ДОКУМЕНТ
                        document,
                        //переменная объекта - Папка
                        folder
                }
        };
    }


    /**
     * Параметризация - Инициализируем модель - Объекты необходимые для  проверки утверждения резолюции в арм
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataForVerifyingApproveFirstProjectResolution() {
        return new Object[][]{
                {
                        //переменная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee[0],
                        //переменная объекта - ДОКУМЕНТ
                        document,
                        //переменная объекта - Папка
                        folder[0]
                }
        };
    }


    /**
     * Параметризация - Инициализируем модель - Объекты необходимые для  проверки утверждения резолюции в арм
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataForVerifyingApproveSecondProjectResolution() {
        return new Object[][]{
                {
                        //переменная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee[1],
                        //переменная объекта - ДОКУМЕНТ
                        document,
                        //переменная объекта - Папка
                        folder[0]
                }
        };
    }

    /**
     * Авторы граф.коментария
     */
    private AuthorOfAnnotation[] authorOfAnnotations = new AuthorOfAnnotation[]{
            new AuthorOfAnnotation(employee[0]).setNumberOfAnnotation(FirstAnnotation),
    };


    /**
     * Параметризация - Инициализируем модель - Объекты необходимые для  проверки утверждения резолюции в арм
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataForVerifyingApproveThirdProjectResolution() {
        return new Object[][]{
                {
                        //переменная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee[2],
                        //переменная объекта - ДОКУМЕНТ
                        document,
                        //переменная объекта - Папка
                        folder[0],
                        authorOfAnnotations
                }
        };
    }
}
