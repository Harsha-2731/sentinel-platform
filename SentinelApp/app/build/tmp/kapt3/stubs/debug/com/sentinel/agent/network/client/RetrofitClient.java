package com.sentinel.agent.network.client;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u000bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J:\u0010\u0005\u001a\u00020\u00062\u0010\b\u0002\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\b2\u0010\b\u0002\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\b2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/sentinel/agent/network/client/RetrofitClient;", "", "()V", "BASE_URL", "", "createService", "Lcom/sentinel/agent/network/api/ApiService;", "getToken", "Lkotlin/Function0;", "getSecret", "getDeviceId", "AuthInterceptor", "app_debug"})
public final class RetrofitClient {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String BASE_URL = "http://192.168.137.32:8080/";
    @org.jetbrains.annotations.NotNull
    public static final com.sentinel.agent.network.client.RetrofitClient INSTANCE = null;
    
    private RetrofitClient() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.sentinel.agent.network.api.ApiService createService(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<java.lang.String> getToken, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<java.lang.String> getSecret, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<java.lang.String> getDeviceId) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B1\u0012\u000e\u0010\u0002\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0002\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/sentinel/agent/network/client/RetrofitClient$AuthInterceptor;", "Lokhttp3/Interceptor;", "getToken", "Lkotlin/Function0;", "", "getSecret", "getDeviceId", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "app_debug"})
    public static final class AuthInterceptor implements okhttp3.Interceptor {
        @org.jetbrains.annotations.NotNull
        private final kotlin.jvm.functions.Function0<java.lang.String> getToken = null;
        @org.jetbrains.annotations.NotNull
        private final kotlin.jvm.functions.Function0<java.lang.String> getSecret = null;
        @org.jetbrains.annotations.NotNull
        private final kotlin.jvm.functions.Function0<java.lang.String> getDeviceId = null;
        
        public AuthInterceptor(@org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function0<java.lang.String> getToken, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function0<java.lang.String> getSecret, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function0<java.lang.String> getDeviceId) {
            super();
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public okhttp3.Response intercept(@org.jetbrains.annotations.NotNull
        okhttp3.Interceptor.Chain chain) {
            return null;
        }
    }
}