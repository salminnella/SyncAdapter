package anthony.example.com.syncadapterlab.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by anthony on 4/13/16.
 */
public interface StockAPIService {

    //http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol=NFLX

    @GET("?symbol={company}")
    Call<StockQuote> getNetFlixPrice(@Path("company") String companSymbol);



}
