package com.company.parser;

import com.company.model.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Парсер TXT-файлов миссий
 */
public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws Exception {
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        Map<String, String> data = new LinkedHashMap<>();
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            int colonIdx = line.indexOf(':');
            if (colonIdx == -1) continue;

            String key = line.substring(0, colonIdx).trim();
            String value = line.substring(colonIdx + 1).trim();
            data.put(key, value);
        }

        Mission mission = new Mission();

        mission.setMissionId(data.get("missionId"));
        mission.setDate(data.get("date"));
        mission.setLocation(data.get("location"));

        String outcomeStr = data.get("outcome");
        if (outcomeStr != null) {
            mission.setOutcome(Outcome.fromString(outcomeStr));
        }

        String costStr = data.get("damageCost");
        if (costStr != null) {
            mission.setDamageCost(Long.parseLong(costStr.trim()));
        }

        String comment = data.get("comment");
        if (comment == null) {
            comment = data.get("note");
        }
        mission.setComment(comment);

        Curse curse = new Curse();
        curse.setName(data.get("curse.name"));
        String threatStr = data.get("curse.threatLevel");
        if (threatStr != null) {
            curse.setThreatLevel(ThreatLevel.fromString(threatStr));
        }
        mission.setCurse(curse);

        List<Sorcerer> sorcerers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String name = data.get("sorcerer[" + i + "].name");
            if (name == null) break;
            Sorcerer s = new Sorcerer();
            s.setName(name);
            String rankStr = data.get("sorcerer[" + i + "].rank");
            if (rankStr != null) {
                s.setRank(SorcererRank.fromString(rankStr));
            }
            sorcerers.add(s);
        }
        mission.setSorcerers(sorcerers);

        List<Technique> techniques = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String name = data.get("technique[" + i + "].name");
            if (name == null) break;
            Technique t = new Technique();
            t.setName(name);
            String typeStr = data.get("technique[" + i + "].type");
            if (typeStr != null) {
                t.setType(TechniqueType.fromString(typeStr));
            }
            t.setOwner(data.get("technique[" + i + "].owner"));
            String dmgStr = data.get("technique[" + i + "].damage");
            if (dmgStr != null) {
                t.setDamage(Long.parseLong(dmgStr.trim()));
            }
            techniques.add(t);
        }
        mission.setTechniques(techniques);

        return mission;
    }
}
