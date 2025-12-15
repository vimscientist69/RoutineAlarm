# RoutineAlarm

[![Android](https://img.shields.io/badge/Platform-Android-green)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-%E2%9C%93-blueviolet)](https://kotlinlang.org/)
[![Markdown](https://img.shields.io/badge/Input-Markdown-orange)](https://daringfireball.net/projects/markdown/)
[![Productivity Tool](https://img.shields.io/badge/Productivity-Tool-yellowgreen)]()
[![Unpublished](https://img.shields.io/badge/Status-Unpublished-lightgrey)]()

---

...image of application...

---
RoutineAlarm is a small Android utility app intended for personal use that reads daily routine files in Markdown (for example from Obsidian) and creates corresponding alarms in the system Clock app so you get notified when each timebox starts. It speeds up creating multiple alarms compared to creating them one-by-one in the Clock app manually.

> [!important]
> This project is typically used locally/unpublished on your phone as a personal tool.

> [!info] 
> Batch alarm creation isn't supported, so simply press the "Back" button after a alarm is created to navigate back to the application, and repeat until all alarms are successfully created.

---
## Features
- Upload daily routine files for specific days of week (weekdays, Saturday and Sunday)
- Parse routines and extract task start times and descriptions
- Creates alarms via the system Clock app so alarms integrate with your phone’s normal alarm UX
- Lightweight and focused on single-device personal use

---
## Supported Input Format 
Your routine files should be plain markdown with sections and list items like:

```
**morning routine**
- [ ] 05:30am - 05:35am / make bed
- [ ] 05:35am - 05:40am / track weight + hydration
- [ ] 05:40am - 05:45am / outdoor sunlight in eyes

**work ready**
- [ ] 05:45am - 06:15am / breakfast
- [ ] 06:15am - 06:45am / personal hygiene
- [ ] 06:45am - 07:00am / go to work
```

The app parses each list line to extract the start time (e.g., `05:30am`) and the description after `/`. Only the start time is used to create alarms. Time formats accepted: `hh:mmam`, `hh:mmpm`, `HH:MM` (24-hour) — whitespace tolerant. (The app will show parse errors for malformed lines.)

---
## Installation

To install and use this application on your Android phone, follow the these steps:
1. Download and install Android Studio
2. Clone repository locally and open in Android Studio
```sh
git clone https://github.com/vimscientist69/RoutineAlarm
```
3. [Setup wireless debugging](https://www.muvi.com/blogs/wireless-debugging-setup-in-android-studio/)
4. Run application

---
## Contributing

Contributions are welcome! If you encounter any issues or have suggestions for improvements, feel free to open an issue or submit a pull request.

---
## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/vimscientist69/i3-nightlight/blob/master/LICENSE) file for details.

---
