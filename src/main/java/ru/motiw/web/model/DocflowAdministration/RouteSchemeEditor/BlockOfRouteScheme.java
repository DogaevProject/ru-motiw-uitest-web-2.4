package ru.motiw.web.model.DocflowAdministration.RouteSchemeEditor;


import ru.motiw.web.model.Administration.Users.Employee;

/**
 * Модель объекта - Блок Маршрутной схемы
 */
public class BlockOfRouteScheme {

    private String nameOfBlock; // название уровня блока
    private Employee[] usersRoute;
    private Employee userRoute;
    private TypesBlockOfRouteScheme typesBlockOfRouteScheme;


    public BlockOfRouteScheme(String nameOfBlock, Employee userRoute, TypesBlockOfRouteScheme typesBlockOfRouteScheme) {
        this.nameOfBlock = nameOfBlock;
        this.userRoute = userRoute;
        this.typesBlockOfRouteScheme = typesBlockOfRouteScheme;
    }


    public BlockOfRouteScheme(String nameOfBlock, Employee[] usersRoute, TypesBlockOfRouteScheme typesBlockOfRouteScheme) {
        this.nameOfBlock = nameOfBlock;
        this.usersRoute = usersRoute;
        this.typesBlockOfRouteScheme = typesBlockOfRouteScheme;
    }


    public String getNameOfBlock() {
        return nameOfBlock;
    }


    public Employee[] getUsersRoute() {
        return usersRoute;
    }

    public Employee getUserRoute() {
        return userRoute;
    }

    public TypesBlockOfRouteScheme getTypesBlockOfRouteScheme() {
        return typesBlockOfRouteScheme;
    }

}
