package ru.motiw.data.dataproviders;

import org.testng.annotations.DataProvider;
import ru.motiw.data.BaseTest;
import ru.motiw.mobile.model.FilesForAttachment;
import ru.motiw.web.model.AccessRights;
import ru.motiw.web.model.Administration.Directories.Directories;
import ru.motiw.web.model.Administration.FieldsObject.*;
import ru.motiw.web.model.Administration.TaskTypeListEditObject;
import ru.motiw.web.model.Administration.TasksTypes.ObligatoryField;
import ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields;
import ru.motiw.web.model.Administration.TasksTypes.TasksTypes;
import ru.motiw.web.model.Administration.Users.Department;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.CorrectionMethod;
import ru.motiw.web.model.DocflowAdministration.DictionaryEditor.DictionaryEditor;
import ru.motiw.web.model.DocflowAdministration.DictionaryEditor.DictionaryEditorField;
import ru.motiw.web.model.DocflowAdministration.DocumentRegistrationCards.*;
import ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.BlockOfRouteScheme;
import ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.RouteSchemeEditor;
import ru.motiw.web.model.Document.Document;
import ru.motiw.web.model.OpenFilesForEdit;
import ru.motiw.web.model.ShiftDirection;
import ru.motiw.web.model.Tasks.Folder;
import ru.motiw.web.model.Tasks.Task;

import static ru.motiw.data.dataproviders.Administration.getRandomDepartment;
import static ru.motiw.data.dataproviders.Administration.getRandomEmployer;
import static ru.motiw.data.dataproviders.Tasks.getRandomProject;
import static ru.motiw.web.model.Administration.TasksTypes.SettingsForTaskTypeListFields.YES;
import static ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.TypesBlockOfRouteScheme.ANY_BOSS;
import static ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor.TypesBlockOfRouteScheme.ANY_WORK_GROUP;

/**
 * Данные для проверки согласования
 */
public abstract class ConsiderationDocflow extends DocflowAdministration {

    Employee[] employee = new Employee[]{getRandomEmployer(), getRandomEmployer(), getRandomEmployer()};


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


    //---------------------------------------------------------------------------------------------------------- Инициализируем объект - Подразделение и Пользователь
    Department[] department = new Department[]{getRandomDepartment()};


    // Инициализация объекта - Названия Файлов задачи
    String[] file = new String[]{
            FilesForAttachment.FILE_1.getNameFile(),
            FilesForAttachment.FILE_2.getNameFile(),
            FilesForAttachment.FILE_3.getNameFile(),
    };

    // Инициализация объекта - Типы задач с настройками
    TaskTypeListEditObject tasksTypes = new TasksTypes("wD_Тип задачи_for_DRC (verify трансл. значений полей) "
            + randomString(5))
            // Поля типа
            .setTaskTypeListObjectFields(new FieldObject[]{});


    //-----------Инициализация объекта - Задача (с атрибутами)
    Task task = new Task()
            .setTaskName(randomString(10))
            .setTaskType((TasksTypes) tasksTypes);


    // =========================Инициализация полей объекта - Документ


    /*
    СТРОКА (Уникальное == Да)
    */
    FieldDocument fieldUniqueString = new FieldTypeStringDocument()
            .setDocumentFieldName("Строка (Уникальное) " + "|транс. 1| " + randomString(5)) // Имя поля документа
            .setFieldIdentifierDoc("STRING" + randomIdentifier(5)) // Идентификатор поля
            .setEditableField(true)
            .setUniqueField(true) // Уникальное поле
            .setObjectFieldDocument(new FieldTypeStringDocument()
                    .setFieldLength(randomInt(999))); // Длина поля

    /*
    НУМЕРАТОР
    */
    FieldDocument fieldNumerator = new FieldTypeNumeratorDocument()
            .setDocumentFieldName("Нумератор " + randomString(5))
            .setFieldIdentifierDoc("NUMERATOR" + randomIdentifier(5)) // Идентификатор поля
            .setObjectFieldDocument(new FieldTypeNumeratorDocument()
                    .setNumeratorTemplateDoc("{" + fieldUniqueString.getFieldIdentifierDoc() + "}-{counter}-{DD}.{YYYY} "
                            + randomString(10))
                    .setEditionAvailableWhileCreation(true)) // Изменяемое при создании
            .setObligatoryFieldDoc(ObligatoryFieldDocument.REQUIRED_WHEN_CREATION) // Обязательное поле == Обязательное при создании
            .setEditableField(true); // Обязательное при редактировании (true == Да; false == Нет)


    // Инициализация РКД и её настроек
    DocRegisterCards registerCards = new DocRegisterCards("wD_Тестовая карточка " + randomString(20))
            .setCheckBoxUseAllPossibleRoutes(true) // Использовать все возможные маршруты
            .setDisplayNameTemplate("{" + fieldNumerator.getFieldIdentifierDoc() + "}, " + "{" + fieldUniqueString.getFieldIdentifierDoc() + "} "
                    + randomString(15)) // Шаблон отображения
            // Типы полей документа
            .setFieldDocumentFields(new FieldDocument[]{fieldUniqueString, fieldNumerator
            })
            // Тип задачи по рассмотрению / исполнению документа
            .setTheTypeOfTaskToReviewAndExecutionOfDocument(tasksTypes.getObjectTypeName());

    //----------------------------------------------------------------------------------------------------------- Инициализация Документа для согласования
    Document documentForConsideration_1 = new Document()

            .setDocumentType(registerCards) // Тип документа
            .setAuthorOfDocument(EMPLOYEE_ADMIN)
            .setDateRegistration(randomDateTime()) // Дата регистрации
            .setProject(getRandomProject()) // Инициализируем проект документа
            // Осуществляем заполнение (наполнение) полей документа через массив
            .setDocumentFields(new FieldDocument[]{
                            (FieldDocument) fieldUniqueString.setValueField(task.getTaskName())
                    }
            )
            .setRouteSchemeForDocument(new RouteSchemeEditor()
                    .setRouteScheme("Маршрут согласования договора")
                    .setReviewDuration(randomInt(999))
                    .setUserRouteWithCoupleBlocks(new BlockOfRouteScheme[]{
                            new BlockOfRouteScheme("Произвольная рабочая группа", new Employee[]{employee[0]}, ANY_WORK_GROUP),
                            new BlockOfRouteScheme("Произвольный начальник", employee[1], ANY_BOSS)
                    }) // Добавляем в маршрут созданного пользователя
            );


    /**
     * Параметризация - Инициализируем модель - Регистрационная карточка документа (со всеми надстройками)
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataRCD() {

        return new Object[][]{
                {
                        //пременная объекта - ПОДРАЗДЕЛЕНИЕ
                        department,
                        //пременная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee,
                        //пременная объекта - ТИПЫ ЗАДАЧ
                        tasksTypes,
                        //пременная объекта - РКД
                        registerCards,
                        //пременная объекта - ДОКУМЕНТ
                        documentForConsideration_1,
                        //переменная объекта - Папка
                        folder

                }
        };

    }

    /**
     * Данные для проверки согласования
     *
     * @return массив параметров объектов системы
     */
    @DataProvider
    public Object[][] objectDataForConsideration() {
        return new Object[][]{
                {
                        //пременная объекта - ПОЛЬЗОВАТЕЛЬ
                        employee,
                        //пременная объекта - ДОКУМЕНТ
                        documentForConsideration_1,
                        // задача по джокументу
                        task,
                        //переменная объекта - Папка
                        folder

                }
        };
    }

}
