package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.JourneyListAdapter;
import mx.app.fashionme.pojo.Journey;

public interface IJourneyView {

    void generateLinearLayoutJourney();

    JourneyListAdapter createAdapter(ArrayList<Journey> journeys);

    void initializeJourneyListAdapter(JourneyListAdapter adapter);

    void showEmptyList();

    void showOffline();

    void setUpToolbar();

    boolean isOnline();

    void setUpPresenterJourneyList();

    void getJourneys();

    void showError(String error);
}
