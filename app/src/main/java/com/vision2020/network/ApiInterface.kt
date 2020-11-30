package com.vision2020.network

import BaseModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.vision2020.data.request.LoginReq
import com.vision2020.data.response.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @POST("/api/ljAppLogin")
    fun userLogin(@Body userModel: LoginReq): Call<User>

    @FormUrlEncoded
    @POST("/api/ljAppCalLogin")
    fun userDistressLogin(@Field("tokenId") email: String, @Field("password") password: String): Call<CalLoginDistressResponse>

    @POST("/api/ljAppSignUp")
    fun userSignUp(@Body file: RequestBody): Call<User>

    @FormUrlEncoded
    @POST("/api/ljAppForgotPwd")
    fun userForgot(@Field("email") email: String): Call<User>

    @FormUrlEncoded
    @POST("/api/ljAppCreateGroup")
    fun createGroup(@Field("authToken") token: String, @Field("name") name: String): Call<BaseModel>

    @FormUrlEncoded
    @POST("/api/ljAppGroupListing")
    fun getListOfGroup(
        @Field("authToken") token: String,
        @Field("pageNo") page: String
    ): Call<GroupListing>

    @FormUrlEncoded
    @POST("/api/ljAppStudentListing")
    fun getStudents(
        @Field("authToken") token: String,
        @Field("pageNo") page: String
    ): Call<StudentListing>

    @FormUrlEncoded
    @POST("/api/ljAppGroupMemberListing")
    fun getGroupMembers(
        @Field("authToken") token: String,
        @Field("group_id") groupId: String
    ): Call<GroupMembers>

    @FormUrlEncoded
    @POST("/api/ljAppAddtoGroup")
    fun addLeaderToGroup(
        @Field("authToken") token: String,
        @Field("group_id") groupId: String,
        @Field("user_ids") userId: String,
        @Field("leader_id") id: String
    ): Call<BaseModel>

    @FormUrlEncoded
    @POST("/api/ljAppGroupDelete")
    fun deleteGroup(
        @Field("authToken") token: String,
        @Field("del_ids") id: String
    ): Call<BaseModel>

    @FormUrlEncoded
    @POST("/api/ljAppGroupMemberDelete")
    fun deleteMembers(
        @Field("authToken") token: String, @Field("group_id") id: String,
        @Field("student_ids") stuId: String
    ): Call<BaseModel>

    @GET("/api/ljAppDrugList")
    fun getDrugList():Call<DrugList>
    @GET("/api/ljAppDrugDossesList/{path}")
    fun getDosagesList(@Path("path", encoded = true)id:String)

    @GET("/api/ljAppViewProfile/{path}")
    fun getUserData(@Path("path", encoded = true)token: String): Call<ProfileResponse>

    @GET("/api/ljAppdistressGroupList/{path}")
    fun getdistressGroupList(@Path("path", encoded = true)token: String): Call<DistressGroupDataListing>


    @POST("/api/ljAppUpdateProfile")
    fun userEditProfile(@Body file: RequestBody): Call<UpdateProfileResponse>

  //  @POST("/api/ljAppdistressExperientSetup")
    //fun postRawJSON(@QueryMap params: Map<String, String>): Call<JsonArray>

    @Headers("Content-Type: application/json")
    @POST("/api/ljAppdistressExperientSetup")
    fun postRawJSON(@Body jsonArray: JsonObject): Call<DistressExpResponse>


    @FormUrlEncoded
    @POST("/api/ljAppfetchexperientResult")
    fun fetchExpResult(@Field("authToken") authToken:String,@Field("calDate") calDate:String): Call<ExpResultResponse>


    @FormUrlEncoded
    @POST("/api/ljAppcalculateExpResultByDate")
    fun fetchExpResultbyDate(@Field("authToken") authToken:String,@Field("selectedDate") calDate:String,@Field("expId") expId:String): Call<ExpCalcResponse>


    @FormUrlEncoded
    @POST("/api/ljAppcalculateExpResultByWeek")
    fun fetchExpResultbyWeek(@Field("authToken") authToken:String,@Field("selectedDate") calDate:String,@Field("expId") expId:String): Call<ExpCalWeekResponse>


    @Headers("Content-Type: application/json")
    @POST("/api/ljAppexperientResult")
    fun postExpRawJSON(@Body jsonArray: JsonObject): Call<ExpResultEditResponse>


    // fun postRawJSON(@Body jsonArray: JSONObject): Call<JsonArray>
  //  fun postRawJSON(@Body body: JSONObject): Call<Any?>?

    /*// Get current weather data
@GET("api/weather/griddata")
fun getCurrentLocWeatherForecast(@Query("lat")lat:String,@Query("lon")long:String,
                             @Query("s_date")startDate:String,@Query("e_date")e_date:String):Call<Weather>
// Land Weather Data
@GET("api/weather/all")
fun getLandWeatherData(@Query("lat")lat:String,@Query("lon")long:String,
                    @Query("s_date")startDate:String,@Query("e_date")e_date:String):Call<LandWeather>

// user register
@POST("api/users/register")
fun saveUser(@Body userModel : UserReqModel): Call<UserRespModel>
// user login
@POST("api/users/login")
fun userLogin(@Body userModel: LoginModel):Call<UserRespModel>
// Get Crop List
@GET("api/agriadvisory/crops")
fun getCropList():Call<CropList>
// Advisory List
@GET("api/agriadvisory/{path}")
fun getAdvisoryList(@Path(value="path", encoded=true)name:String):Call<AdvisoryModel>
// Add land
@POST("api/users/farm")
fun addFarmerLand(@Body model: ArrayList<LandInfo>):Call<LandResp>
// Get Land
@GET("api/users/farm")
fun getUserLand(@Query("user_id") userId:String):Call<LandResp>
// planting date
@GET("api/agriadvisory/{path}")
fun getPlantingAdvisory(@Path(value="path", encoded=true)name:String,@Query("lat")lat:String,@Query("lon")long:String,
                             @Query("planting_date")date:String):Call<PlantingModel>
@GET("api/agriadvisory/{path}")
fun getIndoBlightAdvisory(@Path(value="path", encoded=true)name:String,@Query("lat")lat:String,
                      @Query("lon")long:String,@Query("ref_date")date:String):Call<IndoBlightModel>
@GET("api/agriadvisory/{path}")
fun getYieldPrediction(@Path(value="path", encoded=true)name: String,@Query("lat")lat:String,
                   @Query("lon")lang:String,@Query("cs_date")cPlantingDate:String,@Query("ce_date")cHarvestingDate:String,
                   @Query("rs_date")pPlantingDate:String,@Query("re_date")pHarvestingDate:String):Call<YieldResult>

*//*@FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("emailAddress") email: String,
              @Field("password") password: String,
              @Field("roleId") rollId: Int): Call<UserModel>

    @FormUrlEncoded
    @POST("/user/listBySchool")
    fun teacherListBySchool(@Field("school") school: String): Call<TeacherModel>

    @POST("/user/register")
    fun register(@Body userModel: UserModel.DataBeanRequest): Call<UserModel>

    @POST("/user/updateUserInfo")
    fun updateUserInfo(@Body userModel: UserModel.DataBeanRequest): Call<UserModel>

    // Get School List
    @GET("school/list")
    fun getSchoolList(): Call<SchoolModel>

    // Get child List
    @FormUrlEncoded
    @POST("/student/listByParent")
    fun getChildList(@Field("_id") _id: String): Call<ChildModel>

    // edit child
    @POST("/student/updateChildbyParent")
    fun updateChildbyParent(@Body child: ChildModel.DataBeanRequest): Call<BaseModel>

    // add child
    @POST("/student/addChildbyParent")
    fun addChildbyParent(@Body child: ChildModel.DataBeanRequest): Call<BaseModel>

    // get class mDialoglist by teacher
    @FormUrlEncoded
    @POST("/student/listChildbyTeacher")
    fun getClassByTeacher(@Field("_id") id: String): Call<ClassModel>

    // add student manually by teacher
    @POST("/student/addStudentbyTeacher")
    fun addStudentByTeacher(@Body child: AddStudentManullyRequest
    ): Call<BaseModel>

    //    @GET("/survey/list2")
    @GET("/survey/list")
    fun getSurveyList(): Call<SurveysModel>

    @FormUrlEncoded
    @POST("/student/getSchoolListforSurvey")
    fun getSchoolListforSurvey(@Field("_id") _id: String): Call<SurveySchoolModel>

    @POST("/survey/saveSurvey")
    fun saveSurvey(@Body child: SurveysModel.DataBeanRequest): Call<BaseModel>

    @FormUrlEncoded
    @POST("/student/listChildbyTeacherCSV")
    fun getCSVFile(@Field("_id") _id: String): Call<ExportCsvModel>

    @FormUrlEncoded
    @POST("/message/list")
    fun getMessageList(@Field("_id") _id: String,
                       @Field("isfilter") isfilter: Boolean): Call<MessagesModel>

    @POST("/message/create")
    fun createMessage(@Body file: RequestBody):
            Call<MessagesModel>

    // get single user message
    @FormUrlEncoded
    @POST("/message/find")
    fun getMessageForSingleUser(@Field("from") from: String,
                                @Field("to") to: String): Call<MessagesModel>

    // delete account
    @FormUrlEncoded
    @POST("user/deleteAccount")
    fun deleteAccount(@Field("_id") id: String): Call<BaseModel>

    // get childParent List
    @FormUrlEncoded
    @POST("user/listBySchoolForMessage")
    fun getChildParentList(@Field("_id") id: String): Call<ChildTeacherModel>

//    //  get list with score
//    @GET("school/listWithScore")
//    fun getSchoolWithScore(): Call<SchoolScoreListModel>

    //  get list with score
    @POST("school/searchSchool")
    fun getSchoolWithScore(@Body model: SchoolFilterModel.DataBean): Call<SchoolScoreListModel>

    @FormUrlEncoded
    @POST("school/schoolDepartment")
    fun schoolDepartment(@Field("code") code: String): Call<SupervisorFilterExportModel>

    // get School Info
    @FormUrlEncoded
    @POST("school/listSchoolWithTeacherParentSurvey")
    fun getSchoolInfoData(@Field("_id") id: String): Call<SchoolExpandInfoModel>

    @FormUrlEncoded
    @POST("/school/downloadSchoolCSV")
    fun downloadSchoolCSV(@Field("department") department: ArrayList<String>,
                          @Field("location") location: ArrayList<String>): Call<ExportCsvModel>

    @POST("/school/getSchoolFilterOptions")
    fun getSchoolFilterOptions(): Call<SchoolFilterModel>

    // get Gallery List


    @POST("/message/images")
    fun getGalleryImg():Call<GalleryModel>*//*


//    @FormUrlEncoded
//    @POST("users/authenticate")
//    fun authenticate_user(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<UserModel>
//
//
//    @FormUrlEncoded
//    @POST("users/current-user")
//    fun current_user(@Header("Authorization") authorization: String): Call<UserModel>
//
//
//    @FormUrlEncoded
//    @POST("users/confirm-registration")
//    fun confirm_registration(@Field("token") token: String): Call<UserModel>
//
//
//    @FormUrlEncoded
//    @POST("users/reset-password?")
//    fun reset_password(
//        @Field("resetToken") email: String,
//        @Field("password") password: String
//    ): Call<UserModel>
//
//
//    @FormUrlEncoded
//    @POST("users/forgot-password")
//    fun forgot_password(@Field("email") email: String): Call<UserModel>


    //    @GET
    //    public Call<GooglePlaceModal> getGooglePlaces(@Url String url);
    //
    //    @GET
    //    public Call<NearbyPlaceModel> getGoogleNearByPlaces(@Url String url);
    //                                @Field("profile_pic") String profile_pic);

    //
    //    @FormUrlEncoded
    //    @POST("get_top_deal")
    //    Call<DealModel> get_top_deal(@Field("access_token") String access_token,
    //                                 @Field("longitude") String longitude,
    //                                 @Field("latitude") String latitude);
    //
    //    @Multipart
    //    @POST("create_profile")
    //    Call<UserModel> create_profile(@Part("name") RequestBody name,
    //                                   @Part("access_token") RequestBody access_token,
    //                                   @Part("email") RequestBody email,
    //                                   @Part MultipartBody.Part profile_pic);
    //
    //    @FormUrlEncoded
    //    @POST("get_brands")
    //    Call<FilterModel> get_brands(@Field("access_token") String access_token);
    //
    //    @FormUrlEncoded
    //    @POST("deal_detail_by_id")
    //    Call<DealModel> deal_detail_by_id(@Field("access_token") String access_token,
    //                                      @Field("deal_id") String deal_id);
    //
    //    @GET("get_category")
    //    Call<CategoryModel> get_category();
    //    @FormUrlEncoded
    //    @PUT("/api/user/getUserDocuments")
    //    Call<ActsModel> getActsUserDocuments(@Field("category") String category);
    //
    //    @FormUrlEncoded
    //    @PUT("/api/user/getUserDocuments")
    //    Call<BillsModel> getBillsUserDocuments(@Field("category") String category);

    //    @FormUrlEncoded
    //    @PUT("/api/user/sendEmail")
    //    Call<SenatorDetailModel> sendEmail(@Field("senator") String senator,
    //                                       @Field("message") String message,
    //                                       @Field("NAME") String NAME,
    //                                       @Field("EMAIL") String EMAIL);

*/
}