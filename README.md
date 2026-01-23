# Plank_Timer
> **This project was developed as a personal practice project to improve Java GUI development and timer logic skills.**

<br>

## 📌 Overview
Plank Timer is a simple interval timer application designed for plank training and other interval-based workouts.<br>
It allows users to configure separate training and rest durations, automatically cycling between them with visual state changes and sound notifications for a smoother workout experience.
---

## ⚙️ Features
- Separate timers for configuring training and rest durations
- Start button to begin the timer or resume from a paused state
- Pause button to temporarily halt the timer
- Reset button to fully reset the timer to the idle state (before time input)
- Countdown sequence before the timer starts
- Sound notifications when switching between training and rest, and after the countdown
- Error message display when input values are out of bounds
---

## 🖥 Display States
### Idle
<img width="1334" height="892" alt="Idle state" src="https://github.com/user-attachments/assets/b23dd5db-ac68-4144-8c00-f735b00d7684" />

### Countdown
<img width="1334" height="892" alt="Countdown state" src="https://github.com/user-attachments/assets/41bc747c-884f-4243-9fc6-398fa4a64e7f" />

### Running
<img width="1334" height="892" alt="Running state (training)" src="https://github.com/user-attachments/assets/35914745-6ed2-4072-893e-534b7b2997ce" />

<img width="1334" height="892" alt="Running state (rest)" src="https://github.com/user-attachments/assets/66dcbdbd-c650-4f8d-8985-4a05eeeae52c" />

### Paused
> Same as the running state, but frozen.

### Error
<img width="329" height="161" alt="Error message (invalid input)" src="https://github.com/user-attachments/assets/32d3f80a-f612-41f3-ba49-245063a2acb3" />
<img width="329" height="151" alt="Error message (out of range)" src="https://github.com/user-attachments/assets/0cb55d91-5045-4df2-89da-e5cb9ab6da18" />
<img width="329" height="151" alt="Error message" src="https://github.com/user-attachments/assets/8d2101e7-1f42-4b7f-90cd-f018e91eb14f" />

---

## 🛠 Tech Stack
- **Language:** Java
- **UI Framework:** Java Swing
- **Runtime:** JVM (Desktop Application)
---

## 🎯 Purpose
- Practice Java Swing UI development
- Understand timer management and state transitions
- Improve input validation and user feedback handling
