package golhar.cocomo.zinger.seller.service;

import golhar.cocomo.zinger.seller.model.UserCollegeModel;
import golhar.cocomo.zinger.seller.model.UserModel;
import golhar.cocomo.zinger.seller.model.UserShopListModel;
import golhar.cocomo.zinger.seller.utils.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST("/user/customer")
    Call<Response<UserCollegeModel>> insertCustomer(@Body UserModel user);

    @POST("/user/seller")
    Call<Response<UserShopListModel>> insertSeller(@Body UserModel user);

    @PATCH(value = "/user/college")
    Call<Response<String>> updateUserCollegeData(@Body UserCollegeModel userCollegeModel,
                                                 @Header("oauth_id") String oauthId, @Header("mobile") String mobileRh, @Header("role") String role);

    @POST(value = "/user/seller/{mobile}/{shopId}")
    public Call<Response<String>> insertSeller(@Path("mobile") String mobile, @Path("shopId") Integer shopId,
                                               @Header("oauth_id") String oauthId, @Header("mobile") String mobileRh, @Header("role") String role);

    @PATCH(value = "/user")
    public Call<Response<String>> updateUser(@Body UserModel userModel,
                                       @Header("oauth_id") String oauthId, @Header("mobile") String mobileRh, @Header("role") String role);;

}
