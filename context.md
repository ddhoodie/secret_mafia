# Secret Mafia — Context

Living design doc. Update it when rules or product change.
Current focus: **MVP with 4 core roles** (mafia, healer, cop, civilian).
Special roles from `specifikacija.txt` stay at count `0` until we add them.

UI language: **English**. Serbian comes later as a locale.

---

## 1. Product

One phone runs the game. People play live around a table.
The phone is the host, not a player. Nobody sits out.

The phone travels in a circle, in the order names were entered (same as seating).
Each player sees only their own role and does their night action. Others watch the person, not the screen.

Loop: **night** (secret actions) → **summary** → **discuss** → **day vote** → next night.
Mafia wins if living evil ≥ living good.
Good wins if no mafia remain.

---

## 2. Tech stack

**Kotlin + Jetpack Compose, minSdk 26, single Android module.**

Why:
- Android Studio is already here.
- Compose is good for screens, state, and later animations.
- Vibration, sound, and hold-to-unlock are native.
- One phone, no network in MVP.

Font: **Pixel Operator** from `pixel_operator/PixelOperator.ttf` (+ Bold if needed).
Logo: `assets/secret-mafia-logo.png` (pixel, black/white, red eye / blood).

---

## 3. Visual language

- Background: pure black.
- Text: white.
- Buttons: black fill, white outline, white text.
- Accent: **red** only for bad (kill, bad inspect, warning, expiring timer bar).
- Heal / good: green only on the heal animation (palette exception so the action reads).
- Dummy cover actions: white (like, plus, check).
- No gray fog, no soft Material cards. Sharp, pixel, empty space.

---

## 4. Main menu

1. **PLAY GAME** — setup, then start.
2. **SETTINGS** — global options. Theme extras come later.
3. **ABOUT** — short text: one phone hosts a live mafia game so nobody has to sit out as narrator.

On-screen title: **SECRET MAFIA**. Package / folder: `secret_mafia`.

---

## 5. Roles

### 5.1 MVP (always in setup)

| Role | Team | Night action | Notes |
|---|---|---|---|
| Mafia | evil | pick who to kill | vote stacks with other mafia; **may kill self** |
| Healer | good | pick who to protect | blocks tonight’s kill |
| Cop | good | pick who to inspect | sees GOOD or BAD, not the exact role |
| Civilian | good | dummy action | must tap something so power roles do not stand out |

### 5.2 Later (spec, default 0)

Mafia: Don, Silent, Fake doctor, Lawyer.
Good: Hunter, Seer, Bodyguard.
Neutral: Joker, Solo killer.
Chaos: Trickster, Lunatic, Drunk, Twin, Swapper, Spy.

Every future role has `mafiaStatus` (night tie-break).
Regular mafia = 1. Don (later) = higher. Healer / cop / civilian = 0.

---

## 6. Setup

Host (phone holder) enters:

1. Player names, seating order. Host types **themself first**, then around the table.
2. Count of each MVP role. Specials stay at 0 for now.
3. The system recommends a split from player count. Host may change it.

Balance check:
- good should be **at least 1.5×** evil
- if not: warning, not a hard block (“start anyway”)

### 6.1 Recommended split (MVP)

| Players | Mafia | Healer | Cop | Civilian |
|---|---|---|---|---|
| 4 | 1 | 1 | 1 | 1 |
| 5 | 1 | 1 | 1 | 2 |
| 6 | 2 | 1 | 1 | 2 |
| 7 | 2 | 1 | 1 | 3 |
| 8 | 2 | 1 | 1 | 4 |
| 9 | 3 | 1 | 1 | 4 |
| 10 | 3 | 1 | 1 | 5 |
| 11 | 3 | 1 | 1 | 6 |
| 12 | 3 | 1 | 1 | 7 |

Min players: 4. Max for now: 12 (easy to raise).

Roles are assigned **randomly** after Start. Seating order never changes.

---

## 7. Night loop

Phone goes through **living** players, from `startIndex`, then around.

