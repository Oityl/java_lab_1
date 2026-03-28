package com.company.parser;

import com.company.model.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Парсер JSON-файлов миссий
 */
public class JsonMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

        content = content.trim();
        if (content.startsWith("\uFEFF")) {
            content = content.substring(1);
        }

        Mission mission = new Mission();
        Curse curse = new Curse();
        List<Sorcerer> sorcerers = new ArrayList<>();
        List<Technique> techniques = new ArrayList<>();

        mission.setMissionId(extractString(content, "missionId"));
        mission.setDate(extractString(content, "date"));
        mission.setLocation(extractString(content, "location"));
        mission.setDamageCost(extractLong(content, "damageCost"));
        mission.setComment(extractString(content, "comment"));

        String outcomeStr = extractString(content, "outcome");
        if (outcomeStr != null) {
            mission.setOutcome(Outcome.fromString(outcomeStr));
        }

        String curseBlock = extractObject(content, "curse");
        if (curseBlock != null) {
            curse.setName(extractString(curseBlock, "name"));
            String threatStr = extractString(curseBlock, "threatLevel");
            if (threatStr != null) {
                curse.setThreatLevel(ThreatLevel.fromString(threatStr));
            }
        }
        mission.setCurse(curse);

        String sorcerersArray = extractArray(content, "sorcerers");
        if (sorcerersArray != null) {
            List<String> items = splitArrayObjects(sorcerersArray);
            for (String item : items) {
                Sorcerer s = new Sorcerer();
                s.setName(extractString(item, "name"));
                String rankStr = extractString(item, "rank");
                if (rankStr != null) {
                    s.setRank(SorcererRank.fromString(rankStr));
                }
                sorcerers.add(s);
            }
        }
        mission.setSorcerers(sorcerers);

        String techArray = extractArray(content, "techniques");
        if (techArray != null) {
            List<String> items = splitArrayObjects(techArray);
            for (String item : items) {
                Technique t = new Technique();
                t.setName(extractString(item, "name"));
                String typeStr = extractString(item, "type");
                if (typeStr != null) {
                    t.setType(TechniqueType.fromString(typeStr));
                }
                t.setOwner(extractString(item, "owner"));
                t.setDamage(extractLong(item, "damage"));
                techniques.add(t);
            }
        }
        mission.setTechniques(techniques);

        return mission;
    }

    private String extractString(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return null;

        int colonIdx = json.indexOf(':', idx + pattern.length());
        if (colonIdx == -1) return null;

        int start = json.indexOf('"', colonIdx + 1);
        if (start == -1) return null;

        int end = start + 1;
        while (end < json.length()) {
            if (json.charAt(end) == '\\') {
                end += 2;
            } else if (json.charAt(end) == '"') {
                break;
            } else {
                end++;
            }
        }
        if (end >= json.length()) return null;

        return json.substring(start + 1, end);
    }

    private long extractLong(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return 0;

        int colonIdx = json.indexOf(':', idx + pattern.length());
        if (colonIdx == -1) return 0;

        int start = colonIdx + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        StringBuilder sb = new StringBuilder();
        while (start < json.length()
                && (Character.isDigit(json.charAt(start)) || json.charAt(start) == '-')) {
            sb.append(json.charAt(start));
            start++;
        }

        if (sb.length() == 0) return 0;
        return Long.parseLong(sb.toString());
    }

    private String extractObject(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return null;

        int braceStart = json.indexOf('{', idx + pattern.length());
        if (braceStart == -1) return null;

        int depth = 0;
        for (int i = braceStart; i < json.length(); i++) {
            if (json.charAt(i) == '{') depth++;
            else if (json.charAt(i) == '}') depth--;
            if (depth == 0) {
                return json.substring(braceStart, i + 1);
            }
        }
        return null;
    }

    private String extractArray(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return null;

        int bracketStart = json.indexOf('[', idx + pattern.length());
        if (bracketStart == -1) return null;

        int depth = 0;
        for (int i = bracketStart; i < json.length(); i++) {
            if (json.charAt(i) == '[') depth++;
            else if (json.charAt(i) == ']') depth--;
            if (depth == 0) {
                return json.substring(bracketStart + 1, i);
            }
        }
        return null;
    }

    private List<String> splitArrayObjects(String arrayContent) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;

        for (int i = 0; i < arrayContent.length(); i++) {
            char c = arrayContent.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    objects.add(arrayContent.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        return objects;
    }
}
