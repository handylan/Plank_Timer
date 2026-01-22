import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.CardLayout;
import javax.swing.Timer;
import java.awt.FlowLayout;
import java.awt.Toolkit;
 
public class AppUI {

    private int countdown = 0;
    private AppState appState = AppState.IDLE;

    private IntervalTimer timerA;
    private IntervalTimer timerB;
    private IntervalTimer activeTimer;
        
    private JLabel timerALabel;
    private JTextField timerAInput;
    private JLabel timerAError;

    private JLabel timerBLabel;
    private JTextField timerBInput;
    private JLabel timerBError;

    private JPanel timerAPanel;
    private JPanel timerACardPanel;
    private CardLayout timerACardLayout;

    private JPanel timerBPanel;
    private JPanel timerBCardPanel;
    private CardLayout timerBCardLayout;

    private JLabel countdownLabel;
    private Timer scheduler;

    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;

    private JFrame frame;
    

    public AppUI() {
        // Initialize UI components and layout here
        timerA = new IntervalTimer(0);
        timerB = new IntervalTimer(0);
        activeTimer = timerA;

        timerALabel = new JLabel("00:00");
        timerBLabel = new JLabel("00:00");
        countdownLabel = new JLabel("");
        timerAError = new JLabel("");
        timerBError = new JLabel("");

        // Initialize input fields for setting timer durations
        timerAInput = new JTextField("00:00", 5);
        timerBInput = new JTextField("00:00", 5); 
        
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");

        startButton.addActionListener(e -> startPressed());
        pauseButton.addActionListener(e -> pausePressed());
        resetButton.addActionListener(e -> resetPressed());

        frame = new JFrame("Interval Timer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        frame.add(timerALabel);
        frame.add(timerAInput);
        frame.add(timerAError);

        frame.add(timerBLabel);
        frame.add(timerBInput);
        frame.add(timerBError);

        frame.add(countdownLabel);

        frame.add(startButton);
        frame.add(pauseButton);
        frame.add(resetButton);


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

    public void startPressed() {
    // Only act if in IDLE or PAUSED
    if (appState == AppState.PAUSED) {
        appState = AppState.RUNNING;
        return;
    }
    if (appState != AppState.IDLE) {
        return;
    }

    // Clear previous error messages
    timerAError.setText("");
    timerBError.setText("");

    // ----- Parse Timer A -----
    String aInput = timerAInput.getText();
    String[] aParts = aInput.split(":");
    int aSeconds;

    if (aParts.length != 2) {
        timerAError.setText("Format must be MM:SS");
        return;
    }

    try {
        int aMin = Integer.parseInt(aParts[0]);
        int aSec = Integer.parseInt(aParts[1]);

        if (aMin < 0 || aSec < 0 || aSec > 59) {
            timerAError.setText("Minutes ≥0, seconds 0-59");
            return;
        }

        aSeconds = aMin * 60 + aSec;
    } catch (NumberFormatException e) {
        timerAError.setText("Numbers only");
        return;
    }

    // ----- Parse Timer B -----
    String bInput = timerBInput.getText();
    String[] bParts = bInput.split(":");
    int bSeconds;

    if (bParts.length != 2) {
        timerBError.setText("Format must be MM:SS");
        return;
    }

    try {
        int bMin = Integer.parseInt(bParts[0]);
        int bSec = Integer.parseInt(bParts[1]);

        if (bMin < 0 || bSec < 0 || bSec > 59 || (bMin == 0 && bSec == 0)) {
            timerBError.setText("Time can't be zero; minutes ≥0, seconds 0-59");
            return;
        }

        bSeconds = bMin * 60 + bSec;
    } catch (NumberFormatException e) {
        timerBError.setText("Numbers only");
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

        if(appState == AppState.COUNTDOWN) {
            countdownLabel.setVisible(true);
            countdownLabel.setText(String.valueOf(countdown));
        } else {
            countdownLabel.setVisible(false);
        }
    }
}
