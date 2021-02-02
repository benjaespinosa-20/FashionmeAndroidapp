package mx.app.fashionme.di.modules.data;

import dagger.Module;

/**
 * NetworkModule
 */
@Module
public class NetworkModule {

    /**
     * Tag for request log
     */
    private String TAG_REQUEST = "Request";

    /**
     * Read time out request
     */
    private Long READ_TIME_OUT = 60L;

    /**
     * Connection time out request
     */
    private Long CONECTION_TIME_OUT = 60L;

}
