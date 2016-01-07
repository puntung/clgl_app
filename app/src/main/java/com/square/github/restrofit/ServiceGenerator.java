package com.square.github.restrofit;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class ServiceGenerator {
	 private ServiceGenerator() {
	    }
	 public static <S> S createService(Class<S> serviceClass, String baseUrl) {
	        // call basic auth generator method without user and pass
	        //return createService(serviceClass, baseUrl, null, null);
	        RestAdapter.Builder builder = new RestAdapter.Builder()
	         .setEndpoint(baseUrl)
	         .setClient(new OkClient(new OkHttpClient()));
            //add Header
             builder.setRequestInterceptor(
                     new RequestInterceptor() {
                         @Override
                         public void intercept(RequestFacade request) {
                             request.addHeader("content-type","application/json");
                         }
                     }
             );
	        RestAdapter adapter = builder.build();
	    	return adapter.create(serviceClass);
		 
	    }

	    public static <S> S createService(Class<S> serviceClass, String baseUrl, String username, String password) {
	    	//Digest authentication 的验证方法
	        RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(baseUrl)
            .setClient(new MyClient(username, password));
			//add Header
			builder.setRequestInterceptor(
					new RequestInterceptor() {
						@Override
						public void intercept(RequestFacade request) {
							request.addHeader("content-type","application/json");
						}
					}
			);

	    	RestAdapter adapter = builder.build();
	    	return adapter.create(serviceClass);
	    }
	    
	    
	
}
