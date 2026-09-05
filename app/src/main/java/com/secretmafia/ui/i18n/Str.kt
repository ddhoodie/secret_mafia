package com.secretmafia.ui.i18n

import com.secretmafia.game.AppLang
import com.secretmafia.game.DeathCause
import com.secretmafia.game.Role
import com.secretmafia.game.Winner

class Str(private val lang: AppLang) {
    private fun t(en: String, sr: String) = if (lang == AppLang.SR) sr else en

    val play = t("PLAY GAME", "IGRAJ")
    val settings = t("SETTINGS", "PODEŠAVANJA")
    val about = t("ABOUT", "O IGRI")
    val rules = t("RULES", "PRAVILA")
    val back = t("BACK", "NAZAD")
    val tagline = t("ONE PHONE. EVERYONE PLAYS.", "JEDAN TELEFON. SVI IGRAJU.")
    val appearance = t("APPEARANCE", "IZGLED")
    val gameplay = t("GAMEPLAY", "GEJMPLEJ")
    val comingSoon = t("COMING SOON", "USKORO")
    val night = t("NIGHT", "NOĆ")
    val day = t("DAY", "DAN")
    val roles = t("ROLES", "ULOGE")
    val round = t("ROUND", "RUNDA")
    val table = t("TABLE", "STO")
    val lightTheme = t("WHITE THEME", "BELA TEMA")
    val language = t("LANGUAGE", "JEZIK")
    val hideColors = t("HIDE GAME COLORS", "SAKRIJ BOJE U IGRI")
    val hideColorsHint = t("NO RED / GREEN DURING PLAY", "BEZ CRVENE / ZELENE U IGRI")
    val sound = t("SOUND", "ZVUK")
    val narrator = t("NARRATOR VOICE", "GLAS VODIČ")
    val narratorSoon = t("YOU WILL RECORD LINES LATER", "KASNIJE SNIMAŠ REČENICE")
    val voteCount = t("MAFIA VOTE COUNT", "BROJ GLASOVA MAFIJE")
    val discussTimer = t("DISCUSS TIMER", "TAJMER DISKUSIJE")
    val dummyAction = t("DUMMY ACTION", "LAŽNA AKCIJA")
    val revealRole = t("REVEAL ROLE ON DEATH", "OTKRIJ ULOGU POSLE SMRTI")
    val firstKill = t("FIRST NIGHT KILL", "UBISTVO PRVE NOĆI")
    val healerRepeat = t("REPEAT HEAL / GUARD", "ISTI CILJ ZAREDOM")
    val dayVote = t("DAY VOTE", "DNEVNO GLASANJE")
    val on = t("ON", "DA")
    val off = t("OFF", "NE")
    val live = t("LIVE", "UŽIVO")
    val phone = t("PHONE", "TELEFON")
    val like = t("LIKE", "LAJK")
    val math = t("MATH", "RAČUN")
    val random = t("RANDOM", "NASUMIČNO")
    val newGame = t("NEW GAME", "NOVA IGRA")
    val hostHint = t("TYPE YOURSELF FIRST, THEN GO AROUND THE TABLE.", "PRVO UPIŠI SEBE, ONDA IĆI U KRUG.")
    val hostYou = t("HOST / YOU", "HOST / TI")
    val seat = t("SEAT", "MESTO")
    val addPlayer = t("ADD PLAYER", "DODAJ IGRAČA")
    val removeLast = t("REMOVE LAST", "SKLONI POSLEDNJEG")
    val recommended = t("RECOMMENDED FOR", "PREPORUKA ZA")
    val players = t("PLAYERS", "IGRAČA")
    val advanced = t("ADVANCED ROLES", "NAPREDNE ULOGE")
    val resetRec = t("RESET TO RECOMMENDED", "VRATI PREPORUKU")
    val start = t("START GAME", "POKRENI")
    val startAnyway = t("START ANYWAY", "IPAK KRENI")
    val roleMismatch = t("ROLE TOTAL MUST EQUAL PLAYER COUNT.", "BROJ ULOGA MORA BITI JEDNAK BROJU IGRAČA.")
    val revealEnd = t("REVEAL ROLES AT END", "OTKRIJ ULOGE NA KRAJU")

