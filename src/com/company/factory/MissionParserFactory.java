package com.company.factory;

import com.company.parser.JsonMissionParser;
import com.company.parser.MissionParser;
import com.company.parser.TxtMissionParser;
import com.company.parser.XmlMissionParser;

import java.io.File;

/**
 * Создаёт парсер для указанного файла
 */
public class MissionParserFactory {
    public static MissionParser createParser(File file) {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".json")) {
            return new JsonMissionParser();
        } else if (name.endsWith(".xml")) {
            return new XmlMissionParser();
        } else if (name.endsWith(".txt")) {
            return new TxtMissionParser();
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат файла: " + name + "\nПоддерживаются: .json, .xml, .txt");
        }
    }
}
