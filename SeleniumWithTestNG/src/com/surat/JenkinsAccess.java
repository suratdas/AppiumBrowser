package com.surat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;


/**
 * Created by surat_das on 10/18/2017.
 */
public class JenkinsAccess {
    private static List<String> pathList = new ArrayList<>();
    private static List<String> actionList = new ArrayList<>();
    private static List<String> pathPlusFileNamesAfterRemovingWorkspaceFolder = new ArrayList<>();
    private static String fullLogForThisCommand = "";


    public static void main(String[] args) {

        System.out.println("Parameter list of this program : currentJenkinsBuildUrl JenkinsUsername JenkinsPwd sourceCodePathBeingMonitored robocopyCommand pathToCopy");

        List<String> serverList = new ArrayList();
        for (String arg : args) serverList.add(arg);

        String currentJenkinsBuildURL = serverList.get(0);
        serverList.remove(0);
        String jenkinsUsername = serverList.get(0);
        serverList.remove(0);
        String jenkinsPassword = serverList.get(0);
        serverList.remove(0);
        String sourceCodePath = serverList.get(0);
        serverList.remove(0);
        String robocopyCommand = serverList.get(0);
        serverList.remove(0);

        String jenkinsURLWithCurrentBuildChangeset = currentJenkinsBuildURL + "api/json?pretty=true&tree=changeSet[items[items[path,action]]]";
        String commandString = "";
        String pathOnly = "";
        String fileName = "";


        String jsonBody = connectToWebsiteAndReturnHTMLBody(jenkinsURLWithCurrentBuildChangeset, jenkinsUsername, jenkinsPassword);
        populatePathAndActionList(jsonBody);
        makePathRelativeTo(sourceCodePath);

        int countItems = -1;
        boolean anyFailedStep = false;

        for (String pathPlusFileName : pathPlusFileNamesAfterRemovingWorkspaceFolder) {
            fileName = "";
            ++countItems;
            for (String destination : serverList) {
                commandString = "";
                fullLogForThisCommand = "";
                pathOnly = "";
                destination = (destination.endsWith("\\") && pathOnly.trim().length() > 0) ? destination : (destination + "\\");
                if (actionList.get(countItems).startsWith("delete")) {
                    if (pathPlusFileName.contains("."))
                        commandString = "del /f /q " + destination + pathPlusFileName;
                    else
                        commandString = "rmdir /s /q " + destination + pathPlusFileName;
                } else {
                    if (pathPlusFileName.contains(".")) {
                        int lastIndexOfFileNameStart = pathPlusFileName.lastIndexOf("\\");
                        pathOnly = pathPlusFileName.substring(0, lastIndexOfFileNameStart);
                        fileName = pathPlusFileName.substring(lastIndexOfFileNameStart + 1, pathPlusFileName.length());
                        commandString = robocopyCommand + " " + pathOnly + " " + destination + pathOnly + " " + fileName;
                    } else continue;
                }
                fullLogForThisCommand = "";
                int exitCode = getCommandLineExecutionDetails(commandString);
                if (shouldIgnoreDeleteError(commandString) || shouldIgnoreRobocopyError(commandString)) {
                    System.out.println("\n===Info===\n" + commandString + "\n" + fullLogForThisCommand + "\n===EndInfo===");
                    continue;
                }
                if ((exitCode == 0 || exitCode == 1 || exitCode == 3) && commandString.startsWith("robocopy")) {
                    continue;
                }
                if (exitCode > 1 && commandString.startsWith("robocopy")) {
                    System.out.println("===Error===\nThis command threw error with exit code:" + exitCode + "\n" + commandString + "\n" + fullLogForThisCommand + "\n===EndError===");
                    if (fullLogForThisCommand.trim().length() == 0 && commandString.contains("robocopy")) {
                        System.out.println("===Info===\nAnother attempt with a different command");
                        commandString = "xcopy " + pathPlusFileName + " " + destination + pathPlusFileName + " /e /y /i";
                        exitCode = getCommandLineExecutionDetails(commandString);
                        System.out.println("===EndInfo===\n");
                    }
                }
                if (exitCode != 0) {
                    System.out.println("===Error===\nThis command threw error with exit code:" + exitCode + "\n" + commandString + "\n" + fullLogForThisCommand + "\n===EndError===");
                    anyFailedStep = true;
                }
            }
        }
        if (anyFailedStep) System.exit(1);
    }

    private static boolean shouldIgnoreRobocopyError(String commandString) {
        if (commandString.startsWith("robocopy"))
            return fullLogForThisCommand.contains("Accessing Source Directory");
        return false;
    }

    private static boolean shouldIgnoreDeleteError(String commandString) {
        if (commandString.startsWith("rmdir") || commandString.startsWith("del"))
            return (fullLogForThisCommand.startsWith("The system cannot find the file specified") || fullLogForThisCommand.startsWith("Could Not Find") || fullLogForThisCommand.startsWith("The system cannot find the path specified"));
        return false;

    }

    private static int getCommandLineExecutionDetails(String commandString) {
        System.out.println(commandString);
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", commandString);
        processBuilder.redirectErrorStream(true);
        Process process = null;
        try {
            process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null)
                    break;
                if (line.length() > 0) System.out.println(line);
                fullLogForThisCommand += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return process.exitValue();
    }

    private static void makePathRelativeTo(String sourceCodePath) {
        for (String path : pathList) {
            String replacedString = path.replace(sourceCodePath, "");
            if (replacedString.startsWith("/"))
                replacedString = replacedString.replaceFirst("/", "");
            replacedString = replacedString.replace("/", "\\");
            pathPlusFileNamesAfterRemovingWorkspaceFolder.add(replacedString);
        }
    }

    private static void populatePathAndActionList(String jsonBody) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(jsonBody);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = (JSONArray) ((JSONObject) jsonObject.get("changeSet")).get("items");
        Iterator<JSONObject> firstIterator = jsonArray.iterator();
        while (firstIterator.hasNext()) {
            JSONObject currentChangeSet = (JSONObject) firstIterator.next();
            JSONArray jsonArraySecondLevel = (JSONArray) (currentChangeSet.get("items"));
            Iterator<JSONObject> jsonObjectIterator = jsonArraySecondLevel.iterator();
            while (jsonObjectIterator.hasNext()) {
                jsonObject = (JSONObject) jsonObjectIterator.next();
                actionList.add(jsonObject.get("action").toString());
                pathList.add(jsonObject.get("path").toString());
            }
        }
    }

    private static String connectToWebsiteAndReturnHTMLBody(String webPage, String name, String password) {
        String authString = name + ":" + password;
        byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        try {
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
