package com.malerx.mctester.view;

import com.malerx.mctester.kafka.PostmanService;
import com.malerx.mctester.repositories.MongoTestDataRepositories;
import com.malerx.mctester.service.validator.ValidationChain;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Route("")
@PageTitle("Sphinx. Testing microservice.")
public class MainView extends VerticalLayout {
    private final MongoTestDataRepositories repositories;
    private final String name;
    private final PostmanService postman;
    private final ValidationChain validation;

    public Grid<RepoDataClass> grid = new Grid<>(RepoDataClass.class, false);
    private Button startTest;

    @Autowired
    public MainView(MongoTestDataRepositories repositories,
                    @Value("${key.mcName}") String name,
                    PostmanService postman,
                    ValidationChain validation
    ) {
        this.postman = postman;
        this.name = name;
        this.repositories = repositories;
        this.validation = validation;
        this.startTest = new Button("Start", buttonClickEvent -> {
            validation.validate();
        });
        add(
                setHeader(),
                setGrid(),
                startTest
        );
        setAlignItems(Alignment.CENTER);
    }

    private Component setGrid() {
        grid.addColumn(RepoDataClass::getDatabaseName).setHeader("Database").setAutoWidth(true);
        grid.addColumn(RepoDataClass::getCollectionName).setHeader("Collection").setAutoWidth(true);
        grid.addColumn(RepoDataClass::getCountRecord).setHeader("Count chains").setAutoWidth(true);
        grid.setItems(new RepoDataClass("Chain", "test", repositories.count()));
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        return grid;
    }

    private Component setHeader() {
        Label appName = new Label("Sphinx. Testing microservice: ");
        Label mcName = new Label(name);
        HorizontalLayout header = new HorizontalLayout(appName, mcName);
        header.setAlignItems(Alignment.CENTER);
        return header;
    }

    @AllArgsConstructor
    @Getter
    public static class RepoDataClass {
        private String databaseName;
        private String collectionName;
        private long countRecord;
    }
}