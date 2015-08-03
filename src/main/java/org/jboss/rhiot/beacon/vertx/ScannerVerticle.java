package org.jboss.rhiot.beacon.vertx;

import java.net.SocketException;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.jboss.logging.Logger;
import org.jboss.rhiot.beacon.bluez.HCIDump;
import org.jboss.rhiot.beacon.common.ParseCommand;
import org.jboss.rhiot.beacon.scannerjni.HCIDumpParser;

/**
 * The
 */
public class ScannerVerticle extends AbstractVerticle {
    private static Logger log = Logger.getLogger(ScannerVerticle.class);
    private ParseCommand cmdArgs;
    private HCIDumpParser parser;

    public ScannerVerticle(ParseCommand cmdArgs) {
        this.cmdArgs = cmdArgs;
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        log.infof("Scanner initialized");
    }

    @Override
    public void start() throws Exception {
        parser = new HCIDumpParser(cmdArgs);
        log.infof("Begin start of scanner");
        // Start the scanner parser handler threads other than the native stack handler
        parser.start();
        // Setup the native bluetooth stack integration, callbacks, and stack thread
        HCIDump.setRawEventCallback(parser::beaconEvent);
        HCIDump.initScanner(cmdArgs.hciDev);
        // Schedule a thread to wait for a shutdown marker
        final Vertx vertx = getVertx();
        Handler<Future<Vertx>> shutdownMonitor = (future) -> {
            parser.waitForStop();
            log.info("Shutdown marker seen, stopping container");
            vertx.close();
            log.info("Close complete");
        };
        vertx.executeBlocking(shutdownMonitor, false, null);
        log.info("Completed start of scanner");
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopping scanner");
        parser.stop();
        log.info("End scanning");
    }

}
