package quinton.terence.eugynefamous.interceptor;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import quinton.terence.eugynefamous.BuildConfig;

public class AccessTokenInterceptor implements Interceptor {

    public AccessTokenInterceptor() {

    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        String keys = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET;

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Basic " + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP))
                .build();
        return chain.proceed(request);
    }

}
