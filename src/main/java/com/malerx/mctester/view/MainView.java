package com.malerx.mctester.view;

import com.malerx.mctester.model.Chain;
import com.malerx.mctester.repositories.MongoTestDataRepositories;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PageTitle("Sphinx. Testing microservice.")
public class MainView extends VerticalLayout {
    private final MongoTestDataRepositories repositories;
    private Grid<Chain> grid = new Grid<>(Chain.class, true);

    @Autowired
    public MainView(MongoTestDataRepositories repositories) {
        this.repositories = repositories;
        configureGrid();
        grid.setItems(repositories.findAll());
        add(grid);
    }

    private void configureGrid() {
        addClassName("main-view");
        setSizeFull();
    }
}