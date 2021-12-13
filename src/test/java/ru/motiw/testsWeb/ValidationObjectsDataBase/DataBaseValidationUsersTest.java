package ru.motiw.testsWeb.ValidationObjectsDataBase;

import com.codeborne.selenide.testng.TextReport;
import com.codeborne.selenide.testng.annotations.Report;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.motiw.data.BaseTest;
import ru.motiw.data.listeners.ScreenShotOnFailListener;
import ru.motiw.web.model.Administration.Users.Employee;
import ru.motiw.web.model.Administration.Users.Module;
import ru.motiw.web.steps.Administration.Users.UsersSteps;
import ru.motiw.web.steps.Home.InternalSteps;
import ru.motiw.web.steps.Login.LoginStepsSteps;

import java.sql.*;

import static com.codeborne.selenide.Selenide.page;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertTrue;
import static ru.motiw.data.dataproviders.Administration.getRandomEmployer;
import static ru.motiw.web.steps.Administration.Users.DepartmentSteps.goToURLDepartments;


@Listeners({ScreenShotOnFailListener.class, TextReport.class})
@Report
public class DataBaseValidationUsersTest extends BaseTest {

    private LoginStepsSteps loginPageSteps;
    private InternalSteps internalPageSteps;
    private Connection connection;
    private Statement statement;
    private UsersSteps userPageSteps;

    @BeforeClass
    public void beforeTest() {
        loginPageSteps = page(LoginStepsSteps.class);
        internalPageSteps = page(InternalSteps.class);
        userPageSteps = page(UsersSteps.class);
    }

    Employee user = getRandomEmployer()
            .setModule(Module.COMPLETE) // Право на модуль
            .setNeedsPasswordChange(false);


    @Test(priority = 1)
    public void checkValidationOfUsersInTheDatabase() throws SQLException, ClassNotFoundException {

        try {
            this.connection = connectionDB();
            // Создание Statement для отправки запроса к базе данных
            this.statement = connection.createStatement();
            // Результирующий запрос
            ResultSet result = this.statement.executeQuery("select * from employer;");

            int i = 1;
            while (result.next()) {
                System.out.println("Текущий список пользователей в БД: " + "\n" +
                        i + " " + "пользователь: " + result.getString("name") + " "
                        + result.getString("login"));
                i++;
            }
            result.close();

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            fail("Connection Failed! Fail verify users in DataBase");
        }

        // Авторизация
        loginPageSteps.loginAs(ADMIN);
        assertThat("Check that the displayed menu item 8 (Logo; Tasks; Documents; Messages; Calendar; Library; Tools; Details)",
                internalPageSteps.hasMenuUserComplete()); // Проверяем отображение п.м. на внутренней странице*/
        goToURLDepartments();
        userPageSteps
                // Создание пользователей
                .createUser(user);

        // verify the new user in the database
        // create a statement
        PreparedStatement stat = this.connection.prepareStatement("SELECT * FROM employer where login=? AND name=?");
        stat.setString(1, user.getLoginName());
        stat.setString(2, user.getLastName() + " " + user.getName() + " " + user.getPatronymic());
        try {
            boolean hasResultSet = stat.execute();
            if (hasResultSet) {
                ResultSet result = stat.getResultSet();
                while (result.next()) {
                    // get new user name from the table
                    String newLoginUser = result.getString("login");
                    String newUserName = result.getString("name");
                    // assert that new user name should be
                    assertEquals(user.getLoginName(), newLoginUser);
                    assertEquals(user.getLastName() + " " + user.getName() + " " + user.getPatronymic(), newUserName);

                    System.out.println("Info DB: Логин: " + newLoginUser + " " + "ФИО: " + newUserName);
                    System.out.println("login specified when initializing a new user: " + "Логин: " + user.getLoginName() + " " +
                            "Фамилия: " + user.getLastName() + " " + "Имя: " + user.getName() + " " + "Отчество: " + user.getPatronymic());
                }

                result.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            fail("Fail verify users (login and name) in DataBase");
        } finally {
            this.connection.close();
            this.statement.close();
            System.out.println("Game Over! Failed to make connection!");
        }
        internalPageSteps.logout(); // Выход из системы
        assertTrue(loginPageSteps.isNotLoggedIn()); // Проверка того, что пользователь разлогинен
    }

}