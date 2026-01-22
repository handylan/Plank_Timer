import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.Timer;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Color;
 
public class AppUI {

    private int countdown = 0;
    private AppState appState = AppState.IDLE;

    private IntervalTimer timerA;
    private IntervalTimer timerB;
    private IntervalTimer activeTimer;
        
    private JLabel timerATitle;
    private JLabel timerALabel;
    private JTextField timerAInput;
    private JPanel timerAPanel;
    private JPanel timerACardPanel;
    private CardLayout timerACardLayout;

    private JLabel timerBTitle;
    private JLabel timerBLabel;
    private JTextField timerBInput;
    private JPanel timerBPanel;
    private JPanel timerBCardPanel;
    private CardLayout timerBCardLayout;

    private JLabel countdownLabel;
    private Timer scheduler;

    private JPanel timersPanel = new JPanel();
    private JComponent countdownPanel = new JPanel();
    private JPanel buttonsPanel = new JPanel();

    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;

    private JFrame frame;

    public AppUI() {
        // Set up the main application window
        frame = new JFrame("Interval Timer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLayout(new BorderLayout());

        timersPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // Initialize UI components and layout here
        timerA = new IntervalTimer(0);
        timerB = new IntervalTimer(0);
        activeTimer = timerA;

        timerATitle = new JLabel("Train ");
        timerALabel = new JLabel("00:00");
        timerALabel.setHorizontalAlignment(JLabel.CENTER);
        timerAInput = new JTextField("00:00", 5);
        timerAInput.setHorizontalAlignment(JTextField.CENTER);
        
        timerBTitle = new JLabel("Rest ");
        timerBLabel = new JLabel("00:00");
        timerBLabel.setHorizontalAlignment(JLabel.CENTER);
        timerBInput = new JTextField("00:00", 5); 
        timerBInput.setHorizontalAlignment(JTextField.CENTER);

        countdownLabel = new JLabel("");

        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        
        Font timerFont = new Font("Arial", Font.BOLD, 144);
        timerALabel.setFont(timerFont);
        timerBLabel.setFont(timerFont);
        timerAInput.setFont(timerFont);
        timerBInput.setFont(timerFont);
        Font titleFont = new Font("Arial", Font.BOLD, 72);
        timerATitle.setFont(titleFont);
        timerBTitle.setFont(titleFont);
        Font buttonFont = new Font("Arial", Font.BOLD, 36);
        startButton.setFont(buttonFont);
        pauseButton.setFont(buttonFont);
        resetButton.setFont(buttonFont);

        // Timer A Panel
        timerAPanel = new JPanel();
        timerAPanel.setLayout(new FlowLayout());
        // Timer A Card Layout for input and display
        timerACardLayout = new CardLayout();
        timerACardPanel = new JPanel(timerACardLayout);
        //Cards
        timerACardPanel.add(timerAInput, "INPUT");
        timerACardPanel.add(timerALabel, "DISPLAY");
        // Assemble Timer A Panel
        timerAPanel.add(timerATitle, BorderLayout.NORTH);
        timerAPanel.add(timerACardPanel, BorderLayout.CENTER);
        // Add Timer A panel to timers panel
        timersPanel.add(timerAPanel);

        // Timer B Panel
        timerBPanel = new JPanel();
        timerBPanel.setLayout(new FlowLayout());
        // Timer B Card Layout for input and display
        timerBCardLayout = new CardLayout();
        timerBCardPanel = new JPanel(timerBCardLayout);
        // Cards
        timerBCardPanel.add(timerBInput, "INPUT");
        timerBCardPanel.add(timerBLabel, "DISPLAY");
        // Assemble Timer B Panel
        timerBPanel.add(timerBTitle);
        timerBPanel.add(timerBCardPanel);
        // Add Timer B panel to timers panel
        timersPanel.add(timerBPanel);

        // Glass pane
        JPanel glass = new JPanel(null);
        glass.setOpaque(false);
        frame.setGlassPane(glass);

        // Countdown panel
        countdownPanel = new JPanel(new BorderLayout());
        countdownPanel.setBounds(340, 200, 400, 240);
        countdownPanel.setBackground(new Color(0, 0, 0, 180));
        countdownPanel.setVisible(false);

        // Countdown label
        countdownLabel = new JLabel("", JLabel.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 192));
        countdownLabel.setForeground(Color.GREEN);

        // Assemble ONCE
        countdownPanel.add(countdownLabel, BorderLayout.CENTER);
        glass.add(countdownPanel);

        // Enable glass
        glass.setVisible(true);

        
        // Buttons
        startButton.addActionListener(e -> startPressed());
        pauseButton.addActionListener(e -> pausePressed());
        resetButton.addActionListener(e -> resetPressed());
        // Add buttons to buttons panel
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20));
        buttonsPanel.add(startButton);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(resetButton);

        frame.add(timersPanel, BorderLayout.CENTER);
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        scheduler = new Timer(1000, e -> handleTick());
        scheduler.start();
        
        frame.setVisible(true);
    }
        
    public enum AppState {
        IDLE,
        COUNTDOWN,
        RUNNING,
        PAUSED,
    }

    private void showError(String message) {
    JOptionPane.showMessageDialog(
        frame,
        message,
        "Invalid Input",
        JOptionPane.ERROR_MESSAGE
    );
}


    public void startPressed() {
    // Only act if in IDLE or PAUSED
    if (appState == AppState.PAUSED) {
        appState = AppState.RUNNING;
        return;
    }
    if (appState != AppState.IDLE) {
        return;
    }

    // ----- Parse Timer A -----
    String aInput = timerAInput.getText();
    String[] aParts = aInput.split(":");
    int aSeconds;

    if (aParts.length != 2) {
        showError("Format must be MM:SS");
        return;
    }

    try {
        int aMin = Integer.parseInt(aParts[0]);
        int aSec = Integer.parseInt(aParts[1]);

        if (aMin < 0 || aSec < 0 || aSec > 59 || (aMin == 0 && aSec == 0)) {
            showError("Time can't be zero;\nMinutes ≥0, seconds 0-59");
            return;
        }

        aSeconds = aMin * 60 + aSec;
    } catch (NumberFormatException e) {
        showError("Numbers only");
        return;
    }

    // ----- Parse Timer B -----
    String bInput = timerBInput.getText();
    String[] bParts = bInput.split(":");
    int bSeconds;

    if (bParts.length != 2) {
        showError("Format must be MM:SS");
        return;
    }

    try {
        int bMin = Integer.parseInt(bParts[0]);
        int bSec = Integer.parseInt(bParts[1]);

        if (bMin < 0 || bSec < 0 || bSec > 59 || (bMin == 0 && bSec == 0)) {
            showError("Time can't be zero;\nMinutes ≥0, seconds 0-59");
            return;
        }

        bSeconds = bMin * 60 + bSec;
    } catch (NumberFormatException e) {
        showError("Numbers only");
        return;
    }

    // ----- All input valid, configure timers -----
    timerA.setDuration(aSeconds);
    timerA.reset();
    timerB.setDuration(bSeconds);
    timerB.reset();

    activeTimer = timerA;
    countdown = 4;
    appState = AppState.COUNTDOWN;
}


    public void pausePressed() {
        // Handle pause button press
        if (appState == AppState.RUNNING) {
            appState = AppState.PAUSED;
        }
    }

    public void resetPressed() {
        // Handle reset button press
        timerA.reset();
        timerB.reset();
        activeTimer = timerA;
        countdown = 0;
        appState = AppState.IDLE;
    }

    private void handleTick() {
        // Handle timer tick
        if (appState == AppState.COUNTDOWN) {
            countdown--;
            if (countdown < 1) {
                activeTimer = timerA;
                activeTimer.reset();
                Toolkit.getDefaultToolkit().beep();
                appState = AppState.RUNNING;
            }
        } else if (appState == AppState.RUNNING) {
            if (!activeTimer.tick()) {
                // Switch active timer when one runs out
                Toolkit.getDefaultToolkit().beep();
                activeTimer = ((activeTimer == timerA) ? timerB : timerA);
                activeTimer.reset();
            }
        }

        updateUI();
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updateUI() {
        timerALabel.setText(formatTime(timerA.getTimeLeft()));
        timerBLabel.setText(formatTime(timerB.getTimeLeft()));

        boolean editable = (appState == AppState.IDLE);
        timerAInput.setEditable(editable);
        timerBInput.setEditable(editable);

        if(appState == AppState.IDLE) {
            timerACardLayout.show(timerACardPanel, "INPUT");
            timerBCardLayout.show(timerBCardPanel, "INPUT");
        } else {
            timerACardLayout.show(timerACardPanel, "DISPLAY");
            timerBCardLayout.show(timerBCardPanel, "DISPLAY");
        }

        if(appState == AppState.COUNTDOWN) {
            countdownLabel.setText(String.valueOf(countdown));
            countdownPanel.setVisible(true);
        } else {
            countdownPanel.setVisible(false);
        }

        if(activeTimer == timerA && appState == AppState.RUNNING) {
            timerALabel.setForeground(Color.GREEN);
            timerBLabel.setForeground(Color.BLACK);
        } else if(activeTimer == timerB && appState == AppState.RUNNING) {
            timerALabel.setForeground(Color.BLACK);
            timerBLabel.setForeground(Color.GREEN);
        }
    }
}
