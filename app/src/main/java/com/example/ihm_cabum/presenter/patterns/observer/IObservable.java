package com.example.ihm_cabum.presenter.patterns.observer;

import com.example.ihm_cabum.presenter.home.presenter.MarkersPresenter;

public interface IObservable {
    void update(MarkersPresenter markersPresenter);
}
