package www.sakkawy.com.hyperdesigncodingtask.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import www.sakkawy.com.hyperdesigncodingtask.Model.Product;

public interface IClient {

    @GET("products")
    Call<List<Product>> fetchDataProduct(@Query("count") int count, @Query("from") int from);

}
