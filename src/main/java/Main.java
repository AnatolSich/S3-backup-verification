import download.CloudStorageClient;
import download.MongoDBClient;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.log4j.PropertyConfigurator;
import service.LogService;
import service.VerificationService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;


@SuppressWarnings("WeakerAccess")

@CommonsLog
public class Main {
    private static final String PROPS_PATH = "/s3_backup_verification/configs/";

    public static void main(String[] args) {

        try {
            Properties appProps = loadProperties();

            MongoDBClient mongoDBClient = new MongoDBClient(appProps);
            LogService logService = new LogService();
            CloudStorageClient cloudStorageClient = new CloudStorageClient(appProps);

            VerificationService verificationService = new VerificationService(mongoDBClient, cloudStorageClient, logService);

            log.info("IS COMPLETED: " + cloudStorageClient.isCompletedVerification());
            verificationService.verify();
            //upload report to AWS
            cloudStorageClient.uploadReportLogToAws(logService.getLogFileName());
        } catch (IOException e) {
            log.error(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }


    private static Properties loadProperties() throws IOException {
        File fileApp = new File(PROPS_PATH + "application.properties");
        String appPath;
        if (fileApp.exists()) {
            appPath = fileApp.getPath();
        } else {
            appPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("application.properties")).getPath();
        }
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appPath));

        File fileLog = new File(PROPS_PATH + "log4j.properties");
        String logPath;
        if (fileLog.exists()) {
            logPath = fileLog.getPath();
            Properties logProps = new Properties();
            logProps.load(new FileInputStream(logPath));
            PropertyConfigurator.configure(logProps);
        }
        log.info("(Additional info) AppProps ready");
        return appProps;
    }

}
