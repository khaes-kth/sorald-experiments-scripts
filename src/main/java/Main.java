import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        String mode = args[0];

        if (mode.equals("--mined-violations-to-commands-info")) {
            minedViolationToExecution(args[1], args[2]);
        }

        System.out.println(args[0]);
    }

    private static void minedViolationToExecution(String inputDirPath, String outputPath)
            throws IOException, ParseException {
        File inputDir = new File(inputDirPath);
        PrintWriter pw = new PrintWriter(outputPath);

        File[] minedReports = inputDir.listFiles();
        for(File minedReport : minedReports){
            JSONParser parser = new JSONParser();
            JSONObject report = (JSONObject) parser.parse(new FileReader(minedReport));

            JSONArray minedRules = (JSONArray) report.get("minedRules");
            for(int i = 0; i < minedRules.size(); i++){
                JSONObject minedRule = (JSONObject) minedRules.get(i);
                String ruleKey = (String) minedRule.get("ruleKey");

                JSONArray locations = (JSONArray) minedRule.get("warningLocations");
                for(int j = 0; j < locations.size(); j++){
                    JSONObject location = (JSONObject) locations.get(j);
                    String usableLocation = (String) location.get("usableLocation");

                    pw.println(ruleKey + "," + usableLocation + "," + minedReport.getName());
                }
            }
        }

        pw.flush();
        pw.close();
    }
}
