package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import static org.assertj.core.api.Assertions.*;

class IndexControllerTest {

    @Test
    public void whenRequestIndexPageThenGetPage() {
        IndexController indexController = new IndexController();
        var model = new ConcurrentModel();
        var view = indexController.getIndex(model);
        assertThat(view).isEqualTo("index");
    }

}