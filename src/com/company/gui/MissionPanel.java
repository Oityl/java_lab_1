package com.company.gui;

import com.company.model.Mission;
import com.company.model.Sorcerer;
import com.company.model.Technique;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Панель для отображения данных о миссии
 */
public class MissionPanel extends JPanel {

    private final JLabel lblMissionId = new JLabel("-");
    private final JLabel lblDate = new JLabel("-");
    private final JLabel lblLocation = new JLabel("-");
    private final JLabel lblOutcome = new JLabel("-");
    private final JLabel lblDamageCost = new JLabel("-");
    private final JLabel lblCurseName = new JLabel("-");
    private final JLabel lblCurseThreat = new JLabel("-");
    private final JTextArea txtSorcerers = new JTextArea(4, 40);
    private final JTextArea txtTechniques = new JTextArea(6, 40);
    private final JTextArea txtComment = new JTextArea(2, 40);

    public MissionPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(createTitledBorder("Общая информация о миссии"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addLabeledField(infoPanel, gbc, row++, "ID миссии:", lblMissionId);
        addLabeledField(infoPanel, gbc, row++, "Дата:", lblDate);
        addLabeledField(infoPanel, gbc, row++, "Локация:", lblLocation);
        addLabeledField(infoPanel, gbc, row++, "Итог:", lblOutcome);
        addLabeledField(infoPanel, gbc, row++, "Стоимость ущерба:", lblDamageCost);

        JPanel cursePanel = new JPanel(new GridBagLayout());
        cursePanel.setBorder(createTitledBorder("Проклятие (цель)"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4, 8, 4, 8);
        gbc2.anchor = GridBagConstraints.WEST;
        addLabeledField(cursePanel, gbc2, 0, "Название:", lblCurseName);
        addLabeledField(cursePanel, gbc2, 1, "Уровень угрозы:", lblCurseThreat);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(infoPanel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(cursePanel);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        txtSorcerers.setEditable(false);
        txtSorcerers.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtSorcerers.setLineWrap(true);
        txtSorcerers.setWrapStyleWord(true);
        JScrollPane scrollSorcerers = new JScrollPane(txtSorcerers);
        scrollSorcerers.setBorder(createTitledBorder("Участники (маги)"));
        centerPanel.add(scrollSorcerers);
        centerPanel.add(Box.createVerticalStrut(5));

        txtTechniques.setEditable(false);
        txtTechniques.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtTechniques.setLineWrap(true);
        txtTechniques.setWrapStyleWord(true);
        JScrollPane scrollTechniques = new JScrollPane(txtTechniques);
        scrollTechniques.setBorder(createTitledBorder("Применённые техники"));
        centerPanel.add(scrollTechniques);
        centerPanel.add(Box.createVerticalStrut(5));

        txtComment.setEditable(false);
        txtComment.setFont(new Font("SansSerif", Font.ITALIC, 13));
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        JScrollPane scrollComment = new JScrollPane(txtComment);
        scrollComment.setBorder(createTitledBorder("Комментарий / Примечание"));
        centerPanel.add(scrollComment);

        add(centerPanel, BorderLayout.CENTER);

        showPlaceholder();
    }

    public void displayMission(Mission m) {
        lblMissionId.setText(m.getMissionId() != null ? m.getMissionId() : "-");
        lblDate.setText(m.getDate() != null ? m.getDate() : "-");
        lblLocation.setText(m.getLocation() != null ? m.getLocation() : "-");

        if (m.getOutcome() != null) {
            lblOutcome.setText(m.getOutcome().getDisplayName());

            switch (m.getOutcome()) {
                case SUCCESS:
                    lblOutcome.setForeground(new Color(0, 128, 0));
                    break;
                case FAILURE:
                    lblOutcome.setForeground(Color.RED);
                    break;
                case PARTIAL:
                    lblOutcome.setForeground(new Color(200, 150, 0));
                    break;
                default:
                    lblOutcome.setForeground(Color.BLACK);
                    break;
            }
        } else {
            lblOutcome.setText("-");
            lblOutcome.setForeground(Color.BLACK);
        }

        lblDamageCost.setText(String.format("%,d ¥", m.getDamageCost()));

        if (m.getCurse() != null) {
            lblCurseName.setText(m.getCurse().getName() != null ? m.getCurse().getName() : "-");
            lblCurseThreat.setText(m.getCurse().getThreatLevel() != null ? m.getCurse().getThreatLevel().getDisplayName() : "-");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m.getSorcerers().size(); i++) {
            Sorcerer s = m.getSorcerers().get(i);
            sb.append(String.format("  %d. %s — %s%n", i + 1, s.getName(), s.getRank() != null ? s.getRank().getDisplayName() : "?"));
        }
        txtSorcerers.setText(sb.toString().trim());

        sb = new StringBuilder();
        for (int i = 0; i < m.getTechniques().size(); i++) {
            Technique t = m.getTechniques().get(i);
            sb.append(String.format("  %d. %s [%s]%n     Маг: %s | Урон: %,d ¥%n", i + 1, t.getName(), t.getType() != null ? t.getType().getDisplayName() : "?", t.getOwner() != null ? t.getOwner() : "?", t.getDamage()));
        }
        if (!m.getTechniques().isEmpty()) {
            sb.append(String.format("%n  Суммарный урон: %,d ¥", m.getTotalDamage()));
        }
        txtTechniques.setText(sb.toString().trim());

        txtComment.setText(m.getComment() != null ? m.getComment() : "(нет)");
    }

    public void showPlaceholder() {
        lblMissionId.setText("-");
        lblDate.setText("-");
        lblLocation.setText("-");
        lblOutcome.setText("-");
        lblOutcome.setForeground(Color.BLACK);
        lblDamageCost.setText("-");
        lblCurseName.setText("-");
        lblCurseThreat.setText("-");
        txtSorcerers.setText("  Загрузите файл миссии (JSON / XML / TXT)...");
        txtTechniques.setText("");
        txtComment.setText("");
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, int row, String label, JLabel valueLabel) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(valueLabel, gbc);
    }

    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        return border;
    }
}
