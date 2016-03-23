package com.ttndigital.cms;

import java.util.Map;

/**
 *  Interface for email services
 */
public interface EmailServer {
    public void sendMail(Map<String,String> emailParams);

}
