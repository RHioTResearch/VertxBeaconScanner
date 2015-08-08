package org.jboss.rhiot.beacon.vertx;

import java.net.SocketException;
import java.util.function.Consumer;

import io.vertx.core.Context;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.jboss.logging.Logger;
import org.jboss.rhiot.beacon.common.ParseCommand;
import org.jboss.rhiot.beacon.scannerjni.HCIDumpParser;

/**
 * Created by starksm on 8/2/15.
 */
public class ScannerMain {
    private static Logger log = Logger.getLogger(ScannerMain.class);

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        options.setClustered(false);
        options.setEventLoopPoolSize(5);
        Vertx vertx = Vertx.vertx(options);
        Context context = vertx.getOrCreateContext();

        ParseCommand cmdArgs = ParseCommand.parseArgs(args);
        // Exit if help option was given. The help will be output by parseArgs.
        if(cmdArgs.help)
            System.exit(0);
        context.put(ParseCommand.class.getCanonicalName(), cmdArgs);

        // If scannerID is the string {IP}, replace it with the host IP address
        try {
            cmdArgs.replaceScannerID();
        } catch (SocketException e) {
            log.warn("Failed to read host address info", e);
        }
        log.infof("Parsed command line args, %s", cmdArgs);

        System.loadLibrary("scannerJni");
        log.info("Loaded native scannerJni library");

        // Remove any existing stop marker file
        HCIDumpParser.removeStopMarker();

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        ScannerVerticle verticle = new ScannerVerticle(cmdArgs);
        vertx.deployVerticle(verticle, deploymentOptions);
    }
}
