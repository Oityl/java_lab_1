package com.company.parser;

import com.company.model.Mission;
import java.io.File;

/**
 * Интерфейс для всех парсеров миссий
 */
public interface MissionParser {
    Mission parse(File file) throws Exception;
}
