# VertxBeaconScanner
A version of the BLE beacon scanner that uses the Vert.x framework

## Building
First clone and build the native scanner integration. Its README.md documents the required package installations.
https://github.com/RHioTResearch/BeaconScannerJNI

Optionally, if you have an LCD display attached to the scanner and what to use it, clone and install the LCDDisplays project.
https://github.com/RHioTResearch/LCDDisplays.git

Next, clone and install the BaseBeaconScanner jars
https://github.com/RHioTResearch/BaseBeaconScanner

Next, clone and package the VertxBeaconScanner
https://github.com/RHioTResearch/VertxBeaconScanner

## Running
Finally, run the scanner using target/VertxScanner-1.0.0-SNAPSHOT.jar fat jar produced by the package phase. A typical command
line is something like:

	java -Djava.library.path=/usr/local/lib -jar target/VertxScanner-1.0.0-SNAPSHOT.jar -username demo-user -password 2015-summit-user -brokerURL amqp://192.168.1.107:5672

Alternatively, you can use a scanner.conf file in either the current directory, or the user home directory and place properties
there. An example scanner.conf is:

	root@pi2-devzone:~/VertxBeaconScanner# cat ../scanner.conf 
	scannerID={IP}
	heartbeatUUID=DAF246CEF20111E4B116123B93F75CBA
	brokerURL=amqp://192.168.1.107:5672
	#brokerURL=52.10.252.216:5672
	#brokerURL=184.72.167.147:5672
	username=demo-user
	password=2015-summit-user
	beaconMapping=301=Scott,300=Tony,303=Anthony

This would be used via a command line like:

	root@pi2-devzone:~/VertxBeaconScanner# java -Djava.library.path=/usr/local/lib -jar target/VertxScanner-1.0.0-SNAPSHOT.jar -useScannerConf

### Help
The current command arguments are:

		java -jar target/VertxScanner-1.0.0-SNAPSHOT.jar -help
		Usage: <main class> [options]
		Options:
			-analyzeWindow
				 Specify the number of seconds in the analyzeMode time window, default is
				 1.
				 Default: 1
			-analzyeMode
				 Run the scanner in a mode that simply collects beacon readings and
				 reports unique beacons seen in a time window
				 Default: false
			-asyncMode
				 Indicate that the parsed beacons should be published using async delivery
				 mode
				 Default: false
			-batchCount
				 Specify a maxium number of events the scanner should combine before
				 sending to broker; default 0 means no batching
				 Default: 0
			-batteryTestMode
				 Simply monitor the raw heartbeat beacon events and publish them to the
				 destinationName
				 Default: false
			-bcastAddress
				 Address to broadcast scanner status to as backup to statusQueue if
				 non-empty; default empty
			-bcastPort
				 Port to broadcast scanner status to as backup to statusQueue if
				 non-empty; default 12345
				 Default: 12345
			-beaconMapping
				 Specify the source of the beacon id to user name mapping. The following
				 formats are understood:
						string of id1=user1,id2=user2,... mappingsfile:path to
									 java properties file with beaconid=userid mappings
			-brokerURL
				 Specify the brokerURL to connect to the msg broker with; default
				 tcp://localhost:1883
				 Default: tcp://localhost:1883
			-clientID
				 Specify the clientID to connect to the msg broker with
				 Default: <empty string>
			-destinationName
				 Specify the name of the destination on the msg broker to publish to;
				 default beaconEvents
				 Default: beaconEvents
			-hciDev
				 Specify the name of the host controller interface to use; default hci0
				 Default: hci0
			-heartbeatUUID
				 Specify the UUID of the beacon used to signal the scanner heartbeat event
				 Default: <empty string>
			-help, -h, -?
				 
				 Default: false
			-lcdType
				 Specify the LcdDisplayType enum for the LCD implementation to use;
				 default PCD8544
				 Default: PCD8544
			-noBrokerReconnect
				 Don't try to reconnect to the broker on failure, just exit
				 Default: false
			-noParsing
				 Indicate that the hcidump stream should not be parsed, just made
				 available
				 Default: false
			-password
				 Specify the sshPassword to connect to the msg broker with
			-pubType
				 Specify the MsgPublisherType enum for the publisher implementation to
				 use; default AMQP_QPID
				 Default: AMQP_QPID
			-scannerID
				 Specify the ID of the scanner reading the beacon events. If this is a
				 string with a comma separated list of names, the scanner will cycle through
				 them. If this is the string {IP}, the host ip address will be used.
				 Default: {IP}
			-skipHeartbeat
				 Don't publish the heartbeat messages. Useful to limit the noise when
				 testing the scanner.
				 Default: false
			-skipPublish
				 Indicate that the parsed beacons should not be published
				 Default: false
			-skipScannerView
				 Skip the scanner view display of closest beacon
				 Default: false
			-statusDestinationName
				 Specify the name of the status health destination; default scannerHealth
				 Default: scannerHealth
			-statusInterval
				 Specify the interval in seconds between health status messages, <= 0
				 means no messages; default 30
				 Default: 30
			-useQueues
				 Specify whether destination is a queue; default false == destination is a
				 topic
				 Default: false
			-useScannerConf
				 Load the scanner.conf file and populate the command line args from it.
				 Searches current dir and home dir.
				 Default: false
			-username
				 Specify the sshUsername to connect to the msg broker with