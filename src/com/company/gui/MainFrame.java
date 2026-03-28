package com.company.gui;

import com.company.factory.MissionParserFactory;
import com.company.model.Mission;
import com.company.parser.MissionParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Главное окно
 */
public class MainFrame extends JFrame {

    private final MissionPanel missionPanel;
    private final JLabel statusLabel;

    public MainFrame() {
        super("Анализатор миссий магов — Токийский магический колледж");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 650);
        setMinimumSize(new Dimension(550, 500));
        setLocationRelativeTo(null);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnLoad = new JButton("Загрузить файл миссии");
        btnLoad.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLoad.addActionListener(e -> loadMissionFile());
        topBar.add(btnLoad);

        missionPanel = new MissionPanel();
        JScrollPane scrollPane = new JScrollPane(missionPanel);

        statusLabel = new JLabel("  Готов к работе. Откройте файл миссии.");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topBar, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(statusLabel, BorderLayout.SOUTH);
    }

    private void loadMissionFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Выберите файл миссии");
        chooser.setFileFilter(new FileNameExtensionFilter("Файлы миссий (*.json, *.xml, *.txt)", "json", "xml", "txt"));

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        statusLabel.setText("Загрузка: " + file.getName() + "...");

        try {
            MissionParser parser = MissionParserFactory.createParser(file);

            Mission mission = parser.parse(file);

            missionPanel.displayMission(mission);

            statusLabel.setText("Загружено: " + file.getName() + " | Формат: " + getFormatName(file.getName()));

        } catch (IllegalArgumentException ex) {
            statusLabel.setText("Ошибка: неподдерживаемый формат");
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка формата", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            statusLabel.setText("Ошибка при разборе файла");
            JOptionPane.showMessageDialog(this,"Не удалось разобрать файл:\n" + ex.getMessage(),"Ошибка разбора", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFormatName(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".json")) return "JSON";
        if (lower.endsWith(".xml"))  return "XML";
        if (lower.endsWith(".txt"))  return "TXT";
        return "неизвестный";
    }
}
