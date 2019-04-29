package io.slingr.endpoints.ftp;

import io.slingr.endpoints.Endpoint;
import io.slingr.endpoints.exceptions.EndpointException;
import io.slingr.endpoints.exceptions.ErrorCode;
import io.slingr.endpoints.framework.annotations.EndpointFunction;
import io.slingr.endpoints.framework.annotations.EndpointProperty;
import io.slingr.endpoints.framework.annotations.SlingrEndpoint;
import io.slingr.endpoints.ftp.beans.Processor;
import io.slingr.endpoints.utils.Json;
import io.slingr.endpoints.ws.exchange.FunctionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>FTP endpoint
 *
 * <p>Created by dgaviola on 31/08/15.
 */
@SlingrEndpoint(name = "ftp")
public class FtpEndpoint extends Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(FtpEndpoint.class);

    @EndpointProperty(name = "protocol")
    private String protocol;

    @EndpointProperty(name = "host")
    private String host;

    @EndpointProperty(name = "port")
    private String port;

    @EndpointProperty(name = "username")
    private String username;

    @EndpointProperty(name = "password")
    private String password;

    @EndpointProperty(name = "filePattern")
    private String filePattern;

    @EndpointProperty(name = "inputFolder")
    private String inputFolder;

    @EndpointProperty(name = "archiveFolder")
    private String archiveFolder;

    @EndpointProperty(name = "archiveGrouping")
    private String archiveGrouping;

    @EndpointProperty(name = "outputFolder")
    private String outputFolder;

    @EndpointProperty(name = "recursive")
    private Boolean recursive;

    private Processor processor = null;

    @Override
    public void endpointStarted() {
        logger.info("Starting FTP endpoint");
        processor = new Processor(appLogs(), events(), files(), properties().getApplicationName(), properties().isLocalDeployment(),
                protocol, host, port, username, password, filePattern, inputFolder, archiveFolder, archiveGrouping,
                recursive, outputFolder);
        processor.start();
    }

    @Override
    public void endpointStopped(String cause) {
        logger.info(String.format("Stopping FTP endpoint: %s", cause));

        if(processor != null){
            processor.stop();
        }
    }

    @EndpointFunction(name = "_uploadFile")
    public void uploadFile(FunctionRequest request){
        try {
            final Json body = request.getJsonParams();
            processor.sendFile(body.string("fileId"), body.string("folder"));
        } catch (EndpointException ex){
            throw ex;
        } catch (Exception ex){
            throw EndpointException.permanent(ErrorCode.GENERAL, String.format("An exception happened in the endpoint: %s", ex.getMessage()), ex);
        }
    }
}