    fun tooManyEvil(players: Int, evil: Int, good: Int, maxEvil: Int): String = t(
        "TOO MANY EVIL FOR $players PLAYERS. $evil EVIL / $good GOOD. TRY AT MOST $maxEvil EVIL.",
        "PREVIŠE ZLIH ZA $players LJUDI. $evil ZLIH / $good DOBRIH. PROBAJ NAJVIŠE $maxEvil ZLIH.",
    )
    val name = t("NAME", "IME")
    val passTo = t("PASS TO", "DAJ")
    val nobodyLooks = t("NOBODY ELSE LOOKS.", "NIKO DRUGI NE GLEDA.")
    val holdUnlock = t("HOLD TO UNLOCK", "DRŽI DA OTVORIŠ")
    val holdContinue = t("HOLD TO CONTINUE", "DRŽI ZA NASTAVAK")
    val holdVote = t("HOLD TO VOTE", "DRŽI ZA GLASANJE")
    val holdNight = t("HOLD FOR NIGHT", "DRŽI ZA NOĆ")
    val yourCrew = t("YOUR CREW", "TVOJA EKIPA")
    val whoDies = t("WHO DIES?", "KO UMIRJE?")
    val whoProtect = t("WHO DO YOU PROTECT?", "KOGA ŠTITIŠ?")
    val whoInspect = t("WHO DO YOU INSPECT?", "KOGA PROVERAVAŠ?")
    val whoExile = t("WHO GETS EXILED?", "KO ISPADA?")
    val sendLike = t("SEND A LIKE.", "POŠALJI LAJK.")
    val solveThis = t("SOLVE THIS.", "REŠI OVO.")
    val currentKill = t("CURRENT KILL", "TRENUTNA META")
    val votedKill = t("MAFIA VOTED TO KILL", "MAFIJA ŽELI DA UBIJE")
    val notAgain = t("NOT AGAIN TONIGHT.", "NE PONOVO VEČERAS.")
    val summary = t("SUMMARY", "REZIME")
    val nobodyDied = t("NOBODY DIED.", "NIKO NIJE UMRO.")
    val wasKilled = t("WAS KILLED.", "JE UBIJEN.")
    val wasExiled = t("WAS EXILED.", "JE IZBAČEN.")
    val nobodyExiled = t("NOBODY WAS EXILED.", "NIKO NIJE IZBAČEN.")
    val exileBlocked = t("THE EXILE WAS BLOCKED.", "IZBACIVANJE JE BLOKIRANO.")
    val fellFor = t("FELL INSTEAD.", "PAO JE UMESTO METE.")
    val diedWithTwin = t("DIED WITH THEIR TWIN.", "UMRO JE SA BLIZANCEM.")
    val takenByHunter = t("WAS TAKEN BY THE HUNTER.", "LOVAC GA JE POVEO.")
    val discuss = t("DISCUSS", "DISKUSIJA")
    val discussHint = t("TALK. ACCUSE. DEFEND.", "PRIČAJTE. OPTUŽITE. BRANITE SE.")
    val liveVote = t("LIVE VOTE", "GLAS UŽIVO")
    val liveHint = t("THE TABLE DECIDES. HOST TAPS THE RESULT.", "STO ODLUČI. HOST TAPNE REZULTAT.")
    val nobody = t("NOBODY", "NIKO")
    val exile = t("EXILE", "IZBACIVANJE")
    val gameOver = t("GAME OVER", "KRAJ")
    val mafiaWins = t("MAFIA WINS", "MAFIJA POBEĐUJE")
    val goodWins = t("GOOD WINS", "DOBRI POBEĐUJU")
    val jokerWins = t("JOKER WINS", "DŽOKER POBEĐUJE")
    val killerWins = t("KILLER WINS", "UBICA POBEĐUJE")
    val mainMenu = t("MAIN MENU", "GLAVNI MENI")
    val vote = t("VOTE", "GLASAJ")
    val dayVoteTitle = t("DAY VOTE", "DNEVNO GLASANJE")
    val good = t("GOOD", "DOBAR")
    val bad = t("BAD", "LOŠ")
    val unknownRole = t("???", "???")
    val skipShield = t("SKIP SHIELD", "PRESKOČI ŠTIT")
    val shieldHint = t("ONCE: SHIELD A MAFIA FROM EXILE.", "JEDNOM: ZAŠTITI MAFIJU OD IZBACIVANJA.")
    val hunterHint = t("TAKE SOMEONE WITH YOU.", "POVEDI NEKOGA SA SOBOM.")
    val tooDrunk = t("TOO DRUNK TO KNOW.", "PREVIŠE PIJAN DA ZNA.")
    val sober = t("YOU SOBERED UP.", "OTREZNIO SI SE.")
    val general = t("GENERAL", "GENERALNO")
    val winning = t("WINNING", "POBEDA")
    val evilRoles = t("EVIL", "ZLI")
    val goodRoles = t("GOOD", "DOBRI")
    val wildRoles = t("WILD", "WILD")
    val noneSelected = t("NONE SELECTED", "NIŠTA IZABRANO")
    val statistics = t("STATISTICS", "STATISTIKA")
    val statsSoon = t(
        "WINRATE, ROLE HISTORY, EXPORT. COMING SOON.",
        "WINRATE, ISTORIJA ULOGA, EXPORT. USKORO.",
    )
    val yourTwin = t("YOUR TWIN", "TVOJ BLIZANAC")

