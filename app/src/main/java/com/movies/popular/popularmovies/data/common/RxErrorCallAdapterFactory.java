package com.movies.popular.popularmovies.data.common;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.movies.popular.popularmovies.exeption.APIException;
import com.movies.popular.popularmovies.exeption.BadRequestException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created with Android Studio.
 * PersonalInfo: Sasha Shcherbinin
 * Date: 4/24/17
 * Time: 9:44 AM
 */
public class RxErrorCallAdapterFactory extends CallAdapter.Factory {

    private CallAdapter.Factory originalAdapter;
    private Gson gson;

    @Inject
    public RxErrorCallAdapterFactory(Gson gson) {
        super();
        this.gson = gson;
        this.originalAdapter = RxJava2CallAdapterFactory.create();
    }

    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType,
                                 @NonNull Annotation[] annotations,
                                 @NonNull Retrofit retrofit) {
        return new CallAdapterWrapper(
                originalAdapter.get(returnType, annotations, retrofit),
                retrofit,
                returnType);
    }

    private class CallAdapterWrapper implements CallAdapter {

        private CallAdapter wrappedAdapter;
        private Retrofit retrofit;
        private Type returnType;

        private CallAdapterWrapper(CallAdapter wrappedAdapter, Retrofit retrofit, Type returnType) {
            this.wrappedAdapter = wrappedAdapter;
            this.retrofit = retrofit;
            this.returnType = returnType;
        }

        @Override
        public Type responseType() {
            return wrappedAdapter.responseType();
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object adapt(@NonNull Call call) {
            Object observable = wrappedAdapter.adapt(call);

            Class<?> rawType = getRawType(returnType);

            if (rawType == Completable.class) {
                observable = ((Completable) observable)
                        .onErrorResumeNext(throwable -> Completable.error(mapError(throwable)));
            } else if (rawType == Flowable.class) {
                observable = ((Flowable) observable)
                        .onErrorResumeNext(throwable -> {
                            return Flowable.error((Throwable) throwable);
                        });
            } else if (rawType == Single.class) {
                observable = ((Single) observable)
                        .onErrorResumeNext(throwable -> Single.error(mapError((Throwable) throwable)));
            } else if (rawType == Observable.class) {
                observable = ((Observable) observable).onErrorResumeNext(throwable -> {
                    return Observable.error(mapError((Throwable) throwable));
                });
            }

            return observable;
        }

        private Throwable mapError(Throwable throwable) {
            if (throwable instanceof HttpException) {
                int code;
                HttpException httpException = (HttpException) throwable;
                code = httpException.code();

                if (520 == code) {
                    return throwable;
                } else if (400 == code) {
                    return new BadRequestException(code, httpException.message());
                } else {
                    return new APIException(code, httpException.message());
                }
            } else {
                return throwable;
            }
        }

    }

}