`startIndex`:
- night 1: player 0 (host, first name entered)
- each later night: previous start + 1 (skip dead)
- after discuss / day vote it does **not** reset to the first player

Dead players **never** receive the phone. They appear in summaries only.

### 7.1 Handoff

Big name of the player who should hold the phone.
Countdown **3 · 2 · 1**.
Copy like: `PASS TO [NAME]`.
Role is still hidden.

### 7.2 Unlock

Long-press the screen / button.
Only then: private screen with role + action.

### 7.3 Action, then 3 seconds

After the action, the role stays visible **at most 3 seconds**.
A bar at the bottom (red or white) drains. Then lock and handoff to the next living player.

Everyone must do *some* tap. If only mafia / healer / cop tap, the table can see who has a power.

### 7.4 Real actions

**Mafia**
- list of living players, **including self**
- tap a target
- short dumb animation (red X / knife flash)
- vote is stored
- screen also lists **other living mafia by name**
- if vote-count option is on and this is not the first mafia tonight: running counts, e.g. `3 mafia voted to kill Milica`
- last mafia in seating order also sees **who currently dies** (before their vote)

**Healer**
- list of living players, **including self**
- tap a target
- green pluses
- that player is protected tonight
- default rule: **cannot heal the same person two nights in a row** (option can turn this off)

**Cop**
- list of living players, **not self**
- tap to inspect
- result: white **GOOD** / red **BAD** + a small icon, no role name
- GOOD = good team, BAD = evil team

**Civilian**
- dummy action, then 3s bar

### 7.5 Dummy cover action

Chosen in settings (can also be random per player):

1. **Like** (default) — tap any living player, white like animation. No game effect.
2. **Math** — easy sum (`3+7`, `2+6`, `10+5`…), 3 answers, one correct. Then the same 3s bar.

---

## 8. Night kill

Tonight’s victim = living player with the **most mafia votes**, if not healed.

Tie:
1. Take everyone with max votes.
2. Winner is the one who received a vote from the mafia with the highest `mafiaStatus`.
3. If status is also tied → **50/50** among those targets.

Heal:
- if the winning target was healed → nobody dies tonight (MVP).
- bodyguard / fake doctor come later.

### 8.1 First night

Setting: **first night kill**.
Default: **ON** (someone can die night 1).
If OFF: night 1 still runs (roles, inspect, heal, dummy), but the mafia kill is ignored.

### 8.2 Mafia night info

Mafia order = seating order, living mafia only.

- Later mafia can see vote counts from mafia before them (setting).
- Last mafia in that order sees who currently dies.
- Setting **Show mafia vote count**: ON/OFF.
- When OFF: each mafia only sees the list + teammate names, no counts, no victim preview.

Teammate names **are shown**.

---

## 9. Morning / summary / discuss / day vote

After every living player finishes the night:

1. **SUMMARY**
   - `[NAME] was killed.`
   - or `Nobody died.`
   - Role on death: default **hidden**. Setting can reveal it.
2. Win check.
3. If no winner → **DISCUSS**.
4. Optional discuss timer. On expire: vibrate + ring.
5. Then **day vote** (see §10).
6. Win check again.
7. If no winner: hold to start the next night from the **next** living player.

---

## 10. Day vote

Happens after discuss. Two modes (setup / settings):

| Mode | Default | What happens |
|---|---|---|
| **Live** | yes | Table votes out loud / by pointing. Host taps who was exiled, or Nobody. |
| **Phone** | no | Phone goes around again. Each living player hold-unlocks and votes. Dummy cover if we need timing cover (same like/math). |

Phone-vote result:
- most votes is exiled
- **tie → nobody is exiled**

Live-vote result:
- host picks one living player or Nobody
- no automatic tie logic (the table already decided)

Exiled player dies, then win check. Dead skip the next night.

---

## 11. Win

After every death (night or day):

- **Mafia wins** if living evil **≥** living good.
- **Good wins** if no living mafia remain.

Neutrals (later) do not count in this ratio.

---

## 12. Settings