    fun seatN(n: Int) = "${seat} $n"
    fun nightN(n: Int) = "${night} $n"
    fun dayN(n: Int) = "${day} $n"
    fun recommendedFor(n: Int) = "$recommended $n $players"
    fun minLabel(m: Int) = if (m == 0) off else "$m MIN"
    fun roleTitle(role: Role?) = when (role) {
        Role.MAFIA -> t("MAFIA", "MAFIJA")
        Role.DON -> t("DON", "ŠEF")
        Role.LAWYER -> t("LAWYER", "ADVOKAT")
        Role.HEALER -> t("HEALER", "LEKAR")
        Role.COP -> t("COP", "POLICAJAC")
        Role.CIVILIAN -> t("CIVILIAN", "CIVIL")
        Role.HUNTER -> t("HUNTER", "LOVAC")
        Role.SEER -> t("SEER", "VIDOVNJAK")
        Role.BODYGUARD -> t("BODYGUARD", "TELOHRANITELJ")
        Role.JOKER -> t("JOKER", "DŽOKER")
        Role.KILLER -> t("KILLER", "UBICA")
        Role.LUNATIC -> t("LUNATIC", "LUDAK")
        Role.DRUNK -> t("DRUNK", "PIJANI")
        Role.TWIN_CIVIL -> t("CIVIL TWIN", "CIVIL BLIZANAC")
        Role.TWIN_MAFIA -> t("MAFIA TWIN", "MAFIJA BLIZANAC")
        null -> unknownRole
    }

    fun roleBlurb(role: Role) = when (role) {
        Role.MAFIA -> t("Evil. Each night votes who to kill. Sees other mafia.", "Zli. Svake noći glasa koga ubiti. Vidi ostalu mafiju.")
        Role.DON -> t("Mafia boss. Vote counts as stronger on ties.", "Šef mafije. Na nerešenom njegov glas teži više.")
        Role.LAWYER -> t("Mafia who also votes to kill. Once, can block a mafia exile.", "Mafija koja i ubija. Jednom može da blokira izbacivanje mafije.")
        Role.HEALER -> t("Good. Saves one player from tonight's kill.", "Dobri. Spasi jednog igrača od noćnog ubistva.")
        Role.COP -> t("Good. Learns if a player is GOOD or BAD.", "Dobri. Vidi da li je igrač DOBAR ili LOŠ.")
        Role.CIVILIAN -> t("Good. No power. Does a cover action.", "Dobri. Nema moć. Radi lažnu akciju.")
        Role.HUNTER -> t("If killed or exiled, takes one player along.", "Ako umre ili ispadne, vodi jednog igrača sa sobom.")
        Role.SEER -> t("Stronger cop. Sees the exact role.", "Jači policajac. Vidi tačnu ulogu.")
        Role.BODYGUARD -> t("Protects one player. If they are attacked, the bodyguard dies instead.", "Štiti jednog. Ako je meta napadnuta, telohranitelj umire umesto nje.")
        Role.JOKER -> t("Wins only if exiled during the day.", "Pobeđuje samo ako bude izbačen danju.")
        Role.KILLER -> t("Neutral. Kills every other night. Wins if last alive.", "Neutralan. Ubija svake druge noći. Pobeđuje ako ostane poslednji.")
        Role.LUNATIC -> t("Thinks they are a civilian. Actually counts as evil.", "Misli da je civil. Zapravo se broji kao zli.")
        Role.DRUNK -> t("Nights 1-2: no idea. Night 3: sobers up.", "Noći 1-2: ne zna ulogu. Treće noći se otrizni.")
        Role.TWIN_CIVIL -> t(
            "Two good twins. If one dies, the other dies too.",
            "Dva dobra blizanca. Ako jedan umre, umire i drugi.",
        )
        Role.TWIN_MAFIA -> t(
            "Two mafia twins. They vote with the crew. If one dies, the other dies too.",
            "Dva mafija blizanca. Glasaju sa ekipom. Ako jedan umre, umire i drugi.",
        )
    }

