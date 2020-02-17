import download.CloudStorageClient;
import download.MongoDBClient;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.log4j.PropertyConfigurator;
import service.LogService;
import service.VerificationService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

            if (cloudStorageClient.isCompletedVerification()) {
                verificationService.verify();
            }
            //upload report to AWS
            cloudStorageClient.uploadReportLogToAws(logService.getLogFileName());

        } catch (IOException e) {
            log.error(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }


    private static Properties loadProperties() throws IOException {
        File fileApp = new File(PROPS_PATH + "application.properties");
        InputStream appPath;
        if (fileApp.exists()) {
            appPath = new FileInputStream(fileApp.getPath());
        } else {
            appPath = Main.class.getResourceAsStream("/application.properties");
        }
        log.info("appPath = " + appPath);
        Properties appProps = new Properties();
        appProps.load(appPath);

        File fileLog = new File(PROPS_PATH + "log4j.properties");
        InputStream logPath;
        if (fileLog.exists()) {
            logPath = new FileInputStream(fileLog.getPath());
        } else {
            logPath = Main.class.getResourceAsStream("/log4j.properties");
        }
        Properties logProps = new Properties();
        logProps.load(logPath);
        PropertyConfigurator.configure(logProps);
        log.info("(Additional info) AppProps ready");
        return appProps;
    }

}
