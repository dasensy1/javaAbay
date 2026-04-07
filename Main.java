import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class Main {

    // Цветовая палитра тёмной темы
    static final Color BG_DARK       = new Color(18, 18, 24);
    static final Color BG_CARD       = new Color(28, 28, 38);
    static final Color BG_BUTTON     = new Color(99, 102, 241);
    static final Color BG_BTN_HOVER  = new Color(79, 82, 221);
    static final Color BG_RESET      = new Color(38, 38, 52);
    static final Color TEXT_PRIMARY  = new Color(240, 240, 255);
    static final Color TEXT_MUTED    = new Color(140, 140, 170);
    static final Color ACCENT        = new Color(99, 102, 241);
    static final Color DIVIDER       = new Color(50, 50, 70);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowWindow);
    }

    private static void createAndShowWindow() {
        JFrame frame = new JFrame("Counter App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 380);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 420, 380, 20, 20));

        // Главная панель
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(24, 28, 24, 28));

        // --- Заголовок с кнопкой закрытия ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel("Counter App");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT_MUTED);
        topBar.add(title, BorderLayout.WEST);

        JButton closeBtn = new JButton("x");
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        closeBtn.setForeground(TEXT_MUTED);
        closeBtn.setBackground(new Color(0, 0, 0, 0));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { closeBtn.setForeground(new Color(240, 80, 80)); }
            public void mouseExited(MouseEvent e)  { closeBtn.setForeground(TEXT_MUTED); }
        });
        topBar.add(closeBtn, BorderLayout.EAST);

        // --- Карточка счётчика ---
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(DIVIDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(30, 20, 30, 20));

        int[] counter = {0};

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Подпись
        JLabel subtitle = new JLabel("нажатий", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_MUTED);
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 6, 0);
        card.add(subtitle, gbc);

        // Большое число
        JLabel counterLabel = new JLabel("0", SwingConstants.CENTER);
        counterLabel.setFont(new Font("Segoe UI", Font.BOLD, 72));
        counterLabel.setForeground(TEXT_PRIMARY);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 28, 0);
        card.add(counterLabel, gbc);

        // Разделитель
        JSeparator sep = new JSeparator();
        sep.setForeground(DIVIDER);
        sep.setBackground(DIVIDER);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 28, 0);
        card.add(sep, gbc);

        // Кнопка "Нажать"
        RoundButton mainBtn = new RoundButton("+ Нажать", BG_BUTTON, BG_BTN_HOVER, TEXT_PRIMARY);
        mainBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        mainBtn.addActionListener(e -> {
            counter[0]++;
            counterLabel.setText(String.valueOf(counter[0]));
            // Лёгкая анимация цвета числа
            counterLabel.setForeground(ACCENT);
            Timer timer = new Timer(300, ev -> counterLabel.setForeground(TEXT_PRIMARY));
            timer.setRepeats(false);
            timer.start();
        });
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 10, 0);
        card.add(mainBtn, gbc);

        // Кнопка "Сбросить"
        RoundButton resetBtn = new RoundButton("Сбросить", BG_RESET, new Color(55, 55, 75), TEXT_MUTED);
        resetBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resetBtn.addActionListener(e -> {
            counter[0] = 0;
            counterLabel.setText("0");
            counterLabel.setForeground(TEXT_PRIMARY);
        });
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 0, 0);
        card.add(resetBtn, gbc);

        // --- Сборка ---
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(card, BorderLayout.CENTER);

        // Перетаскивание окна мышью
        Point[] drag = {null};
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { drag[0] = e.getPoint(); }
        });
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (drag[0] != null) {
                    Point loc = frame.getLocation();
                    frame.setLocation(loc.x + e.getX() - drag[0].x, loc.y + e.getY() - drag[0].y);
                }
            }
        });

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // Кастомная кнопка с закруглёнными углами
    static class RoundButton extends JButton {
        private final Color normalBg;
        private final Color hoverBg;
        private boolean hovered = false;

        RoundButton(String text, Color normalBg, Color hoverBg, Color fg) {
            super(text);
            this.normalBg = normalBg;
            this.hoverBg = hoverBg;
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(300, 44));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? hoverBg : normalBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}