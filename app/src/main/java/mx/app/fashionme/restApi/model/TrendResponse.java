package mx.app.fashionme.restApi.model;

import mx.app.fashionme.pojo.Trend;

/**
 * Created by heriberto on 26/03/18.
 */

public class TrendResponse {
    private Trend trend;

    public Trend getTrend() {
        return trend;
    }

    public void setTrend(Trend trend) {
        this.trend = trend;
    }
}
