package com.company.parser;

import com.company.model.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Парсер XML-файлов миссий
 */
public class XmlMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        Mission mission = new Mission();

        mission.setMissionId(getTagText(root, "missionId"));
        mission.setDate(getTagText(root, "date"));
        mission.setLocation(getTagText(root, "location"));
        mission.setComment(getTagText(root, "comment"));

        String outcomeStr = getTagText(root, "outcome");
        if (outcomeStr != null) {
            mission.setOutcome(Outcome.fromString(outcomeStr));
        }

        String costStr = getTagText(root, "damageCost");
        if (costStr != null) {
            mission.setDamageCost(Long.parseLong(costStr.trim()));
        }

        NodeList curseNodes = root.getElementsByTagName("curse");
        if (curseNodes.getLength() > 0) {
            Element curseEl = (Element) curseNodes.item(0);
            Curse curse = new Curse();
            curse.setName(getTagText(curseEl, "name"));
            String threatStr = getTagText(curseEl, "threatLevel");
            if (threatStr != null) {
                curse.setThreatLevel(ThreatLevel.fromString(threatStr));
            }
            mission.setCurse(curse);
        }

        List<Sorcerer> sorcerers = new ArrayList<>();
        NodeList sorcererNodes = root.getElementsByTagName("sorcerer");
        for (int i = 0; i < sorcererNodes.getLength(); i++) {
            Element el = (Element) sorcererNodes.item(i);
            Sorcerer s = new Sorcerer();
            s.setName(getTagText(el, "name"));
            String rankStr = getTagText(el, "rank");
            if (rankStr != null) {
                s.setRank(SorcererRank.fromString(rankStr));
            }
            sorcerers.add(s);
        }
        mission.setSorcerers(sorcerers);

        List<Technique> techniques = new ArrayList<>();
        NodeList techNodes = root.getElementsByTagName("technique");
        for (int i = 0; i < techNodes.getLength(); i++) {
            Element el = (Element) techNodes.item(i);
            Technique t = new Technique();
            t.setName(getTagText(el, "name"));
            String typeStr = getTagText(el, "type");
            if (typeStr != null) {
                t.setType(TechniqueType.fromString(typeStr));
            }
            t.setOwner(getTagText(el, "owner"));
            String damageStr = getTagText(el, "damage");
            if (damageStr != null) {
                t.setDamage(Long.parseLong(damageStr.trim()));
            }
            techniques.add(t);
        }
        mission.setTechniques(techniques);

        return mission;
    }

    private String getTagText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return null;
        String text = nodes.item(0).getTextContent();

        return (text != null && !text.trim().isEmpty()) ? text.trim() : null;
    }
}
