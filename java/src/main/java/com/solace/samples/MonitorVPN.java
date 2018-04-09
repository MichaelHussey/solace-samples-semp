package com.solace.samples;

import java.io.UnsupportedEncodingException;

import com.solace.labs.sempclient.monitor.api.MsgVpnApi;
import com.solace.labs.sempclient.monitor.model.MsgVpnClient;
import com.solace.labs.sempclient.monitor.model.MsgVpnClientResponse;
import com.solace.labs.sempclient.samplelib.ApiClient;
import com.solace.labs.sempclient.samplelib.ApiException;

public class MonitorVPN {
	
	String baseURI = "/SEMP/v2/monitor";
    MsgVpnApi sempApiInstance = null;

    public String getClientUsername(String clientId, String vpnName) throws ApiException, UnsupportedEncodingException {
    	
    	//String encodedClientId = URLEncoder.encode(clientId, "utf-8");
		MsgVpnClientResponse resp = sempApiInstance.getMsgVpnClient(clientId, vpnName);
		MsgVpnClient theClient = resp.getData();
		String clientUsername = theClient.getOriginalClientUsername();

    	return clientUsername;
	}
	
    public void initialize(String vmrHostPort, String user, String password) throws Exception {

        System.out.format("SEMP initializing: %s, %s \n", vmrHostPort+baseURI, user);
       
        ApiClient thisClient = new ApiClient();
        thisClient.setBasePath(vmrHostPort+baseURI);
        thisClient.setUsername(user);
        thisClient.setPassword(password);
        sempApiInstance = new MsgVpnApi(thisClient);
    }

    public static void main(String... args) throws Exception {

        // Modify these params as needed
        final String vpnUserName = "default";
        final String vpnUserPassword = "password";
        final String testQueueName = "testQueue";

        final String usage = "\nUsage: MonitorVPN <semp_base_path> <management_user> <management_password> <vpnname>" +
                "\nEx: manageVPN create <semp_base_path> <management_user> <management_password> <vpnname>" +
                "\n        Create a message-vpn and add a sample queue: testQueue" +
                "\n    manageVPN delete <semp_base_path> <management_user> <management_password> <vpnname>" +
                "\n        Delete the message-vpn";
        
        // Check command line arguments
        if (args.length < 5) {
            System.out.println(usage);
            System.exit(-1);
        }
        
        String vmrHostPort = args[0];
        String vmrUser = args[1];
        String vmrPassword = args[2];
        String messageVpnName = args[3];
        String clientId = args[4];

        MonitorVPN app = new MonitorVPN();
        
        app.initialize(vmrHostPort, vmrUser, vmrPassword);
        
        String clientUsername = app.getClientUsername(clientId, messageVpnName);
        System.out.println(clientId+"@"+messageVpnName+" => "+clientUsername);
    }

}