### In MVP / soon
- Show mafia vote count (on/off)
- Discuss timer (off / 2 / 3 / 5 / 8 min) + vibrate + sound
- Dummy action type (like / math / random)
- Reveal role on death (off default / on)
- Reveal roles at end of game (on default / off)
- First night kill (on default / off)
- Healer may repeat same target (off default / on)
- Day vote mode (**live** default / phone)
- Sound master on/off

### Later
- Per-player night timer (spec: same length for all, e.g. 15s)
- Narrator voice (recorded lines instead of on-screen death text)
- Judge mode
- Account
- Special roles leftover from spec (Silent, Fake doctor, Trickster, Swapper, Spy)
- **Statistics (coming soon):** per-player role history (how many times each role), winrate by role and by team, games played, export (CSV / share). Stored on the phone first. Optional later: compare tables / seasons.

---

## Twins

Twins are a pair (count is 0 or 2). Two flavors:

- **Civilian twin** — good. Dummy night action. If one dies, the other dies.
- **Mafia twin** — evil, votes with the crew, sees teammates and their twin. If one dies, the other dies.

A table can run one civilian pair, one mafia pair, both, or neither.

---

## 13. Locked decisions

1. Day vote exists, after discuss. Live or phone. Default live.
2. Dead players skip the phone.
3. Dummy and real actions should feel the same length. Default dummy = like.
4. Mafia **see teammate names**.
5. Healer may heal self. Mafia may kill self. Cop may not inspect self.
6. Healer **cannot** heal the same person two nights in a row (option to allow).
7. First night kill **on** by default (option to skip the kill).
8. Haptics on hold, 3-2-1, death, discuss timeout.
9. Lock overlay between players: black screen + name, so the previous role is gone.
10. UI copy is English. Title: SECRET MAFIA.
11. Day-vote tie (phone mode): nobody is exiled.
12. Death summary hides the role by default.
13. Game over **shows who had which role** by default. Setting: Reveal roles at end (on / off).
14. Balance warning only cares about **good vs evil**. Wild roles are the host’s choice. Concrete copy: “Too many evil for N players. X evil / Y good.”

---

## 14. Screens (MVP map)

```
MainMenu
  ├─ About
  ├─ Settings
  └─ Setup
       ├─ PlayerList (names, seating order)
       ├─ RoleCounts (recommendation + manual)
       └─ Game
            ├─ Handoff (name + 3-2-1)
            ├─ Unlock (hold)
            ├─ RoleAction (real or dummy)
            ├─ ActionLock (3s bar)
            ├─ NightSummary
            ├─ Discuss
            ├─ DayVoteLive (pick exile / nobody)
            ├─ DayVotePhone (handoff loop)
            └─ GameOver
```

---

## 15. Not in the first implementation step

- special roles
- account, online, multi-phone
- light theme
- Serbian locale
- narrator voice
- judge mode
- scoring (spec is empty)
- animations beyond the minimum (like / plus / bar)

---

## 16. Implementation status

Settings are split: **Appearance** (white theme, Serbian, hide red/green in-game, sound) and **Gameplay** (Roles / Night / Day). Narrator voice is Coming soon.

Advanced roles live on a second setup screen: Don, Lawyer, Hunter, Seer, Bodyguard, Joker, Killer, Lunatic, Drunk, Twin.

Main menu has **Rules** (General, Roles, Night, Day, Winning) and **Statistics** (coming soon).

Rules → Roles is only **Good / Evil / Wild**. No Core group.

Android app exists (`com.secretmafia`, Kotlin + Jetpack Compose).

Working now:
- Main menu / Settings / About / Setup
- Recommended role split + balance warning
- Full night pass (handoff 3-2-1, hold unlock, role actions, 3s drain)
- Mafia teammates + optional vote counts + last-mafia preview
- Morning summary, discuss timer, live or phone day vote
- Win check, game over

Open in Android Studio → Run on a phone. Debug APK: `app/build/outputs/apk/debug/app-debug.apk`.
