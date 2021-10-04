package view.constants;

import view.Utility;

/**
 * Handles all the responses for events
 */
public class Responses {

    // FILE UPLOADING
    public static final String FILE_UPLOAD_BAD_TYPE = "File uploading failed, please upload a valid XML document.";
    public static final String FILE_DOWNLOAD_SUCCESS = "File successfully exported to " + Utility.XMLExportDir;
    public static final String FILE_UPLOAD_SUCCESS = "File successfully uploaded.";

    // LOGIN
    public static final String LOGIN_INVALID_CREDENTIALS = "Login failed. Invalid login credentials.";
    public static final String LOGIN_MALFORMED_REQUEST = "Please enter a username and a password.";
    public static final String LOGIN_FIRST_TIME_HEADER = "First Time Setup";
    public static final String LOGIN_REGULAR_HEADER = "Login";
}
