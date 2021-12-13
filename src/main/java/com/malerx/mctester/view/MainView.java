package com.malerx.mctester.view;

import com.malerx.mctester.repositories.MongoTestDataRepositories;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

@Route("")
@RequiredArgsConstructor
public class MainView extends VerticalLayout {
    private final MongoTestDataRepositories repositories;

}