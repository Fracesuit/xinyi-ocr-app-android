package com.xinyi_tech.comm.util;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class MultipartUtil {

    public static MultipartBody createImageMultipartBody(String filepath) {
        File file = new File(filepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        return new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), requestFile)
                .build();
    }

    public static MultipartBody createFileMultipartBody(String filepath, String mediaType) {
        File file = new File(filepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse(mediaType), file);
        return new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), requestFile)
                .build();
    }

    /**
     * @Multipart
     @POST("upload") Call<ResponseBody> upload(@Part("description") RequestBody description,@Part MultipartBody.Part file);


     // use the FileUtils to get the actual file by uri
     File file = FileUtils.getFile(this, fileUri);

     // create RequestBody instance from file
     RequestBody requestFile =
     RequestBody.create(MediaType.parse("multipart/form-data"), file);

     // MultipartBody.Part is used to send also the actual file name
     MultipartBody.Part body =
     MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

     // add another part within the multipart request
     String descriptionString = "hello, this is description speaking";
     RequestBody description =
     RequestBody.create(
     MediaType.parse("multipart/form-data"), descriptionString);

     */

}
/**
 * 第二种
 * public static MultipartBody createMultipartBody(String filepath) {
 * if (!ImageUtils.isImage(filepath)) {
 * throw new RuntimeException("不是图片格式");
 * }
 * File file = new File(filepath);
 * RequestBody requestFile = createRequestBody(filepath);
 * MultipartBody.Builder builder = new MultipartBody.Builder();
 * MultipartBody multipartBody = builder.addFormDataPart("file", file.getName(), requestFile).build();
 * return multipartBody;
 * }
 * <p>
 * public static RequestBody createRequestBody(String filepath) {
 * if (!ImageUtils.isImage(filepath)) {
 * throw new RuntimeException("不是图片格式");
 * }
 * File file = new File(filepath);
 * RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
 * return requestFile;
 * }
 * <p>
 * public static RequestBody createStringValue(String string) {
 * if (StringUtils2.isEmpty(string)) return null;
 * return RequestBody.create(MediaType.parse("multipart/form-data"), string);
 * <p>
 *
 * @Multipart
 * @POST("/spmj/landlord/upload-image") Observable<String> uploadFile(@Part("file") RequestBody file);
 * }
 */
