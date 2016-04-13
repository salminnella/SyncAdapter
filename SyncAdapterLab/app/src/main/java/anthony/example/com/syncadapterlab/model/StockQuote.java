package anthony.example.com.syncadapterlab.model;

/**
 * Created by anthony on 4/13/16.
 */
public class StockQuote {

    private String Name;
    private double LastPrice;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(double lastPrice) {
        LastPrice = lastPrice;
    }
}