    fun winnerTitle(w: Winner) = when (w) {
        Winner.MAFIA -> mafiaWins
        Winner.GOOD -> goodWins
        Winner.JOKER -> jokerWins
        Winner.KILLER -> killerWins
    }

    fun deathLine(name: String, cause: DeathCause): String {
        val n = name.uppercase()
        return when (cause) {
            DeathCause.KILLED -> "$n $wasKilled"
            DeathCause.EXILED -> "$n $wasExiled"
            DeathCause.TWIN -> "$n $diedWithTwin"
            DeathCause.BODYGUARD -> "$n $fellFor"
            DeathCause.HUNTER -> "$n $takenByHunter"
            DeathCause.BLOCKED -> "$n — $exileBlocked"
            DeathCause.SAVED -> nobodyDied
        }
    }

    val generalBody = t(
        "One phone hosts the game. Sit in the order you type names. Pass the phone around each night. Unlock only when it is your turn. After night: summary, discuss, then a day vote. Live vote = the table decides and the host taps who is out. Phone vote = pass again.",
        "Jedan telefon vodi igru. Sedite redom kako upisujete imena. Telefon ide u krug svake noći. Otključaj samo na svom redu. Posle noći: rezime, diskusija, pa dnevno glasanje. Uživo = sto odluči, host tapne ko ispada. Telefon = opet se predaje.",
    )
    val nightBody = t(
        "Everyone living gets the phone. Power roles act in secret. Others do a dummy tap so nobody can tell who has a power. Then the night resolves: mafia votes, healer can save, bodyguard can take the hit, killer may strike on even nights.",
        "Svaki živi dobije telefon. Moćne uloge rade tajno. Ostali tapnu lažnu akciju da se ne vidi ko ima moć. Noć se rešava: mafija glasa, lekar može da spasi, telohranitelj može da primi udarac, ubica udara na parnim noćima.",
    )
    val dayBody = t(
        "Talk freely. Then vote someone out, or nobody. A tie on the phone vote saves everyone. The lawyer can block one mafia exile. The hunter, if they just died, still picks one last target.",
        "Pričajte slobodno. Onda izbacite nekoga, ili nikoga. Nerešeno na telefonu = niko ne ispada. Advokat može jednom da blokira izbacivanje mafije. Lovac, ako je upravo umro, bira još jednu metu.",
    )
    val winBody = t(
        "Mafia wins if living evil is greater or equal to living good. Neutrals do not count in that race. Good wins when no evil remain and the killer is gone. Joker wins only if day-exiled. Killer wins if they are the last player alive.",
        "Mafija pobeđuje ako je živih zlih više ili jednako živim dobrima. Neutralni se ne broje. Dobri pobeđuju kad nema zlih i kad nema ubice. Džoker pobeđuje samo ako ispadne danju. Ubica pobeđuje ako ostane poslednji.",
    )
    val about1 = t(
        "One phone hosts a live mafia game so nobody has to sit out as narrator.",
        "Jedan telefon vodi uživu mafija igru, niko ne sedi kao voditelj.",
    )
    val about2 = t(
        "Pass the phone around the table. Each player unlocks their role, takes a secret night action, then locks the screen before handing it on.",
        "Telefon ide oko stola. Svako otključa ulogu, uradi noćnu akciju, pa zaključa ekran pre predaje.",
    )
    val about3 = t(
        "Mafia hunt at night. Town tries to vote them out by day. Advanced roles change the math. Read Rules before the first messy game.",
        "Mafija lovi noću. Grad ih izbacuje danju. Napredne uloge menjaju računicu. Pročitaj Pravila pre prve haotične partije.",
    )
}
