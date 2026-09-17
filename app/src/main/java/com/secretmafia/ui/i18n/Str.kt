package com.secretmafia.ui.i18n

import com.secretmafia.game.AppLang
import com.secretmafia.game.CoinKind
import com.secretmafia.game.DeathCause
import com.secretmafia.game.Role
import com.secretmafia.game.WhoreAlign
import com.secretmafia.game.Winner

class Str(private val lang: AppLang) {
    private fun t(
        en: String,
        sr: String,
        it: String,
        es: String,
        de: String,
        fr: String,
        zh: String,
    ) = when (lang) {
        AppLang.EN -> en
        AppLang.SR -> sr
        AppLang.IT -> it
        AppLang.ES -> es
        AppLang.DE -> de
        AppLang.FR -> fr
        AppLang.ZH -> zh
    }

    val play = t("PLAY GAME", "IGRAJ", "GIOCA", "JUGAR", "SPIELEN", "JOUER", "开始游戏")
    val settings = t("SETTINGS", "PODEŠAVANJA", "IMPOSTAZIONI", "AJUSTES", "EINSTELLUNGEN", "PARAMÈTRES", "设置")
    val about = t("ABOUT", "O IGRI", "INFO", "ACERCA DE", "ÜBER DAS SPIEL", "À PROPOS", "关于")
    val rules = t("RULES", "PRAVILA", "REGOLE", "REGLAS", "REGELN", "RÈGLES", "规则")
    val back = t("BACK", "NAZAD", "INDIETRO", "ATRÁS", "ZURÜCK", "RETOUR", "返回")
    /** Full in-app brand title. Launcher icon label stays "Secret Mafia" via strings.xml. */
    val brandTitle = "ONE PHONE SECRET MAFIA"
    val brandTitleLine1 = "ONE PHONE"
    val brandTitleLine2 = "SECRET MAFIA"
    val tagline = t(
        "ONE PHONE. EVERYONE PLAYS.",
        "JEDAN TELEFON. SVI IGRAJU.",
        "UN TELEFONO. GIOCANO TUTTI.",
        "UN TELÉFONO. TODOS JUEGAN.",
        "EIN HANDY. ALLE SPIELEN.",
        "UN TÉLÉPHONE. TOUT LE MONDE JOUE.",
        "一部手机。所有人一起玩。",
    )
    val appearance = t("APPEARANCE", "IZGLED", "ASPETTO", "APARIENCIA", "AUSSEHEN", "APPARENCE", "外观")
    val gameplay = t("GAMEPLAY", "GEJMPLEJ", "GIOCO", "JUEGO", "SPIELABLAUF", "GAMEPLAY", "玩法")
    val comingSoon = t("COMING SOON", "USKORO", "PROSSIMAMENTE", "PRÓXIMAMENTE", "DEMNÄCHST", "BIENTÔT", "即将推出")
    val night = t("NIGHT", "NOĆ", "NOTTE", "NOCHE", "NACHT", "NUIT", "夜晚")
    val day = t("DAY", "DAN", "GIORNO", "DÍA", "TAG", "JOUR", "白天")
    val roles = t("ROLES", "ULOGE", "RUOLI", "ROLES", "ROLLEN", "RÔLES", "身份")
    val round = t("ROUND", "RUNDA", "ROUND", "RONDA", "RUNDE", "MANCHE", "回合")
    val table = t("TABLE", "STO", "TAVOLO", "MESA", "TISCH", "TABLE", "牌桌")
    val lightTheme = t("WHITE THEME", "BELA TEMA", "TEMA CHIARO", "TEMA CLARO", "HELLS THEMA", "THÈME CLAIR", "浅色主题")
    val language = t("LANGUAGE", "JEZIK", "LINGUA", "IDIOMA", "SPRACHE", "LANGUE", "语言")
    val hideColors = t("HIDE GAME COLORS", "SAKRIJ BOJE U IGRI", "NASCONDI I COLORI", "OCULTAR COLORES", "FARBEN VERSTECKEN", "CACHER LES COULEURS", "隐藏游戏颜色")
    val hideColorsHint = t(
        "NO RED / GREEN DURING PLAY",
        "BEZ CRVENE / ZELENE U IGRI",
        "NI ROSSO / VERDE IN PARTITA",
        "SIN ROJO / VERDE EN JUEGO",
        "KEIN ROT / GRÜN IM SPIEL",
        "PAS DE ROUGE / VERT EN JEU",
        "对局中不显示红/绿",
    )
    val sound = t("SOUND", "ZVUK", "SUONO", "SONIDO", "TON", "SON", "声音")
    val vibration = t("VIBRATION", "VIBRACIJA", "VIBRAZIONE", "VIBRACIÓN", "VIBRATION", "VIBRATION", "振动")
    val narrator = t("NARRATOR VOICE", "GLAS VODIČ", "VOCE NARRATORE", "VOZ DEL NARRADOR", "ERZÄHLERSTIMME", "VOIX DU NARRATEUR", "旁白声音")
    val voteCount = t("MAFIA VOTE COUNT", "BROJ GLASOVA MAFIJE", "VOTI MAFIA", "VOTOS MAFIA", "MAFIA-STIMMEN", "VOTES MAFIA", "黑手党票数")
    val discussTimer = t("DISCUSS TIMER", "TAJMER DISKUSIJE", "TIMER DISCUSSIONE", "TEMPORIZADOR", "DISKUSIONS-TIMER", "MINUTEUR DÉBAT", "讨论计时")
    val dummyAction = t("DUMMY ACTION", "LAŽNA AKCIJA", "AZIONE FINTA", "ACCIÓN FALSA", "SCHEINAKTION", "FAUSSE ACTION", "伪装行动")
    val revealRole = t("REVEAL ROLE ON DEATH", "OTKRIJ ULOGU POSLE SMRTI", "RIVELA RUOLO ALLA MORTE", "REVELAR ROL AL MORIR", "ROLLE BEIM TOD ZEIGEN", "RÉVÉLER LE RÔLE À LA MORT", "死亡时公布身份")
    val firstKill = t("FIRST NIGHT KILL", "UBISTVO PRVE NOĆI", "UCCISIONE PRIMA NOTTE", "MUERTE PRIMERA NOCHE", "ERSTER NACHTMORD", "MEURTRE PREMIÈRE NUIT", "第一夜击杀")
    val mafiaConfer = t("MAFIA WAKE", "MAFIJA SE BUDI", "SVEGLIA MAFIA", "DESPERTAR MAFIA", "MAFIA WACHT", "RÉVEIL MAFIA", "黑手党醒来")
    val mafiaConferHint = t(
        "EVERYONE SLEEPS. MAFIA LOOKS UP, SEES THE CREW, TALKS WITH HEADS. THEN THEY SLEEP. THEN EVERYONE WAKES AND ACTS ON THE PHONE.",
        "SVI SPAVAJU. MAFIJA POGLEDA GORE, VIDI SVOJE, DOGOVORI SE GLAVAMA. ONDA SPAVA. ONDA SE SVI BUDE I RADI NA TELEFONU.",
        "TUTTI DORMONO. LA MAFIA ALZA LA TESTA, VEDE LA CREW, PARLA. POI DORME. POI TUTTI SI SVEGLIANO E AGISCONO SUL TELEFONO.",
        "TODOS DUERMEN. LA MAFIA LEVANTA LA CABEZA, VE A LOS SUYOS, HABLA. LUEGO DUERME. LUEGO TODOS DESPIERTAN Y ACTÚAN EN EL TELÉFONO.",
        "ALLE SCHLAFEN. DIE MAFIA SCHAUT HOCH, SIEHT DIE CREW, REDET. DANN SCHLÄFT SIE. DANN WACHEN ALLE AUF UND HANDELN AM HANDY.",
        "TOUT LE MONDE DORT. LA MAFIA LEVE LA TÊTE, VOIT L'ÉQUIPE, PARLE. PUIS ELLE DORT. PUIS TOUT LE MONDE SE RÉVEILLE ET AGIT SUR LE TÉLÉPHONE.",
        "所有人睡觉。黑手党抬头互认、点头商量。然后睡觉。然后所有人醒来，在手机上行动。",
    )
    val mafiaTalkTime = t("MAFIA TALK TIME", "VREME MAFIJE", "TEMPO MAFIA", "TIEMPO MAFIA", "MAFIA-ZEIT", "TEMPS MAFIA", "黑手党商量时间")
    val everyoneSleep = t("EVERYONE SLEEP", "SVI SPAVAJU", "TUTTI DORMONO", "TODOS DUERMEN", "ALLE SCHLAFEN", "TOUT LE MONDE DORT", "所有人睡觉")
    val mafiaWake = t("MAFIA WAKE UP", "MAFIJA SE BUDI", "MAFIA SVEGLIATI", "MAFIA DESPIERTA", "MAFIA WACHT AUF", "MAFIA RÉVEILLE-TOI", "黑手党醒来")
    val mafiaTalk = t("MAFIA TALK", "MAFIJA SE DOGOVARA", "MAFIA PARLA", "MAFIA HABLA", "MAFIA REDET", "MAFIA PARLE", "黑手党商量")
    val mafiaSleep = t("MAFIA SLEEP", "MAFIJA SPAVA", "MAFIA DORMI", "MAFIA DUERME", "MAFIA SCHLÄFT", "MAFIA DORS", "黑手党睡觉")
    val everyoneWake = t("EVERYONE WAKE UP", "SVI SE BUDE", "TUTTI SVEGLIATEVI", "TODOS DESPIERTEN", "ALLE AUFWACHEN", "TOUT LE MONDE SE RÉVEILLE", "所有人醒来")
    val mafiaTalkHint = t("LOOK UP. SEE YOUR CREW. NOD.", "POGLEDAJTE GORE. VIDITE SVOJE. KLIMNITE.", "ALZATE LA TESTA. VEDI LA CREW. ANNUITE.", "MIRAD ARRIBA. VED A LOS VUESTROS. ASENTID.", "SCHAUT HOCH. SEHT EURE CREW. NICKT.", "LEVEZ LA TÊTE. VOYEZ VOTRE ÉQUIPE. HOCHEZ.", "抬头。认同伙。点头。")
    val skip = t("SKIP", "PRESKOČI", "SALTA", "SALTAR", "ÜBERSPRINGEN", "PASSER", "跳过")
    val animDebug = t("ANIM DEBUG", "ANIM DEBUG", "DEBUG ANIM", "DEBUG ANIM", "ANIM-DEBUG", "DEBUG ANIM", "动画调试")
    val twoSteps = t("2 STEPS", "2 KORAKA", "2 PASSI", "2 PASOS", "2 SCHRITTE", "2 ÉTAPES", "两步")
    val healerRepeat = t("REPEAT HEAL / GUARD", "ISTI CILJ ZAREDOM", "RIPETI CURA / GUARDIA", "REPETIR CURA / GUARDIA", "HEIL / WACHE WIEDERHOLEN", "RÉPÉTER SOIN / GARDE", "重复治疗/守护")
    val dayVote = t("DAY VOTE", "DNEVNO GLASANJE", "VOTO DIURNO", "VOTO DIURNO", "TAGESABSTIMMUNG", "VOTE DU JOUR", "白天投票")
    val on = t("ON", "DA", "SÌ", "SÍ", "AN", "OUI", "开")
    val off = t("OFF", "NE", "NO", "NO", "AUS", "NON", "关")
    val live = t("LIVE", "UŽIVO", "DAL VIVO", "EN VIVO", "LIVE", "EN DIRECT", "现场")
    val phone = t("PHONE", "TELEFON", "TELEFONO", "TELÉFONO", "HANDY", "TÉLÉPHONE", "手机")
    val like = t("LIKE", "LAJK", "LIKE", "LIKE", "LIKE", "LIKE", "点赞")
    val math = t("MATH", "RAČUN", "MATE", "MATES", "RECHNEN", "MATHS", "算术")
    val random = t("RANDOM", "NASUMIČNO", "CASUALE", "ALEATORIO", "ZUFALL", "ALÉATOIRE", "随机")
    val newGame = t("NEW GAME", "NOVA IGRA", "NUOVA PARTITA", "NUEVA PARTIDA", "NEUES SPIEL", "NOUVELLE PARTIE", "新对局")
    val hostHint = t(
        "TYPE YOURSELF FIRST, THEN GO AROUND THE TABLE.",
        "PRVO UPIŠI SEBE, ONDA IĆI U KRUG.",
        "SCRIVI PRIMA TE, POI GIRA IL TAVOLO.",
        "ESCRÍBETE PRIMERO, LUEGO DA LA VUELTA.",
        "Zuerst dich eintragen, dann um den Tisch.",
        "INSCRIS-TOI D'ABORD, PUIS TOURNE AUTOUR DE LA TABLE.",
        "先填自己，再按座位一圈填。",
    )
    val hostYou = t("HOST / YOU", "HOST / TI", "HOST / TU", "ANFITRIÓN / TÚ", "HOST / DU", "HÔTE / TOI", "房主 / 你")
    val seat = t("SEAT", "MESTO", "POSTO", "ASIENTO", "PLATZ", "PLACE", "座位")
    val addPlayer = t("ADD PLAYER", "DODAJ IGRAČA", "AGGIUNGI GIOCATORE", "AÑADIR JUGADOR", "SPIELER HINZUFÜGEN", "AJOUTER UN JOUEUR", "添加玩家")
    val removeLast = t("REMOVE LAST", "SKLONI POSLEDNJEG", "TOGLI L'ULTIMO", "QUITAR EL ÚLTIMO", "LETZTEN ENTFERNEN", "RETIRER LE DERNIER", "移除最后一位")
    val recommended = t("RECOMMENDED FOR", "PREPORUKA ZA", "CONSIGLIATO PER", "RECOMENDADO PARA", "EMPFOHLEN FÜR", "RECOMMANDÉ POUR", "推荐人数")
    val players = t("PLAYERS", "IGRAČA", "GIOCATORI", "JUGADORES", "SPIELER", "JOUEURS", "人")
    val advanced = t("ADVANCED ROLES", "NAPREDNE ULOGE", "RUOLI AVANZATI", "ROLES AVANZADOS", "SPEZIALROLLEN", "RÔLES AVANCÉS", "进阶身份")
    val resetRec = t("RESET TO RECOMMENDED", "VRATI PREPORUKU", "RIPRISTINA CONSIGLIO", "RESTAURAR RECOMENDADO", "EMPFEHLUNG ZURÜCK", "REMETTRE LE CONSEIL", "恢复推荐")
    val clearSetup = t("CLEAR SETUP", "OBRIŠI SETUP", "AZZERA SETUP", "BORRAR SETUP", "SETUP LÖSCHEN", "EFFACER LE SETUP", "清空配置")
    val start = t("START GAME", "POKRENI", "INIZIA", "EMPEZAR", "START", "LANCER", "开始")
    val startAnyway = t("START ANYWAY", "IPAK KRENI", "INIZIA LO STESSO", "EMPEZAR IGUAL", "TROTZDEM STARTEN", "LANCER QUAND MÊME", "仍然开始")
    val roleMismatch = t(
        "ROLE TOTAL MUST EQUAL PLAYER COUNT.",
        "BROJ ULOGA MORA BITI JEDNAK BROJU IGRAČA.",
        "I RUOLI DEVONO ESSERE QUANTI I GIOCATORI.",
        "LOS ROLES DEBEN IGUALAR A LOS JUGADORES.",
        "ROLLENZAHL MUSS SPIELERZAHL SEIN.",
        "LE NOMBRE DE RÔLES DOIT ÉGALE LES JOUEURS.",
        "身份数量必须等于玩家数。",
    )
    fun rolesVsPlayers(roles: Int, players: Int): String = t(
        "ROLES $roles / PLAYERS $players.",
        "ULOGE $roles / IGRAČI $players.",
        "RUOLI $roles / GIOCATORI $players.",
        "ROLES $roles / JUGADORES $players.",
        "ROLLEN $roles / SPIELER $players.",
        "RÔLES $roles / JOUEURS $players.",
        "身份 $roles / 玩家 $players。",
    )
    val twinsNeedPair = t(
        "TWINS MUST BE EVEN (0, 2, 4…).",
        "BLIZANCI MORAJU BITI PARNI (0, 2, 4…).",
        "I GEMELLI DEVONO ESSERE PARI (0, 2, 4…).",
        "LOS GEMELOS DEBEN SER PARES (0, 2, 4…).",
        "ZWILLINGE MÜSSEN GERADE SEIN (0, 2, 4…).",
        "LES JUMEAUX DOIVENT ÊTRE PAIRS (0, 2, 4…).",
        "双胞胎必须是偶数（0、2、4…）。",
    )
    val revealEnd = t("REVEAL ROLES AT END", "OTKRIJ ULOGE NA KRAJU", "RIVELA RUOLI ALLA FINE", "REVELAR ROLES AL FINAL", "ROLLEN AM ENDE ZEIGEN", "RÉVÉLER LES RÔLES À LA FIN", "结束时公布身份")

    fun tooManyEvil(players: Int, evil: Int, good: Int, maxEvil: Int): String = t(
        "TOO MANY EVIL FOR $players PLAYERS. $evil EVIL / ${players - evil} NON-EVIL. NEED MORE NON-EVIL THAN EVIL.",
        "PREVIŠE ZLIH ZA $players LJUDI. $evil ZLIH / ${players - evil} NEZLIH. TREBA VIŠE NEZLIH NEGO ZLIH.",
        "TROPPI CATTIVI PER $players GIOCATORI. $evil CATTIVI / ${players - evil} NON CATTIVI. SERVONO PIÙ NON CATTIVI CHE CATTIVI.",
        "DEMASIADO MAL PARA $players JUGADORES. $evil MAL / ${players - evil} NO MAL. DEBE HABER MÁS NO MAL QUE MAL.",
        "ZU VIELE BÖSE FÜR $players SPIELER. $evil BÖSE / ${players - evil} NICHT-BÖSE. MEHR NICHT-BÖSE ALS BÖSE NÖTIG.",
        "TROP DE MÉCHANTS POUR $players JOUEURS. $evil MÉCHANTS / ${players - evil} NON MÉCHANTS. IL FAUT PLUS DE NON MÉCHANTS QUE DE MÉCHANTS.",
        "$players 人局里坏人太多。$evil 坏 / ${players - evil} 非坏。非坏人必须多于坏人。",
    )
    fun optimalSplit(players: Int, evil: Int, good: Int): String = t(
        "OPTIMAL $evil EVIL / $good GOOD FOR $players PLAYERS.",
        "OPTIMALNO $evil ZLIH / $good DOBRIH ZA $players LJUDI.",
        "OTTIMO: $evil CATTIVI / $good BUONI PER $players GIOCATORI.",
        "ÓPTIMO: $evil MAL / $good BIEN PARA $players JUGADORES.",
        "OPTIMAL: $evil BÖSE / $good GUTE FÜR $players SPIELER.",
        "IDÉAL : $evil MÉCHANTS / $good BONS POUR $players JOUEURS.",
        "$players 人推荐：$evil 坏 / $good 好。",
    )
    fun currentSplit(evil: Int, good: Int): String = t(
        "NOW $evil EVIL / $good GOOD.",
        "SAD $evil ZLIH / $good DOBRIH.",
        "ORA $evil CATTIVI / $good BUONI.",
        "AHORA $evil MAL / $good BIEN.",
        "JETZT $evil BÖSE / $good GUTE.",
        "MAINTENANT $evil MÉCHANTS / $good BONS.",
        "当前 $evil 坏 / $good 好。",
    )
    fun currentSplitFull(evil: Int, good: Int, neutral: Int): String = t(
        "NOW $evil EVIL / $good GOOD / $neutral WILD.",
        "SAD $evil ZLIH / $good DOBRIH / $neutral WILD.",
        "ORA $evil CATTIVI / $good BUONI / $neutral WILD.",
        "AHORA $evil MAL / $good BIEN / $neutral WILD.",
        "JETZT $evil BÖSE / $good GUTE / $neutral WILD.",
        "MAINTENANT $evil MÉCHANTS / $good BONS / $neutral WILD.",
        "当前 $evil 坏 / $good 好 / $neutral 狂野。",
    )
    fun roleCountLine(role: String, n: Int): String = "$role ×$n"
    val coreRoles = t("CORE", "OSNOVNE", "BASE", "BÁSICO", "KERN", "BASE", "基础")
    val roster = t("ROSTER", "SASTAV", "ROSTER", "PLANTILLA", "AUFSTELLUNG", "COMPOSITION", "阵容")
    val name = t("NAME", "IME", "NOME", "NOMBRE", "NAME", "NOM", "名字")
    val passTo = t("PASS TO", "DAJ", "PASSA A", "PASA A", "GIB AN", "PASSE À", "传给")
    val nobodyLooks = t("NOBODY ELSE LOOKS.", "NIKO DRUGI NE GLEDA.", "NESSUN ALTRO GUARDA.", "NADIE MÁS MIRA.", "NIEMAND SONST SCHAUT.", "PERSONNE D'AUTRE NE REGARDE.", "别人不许看。")
    val holdUnlock = t("HOLD TO UNLOCK", "DRŽI DA OTVORIŠ", "TIENI PER SBLOCCARE", "MANTÉN PARA ABRIR", "HALTEN ZUM ÖFFNEN", "MAINTIENS POUR OUVRIR", "长按解锁")
    val holdContinue = t("HOLD TO CONTINUE", "DRŽI ZA NASTAVAK", "TIENI PER CONTINUARE", "MANTÉN PARA SEGUIR", "HALTEN FÜR WEITER", "MAINTIENS POUR CONTINUER", "长按继续")
    val holdVote = t("HOLD TO VOTE", "DRŽI ZA GLASANJE", "TIENI PER VOTARE", "MANTÉN PARA VOTAR", "HALTEN ZUM ABSTIMMEN", "MAINTIENS POUR VOTER", "长按投票")
    val holdNight = t("HOLD FOR NIGHT", "DRŽI ZA NOĆ", "TIENI PER LA NOTTE", "MANTÉN PARA LA NOCHE", "HALTEN FÜR NACHT", "MAINTIENS POUR LA NUIT", "长按进入夜晚")
    val yourCrew = t("YOUR CREW", "TVOJA EKIPA", "LA TUA CREW", "TU BANDA", "DEINE CREW", "TON ÉQUIPE", "你的同伙")
    val whoDies = t("WHO DIES?", "KO UMIRE?", "CHI MUORE?", "¿QUIÉN MUERE?", "WER STIRBT?", "QUI MEURT ?", "谁死？")
    val whoProtect = t("WHO DO YOU PROTECT?", "KOGA ŠTITIŠ?", "CHI PROTEGGI?", "¿A QUIÉN PROTEGES?", "WEN SCHÜTZT DU?", "QUI PROTÈGES-TU ?", "保护谁？")
    val whoInspect = t("WHO DO YOU INSPECT?", "KOGA PROVERAVAŠ?", "CHI CONTROLLI?", "¿A QUIÉN INVESTIGAS?", "WEN PRÜFST DU?", "QUI INSPECTES-TU ?", "查谁？")
    val whoSleep = t("WHO DO YOU SLEEP WITH?", "S KIM SPAVAŠ?", "CON CHI DORMI?", "¿CON QUIÉN DUERMES?", "MIT WEM SCHLÄFST DU?", "AVEC QUI DORS-TU ?", "和谁睡？")
    val pickSide = t("HOW DO YOU PLAY?", "KAKO IGRAŠ?", "COME GIOCHI?", "¿CÓMO JUEGAS?", "WIE SPIELST DU?", "COMMENT JOUES-TU ?", "你怎么玩？")
    val sideGood = t("GOOD", "DOBRA", "BUONA", "BUENA", "GUT", "BIEN", "好人")
    val sideEvil = t("EVIL", "ZLA", "CATTIVA", "MALA", "BÖSE", "MAL", "坏人")
    val sidePest = t("JUST A PEST", "SAMO SMETA", "SOLO DISTURBA", "SOLO MOLESTA", "NUR STÖRERIN", "JUSTE GÊNE", "只捣乱")
    val sideSolo = t("PLAY ALONE", "IGRA SAMA", "GIOCA DA SOLA", "JUEGA SOLA", "SPIELT ALLEIN", "JOUE SEULE", "独自获胜")
    val sidePick = t("PICK IN GAME", "BIRA U IGRI", "SCEGLIE IN PARTITA", "ELIGE EN PARTIDA", "WAHLT IM SPIEL", "CHOISIT EN PARTIE", "开局自选")
    val whoreHow = t("HOW THIS ROLE WORKS", "KAKO OVA ULOGA RADI", "COME FUNZIONA", "CÓMO FUNCIONA", "SO FUNKTIONIERT DIE ROLLE", "COMMENT ÇA MARCHE", "这个身份怎么玩")
    val notLastNight = t("NOT LAST NIGHT'S NAME.", "NE SINOĆNJE IME.", "NON QUELLO DI IERI NOTTE.", "NO EL DE ANOCHE.", "NICHT DER VON GESTERN NACHT.", "PAS CELUI D'HIER SOIR.", "不能连续两夜同一人。")
    fun whoreAlignLabel(align: WhoreAlign) = when (align) {
        WhoreAlign.GOOD -> sideGood
        WhoreAlign.EVIL -> sideEvil
        WhoreAlign.PEST -> sidePest
        WhoreAlign.SOLO -> sideSolo
        WhoreAlign.PICK -> sidePick
    }
    val whoExile = t("WHO GETS EXILED?", "KO ISPADA?", "CHI VIENE ESILIATO?", "¿QUIÉN ES EXPULSADO?", "WEN WIRFT IHR RAUS?", "QUI EST EXILÉ ?", "放逐谁？")
    val sendLike = t("SEND A LIKE.", "POŠALJI LAJK.", "MANDA UN LIKE.", "MANDA UN LIKE.", "SCHICK EIN LIKE.", "ENVOIE UN LIKE.", "点个赞。")
    val solveThis = t("SOLVE THIS.", "REŠI OVO.", "RISOLVI QUESTO.", "RESUELVE ESTO.", "LÖS DAS.", "RÉSOUS ÇA.", "算出这题。")
    val currentKill = t("CURRENT KILL", "TRENUTNA META", "KILL ATTUALE", "MUERTE ACTUAL", "AKTUELLES ZIEL", "CIBLE ACTUELLE", "当前击杀")
    val votedKill = t("MAFIA VOTED TO KILL", "MAFIJA ŽELI DA UBIJE", "LA MAFIA VOTA DI UCCIDERE", "LA MAFIA VOTA MATAR", "MAFIA STIMMT FÜR MORD", "LA MAFIA VOTE DE TUER", "黑手党要杀")
    val notAgain = t("NOT AGAIN TONIGHT.", "NE PONOVO VEČERAS.", "NON DI NUOVO STASERA.", "NO OTRA VEZ ESTA NOCHE.", "NICHT NOCHMAL HEUTE NACHT.", "PAS ENCORE CETTE NUIT.", "今晚不能再选这个。")
    val summary = t("SUMMARY", "REZIME", "RIEPILOGO", "RESUMEN", "ZUSAMMENFASSUNG", "RÉSUMÉ", "结算")
    val nobodyDied = t("NOBODY DIED.", "NIKO NIJE UMRO.", "NESSUNO È MORTO.", "NADIE MURIÓ.", "NIEMAND IST GESTORBEN.", "PERSONNE N'EST MORT.", "无人死亡。")
    val wasKilled = t("WAS KILLED.", "JE UBIJEN.", "È STATO UCCISO.", "FUE ASESINADO.", "WURDE GETÖTET.", "A ÉTÉ TUÉ.", "被杀了。")
    val wasExiled = t("WAS EXILED.", "JE IZBAČEN.", "È STATO ESILIATO.", "FUE EXPULSADO.", "WURDE RAUSGEWORFEN.", "A ÉTÉ EXILÉ.", "被放逐了。")
    val nobodyExiled = t("NOBODY WAS EXILED.", "NIKO NIJE IZBAČEN.", "NESSUNO ESILIATO.", "NADIE FUE EXPULSADO.", "NIEMAND WURDE RAUSGEWORFEN.", "PERSONNE N'A ÉTÉ EXILÉ.", "无人被放逐。")
    val exileBlocked = t("THE EXILE WAS BLOCKED.", "IZBACIVANJE JE BLOKIRANO.", "L'ESILIO È STATO BLOCCATO.", "EL EXILIO FUE BLOQUEADO.", "DER RAUSWURF WURDE BLOCKIERT.", "L'EXIL A ÉTÉ BLOQUÉ.", "放逐被挡住了。")
    val fellFor = t("FELL INSTEAD.", "PAO JE UMESTO METE.", "È CADUTO AL POSTO SUO.", "CAYÓ EN SU LUGAR.", "FIEL STATTDESSEN.", "EST TOMBÉ À SA PLACE.", "替他挡了刀。")
    val diedWithTwin = t("DIED WITH THEIR TWIN.", "UMRO JE SA BLIZANCEM.", "È MORTO COL GEMELLO.", "MURIÓ CON SU GEMELO.", "STARB MIT DEM ZWILLING.", "EST MORT AVEC SON JUMEAU.", "和双胞胎一起死了。")
    val takenByHunter = t("WAS TAKEN BY THE HUNTER.", "LOVAC GA JE POVEO.", "IL CACCIATORE L'HA PORTATO VIA.", "EL CAZADOR SE LO LLEVÓ.", "DER JÄGER NAHM IHN MIT.", "LE CHASSEUR L'A EMPORTÉ.", "被猎人带走了。")
    val discuss = t("DISCUSS", "DISKUSIJA", "DISCUSSIONE", "DEBATE", "DISKUSSION", "DÉBAT", "讨论")
    val discussHint = t("TALK. ACCUSE. DEFEND.", "PRIČAJTE. OPTUŽITE. BRANITE SE.", "PARLATE. ACCUSATE. DIFENDETEVI.", "HABLEN. ACUSEN. DEFÍENDANSE.", "REDET. BESCHULDIGT. VERTEIDIGT EUCH.", "PARLEZ. ACCUSEZ. DÉFENDEZ-VOUS.", "说话。指控。辩解。")
    val liveVote = t("LIVE VOTE", "GLAS UŽIVO", "VOTO DAL VIVO", "VOTO EN VIVO", "LIVE-ABSTIMMUNG", "VOTE EN DIRECT", "现场投票")
    val liveHint = t(
        "THE TABLE DECIDES. HOST TAPS THE RESULT.",
        "STO ODLUČI. HOST TAPNE KO ISPADA.",
        "IL TAVOLO DECIDE. L'HOST TOCCA IL RISULTATO.",
        "LA MESA DECIDE. EL ANFITRIÓN TOCA EL RESULTADO.",
        "DER TISCH ENTSCHEIDET. DER HOST TIPPT DAS ERGEBNIS.",
        "LA TABLE DÉCIDE. L'HÔTE TAPE LE RÉSULTAT.",
        "牌桌决定。房主点结果。",
    )
    val nobody = t("NOBODY", "NIKO", "NESSUNO", "NADIE", "NIEMAND", "PERSONNE", "无人")
    val exile = t("EXILE", "IZBACIVANJE", "ESILIO", "EXILIO", "RAUSWURF", "EXIL", "放逐")
    val gameOver = t("GAME OVER", "KRAJ", "FINE PARTITA", "FIN DEL JUEGO", "SPIELENDE", "FIN DE PARTIE", "游戏结束")
    val mafiaWins = t("MAFIA WINS", "MAFIJA POBEĐUJE", "VINCE LA MAFIA", "GANA LA MAFIA", "MAFIA GEWINNT", "LA MAFIA GAGNE", "黑手党胜利")
    val goodWins = t("GOOD WINS", "DOBRI POBEĐUJU", "VINCONO I BUONI", "GANA EL BIEN", "DIE GUTEN GEWINNEN", "LES BONS GAGNENT", "好人胜利")
    val jokerWins = t("JOKER WINS", "DŽOKER POBEĐUJE", "VINCE LO JOKER", "GANA EL JOKER", "JOKER GEWINNT", "LE JOKER GAGNE", "小丑胜利")
    val killerWins = t("KILLER WINS", "UBICA POBEĐUJE", "VINCE IL KILLER", "GANA EL ASESINO", "KILLER GEWINNT", "LE TUEUR GAGNE", "杀手胜利")
    val whoreWins = t("WHORE WINS", "KURVA POBEĐUJE", "VINCE LA PUTTANA", "GANA LA PUTA", "HURE GEWINNT", "LA PUTAIN GAGNE", "妓女胜利")
    val mainMenu = t("MAIN MENU", "GLAVNI MENI", "MENU PRINCIPALE", "MENÚ PRINCIPAL", "HAUPTMENÜ", "MENU PRINCIPAL", "主菜单")
    val leaveTitle = t("LEAVE THE GAME?", "IZAĆI IZ IGRE?", "USCIRE DALLA PARTITA?", "¿SALIR DE LA PARTIDA?", "SPIEL VERLASSEN?", "QUITTER LA PARTIE ?", "退出对局？")
    val leaveHint = t(
        "THE MATCH ENDS. THIS CAN'T BE UNDONE.",
        "PARTIJA SE PREKIDA. TO SE NE MOŽE VRATITI.",
        "LA PARTITA FINISCE. NON SI TORNA INDIETRO.",
        "LA PARTIDA TERMINA. NO SE PUEDE DESHACER.",
        "DIE RUNDE ENDET. DAS GEHT NICHT ZURÜCK.",
        "LA PARTIE S'ARRÊTE. IRRÉVERSIBLE.",
        "对局将结束。无法撤销。",
    )
    val leave = t("LEAVE", "IZAĐI", "ESCI", "SALIR", "GEHEN", "QUITTER", "退出")
    val stay = t("STAY", "OSTANI", "RESTA", "QUEDARSE", "BLEIBEN", "RESTER", "留下")
    val vote = t("VOTE", "GLASAJ", "VOTA", "VOTAR", "ABSTIMMEN", "VOTER", "投票")
    val dayVoteTitle = t("DAY VOTE", "DNEVNO GLASANJE", "VOTO DIURNO", "VOTO DIURNO", "TAGESABSTIMMUNG", "VOTE DU JOUR", "白天投票")
    val good = t("GOOD", "DOBAR", "BUONO", "BUENO", "GUT", "BON", "好人")
    val bad = t("BAD", "LOŠ", "CATTIVO", "MALO", "BÖSE", "MAUVAIS", "坏人")
    val unknownRole = t("???", "???", "???", "???", "???", "???", "???")
    val moreInfo = t("MORE INFO", "VIŠE INFO", "ALTRE INFO", "MÁS INFO", "MEHR INFOS", "PLUS D'INFOS", "更多说明")
    val skipShield = t("SKIP SHIELD", "PRESKOČI ŠTIT", "SALTA SCUDO", "SALTAR ESCUDO", "SCHILD ÜBERSPRINGEN", "PASSER LE BOUCLIER", "跳过护盾")
    val shield = t("SHIELD", "ŠTIT", "SCUDO", "ESCUDO", "SCHILD", "BOUCLIER", "护盾")
    val whoFrame = t("WHO LOOKS EVIL TO THE COP?", "KO IZGLEDA ZLO POLICIJI?", "CHI SEMBRA CATTIVO ALLO SBIRRO?", "¿QUIÉN SE VE MAL AL POLI?", "WER SIEHT FÜR DEN BULLEN BÖSE AUS?", "QUI PARAÎT MÉCHANT AU FLIC ?", "让警察看成坏人的是谁？")
    val skipFrame = t("SKIP FRAME", "PRESKOČI NAMETANJE", "SALTA LA MONTATURA", "SALTAR MONTAJE", "FALLE ÜBERSPRINGEN", "PASSER LE PIÈGE", "跳过栽赃")
    val whoGrave = t("WHOSE GRAVE?", "ČIJI GROB?", "DI CHI È LA TOMBA?", "¿LA TUMBA DE QUIÉN?", "WESSEN GRAB?", "LA TOMBE DE QUI ?", "看谁的坟？")
    val whoBecome = t("WHOSE ROLE DO YOU TAKE?", "ČIJU ULOGU UZIMAŠ?", "DI CHI PRENDI IL RUOLO?", "¿DE QUIÉN TOMAS EL ROL?", "WESSEN ROLLE NIMMST DU?", "LE RÔLE DE QUI PRENDS-TU ?", "拿走谁的身份？")
    val whoShoot = t("WHO DO YOU SHOOT?", "KOGA PUCAŠ?", "CHI SPARI?", "¿A QUIÉN DISPARAS?", "WEN SCHIESST DU?", "SUR QUI TIRES-TU ?", "打谁？")
    val keepShot = t("KEEP THE SHOT", "SAČUVAJ METAK", "TIENI IL COLPO", "GUARDAR EL DISPARO", "SCHUSS BEHALTEN", "GARDER LE TIR", "留着子弹")
    val whoPoison = t("WHO DO YOU POISON?", "KOGA TRUJEŠ?", "CHI AVVELENI?", "¿A QUIÉN ENVENENAS?", "WEN VERGIFTTEST DU?", "QUI EMPOISONNES-TU ?", "毒谁？")
    val skipTonight = t("NOT TONIGHT", "NE VEČERAS", "NON STASERA", "ESTA NOCHE NO", "NICHT HEUTE NACHT", "PAS CETTE NUIT", "今晚不")
    val revealNow = t("REVEAL YOURSELF", "OTKRIJ SE", "RIVELATI", "REVELARTE", "DICH ZEIGEN", "TE RÉVÉLER", "公开身份")
    val stayHidden = t("STAY HIDDEN", "OSTANI SKRIVEN", "RESTA NASCOSTO", "SEGUIR OCULTO", "VERSTECKT BLEIBEN", "RESTER CACHÉ", "继续隐藏")
    val mayorHint = t(
        "REVEAL AND YOUR PHONE VOTE COUNTS TWICE.",
        "OTKRIJ SE I TELEFONSKI GLAS VREDÍ DUPLI.",
        "RIVELATI E IL VOTO TELEFONICO VALE DOPPIO.",
        "REVELATE Y TU VOTO POR TELÉFONO VALE DOBLE.",
        "ZEIG DICH UND DIE HANDY-STIMME ZÄHLT DOPPELT.",
        "RÉVÈLE-TOI ET TON VOTE TÉLÉPHONE COMPTE DOUBLE.",
        "公开后，手机投票算两票。",
    )
    val mayorLiveHint = t(
        "THE MAYOR IS PUBLIC. COUNT THEIR HAND TWICE.",
        "GRADONAČELNIK JE JAVAN. NJEGOVA RUKA BROJI DVA PUTA.",
        "IL SINDACO È PUBBLICO. LA SUA MANO VALE DUE.",
        "EL ALCALDE ES PÚBLICO. SU MANO CUENTA DOS VECES.",
        "DER BÜRGERMEISTER IST ÖFFENTLICH. SEINE HAND ZÄHLT ZWEIMAL.",
        "LE MAIRE EST PUBLIC. SA MAIN COMPTE DEUX FOIS.",
        "市长已公开。举手算两票。",
    )
    fun survivorBonus(coin: String) = t(
        "SURVIVOR LIVED. +1 $coin",
        "PREŽIVELI JE ŽIV. +1 $coin",
        "IL SURVIVOR È VIVO. +1 $coin",
        "EL SUPERVIVIENTE SIGUE. +1 $coin",
        "SURVIVOR LEBT. +1 $coin",
        "LE SURVIVANT EST VIVANT. +1 $coin",
        "幸存者活着。+1 $coin",
    )
    val shieldHint = t(
        "ONCE: SHIELD A MAFIA FROM EXILE.",
        "JEDNOM: ZAŠTITI MAFIJU OD IZBACIVANJA.",
        "UNA VOLTA: PROTEGGI UN MAFIOSO DALL'ESILIO.",
        "UNA VEZ: PROTEGE A UN MAFIOSO DEL EXILIO.",
        "EINMAL: SCHÜTZE EINEN MAFIOSO VOR DEM RAUSWURF.",
        "UNE FOIS : PROTÈGE UN MAFIEUX DE L'EXIL.",
        "整局一次：挡掉黑手党的放逐。",
    )
    val hunterHint = t("TAKE SOMEONE WITH YOU.", "POVEDI NEKOGA SA SOBOM.", "PORTATI QUALCUNO CON TE.", "LLÉVATE A ALGUIEN.", "NIMM JEMANDEN MIT.", "EMMÈNE QUELQU'UN AVEC TOI.", "拉一个人垫背。")
    val tooDrunk = t("TOO DRUNK TO KNOW.", "PREVIŠE PIJAN DA ZNA.", "TROPPO UBRIACO PER SAPERE.", "DEMASIADO BORRACHO PARA SABER.", "ZU BETRUNKEN UM ES ZU WISSEN.", "TROP BOURRÉ POUR SAVOIR.", "醉到想不起来。")
    val sober = t("YOU SOBERED UP.", "OTREZNIO SI SE.", "TI SEI RIMESSO.", "YA ESTÁS SOBRIO.", "DU BIST NÜCHTERN.", "TU AS DÉCUVÉ.", "你醒酒了。")
    val general = t("GENERAL", "GENERALNO", "GENERALE", "GENERAL", "ALLGEMEIN", "GÉNÉRAL", "总则")
    val winning = t("WINNING", "POBEDA", "VITTORIA", "VICTORIA", "SIEG", "VICTOIRE", "胜利")
    val evilRoles = t("EVIL", "ZLI", "CATTIVI", "MAL", "BÖSE", "MÉCHANTS", "坏人")
    val goodRoles = t("GOOD", "DOBRI", "BUONI", "BIEN", "GUTE", "BONS", "好人")
    val wildRoles = t("WILD", "WILD", "WILD", "WILD", "WILD", "WILD", "狂野")
    val noneSelected = t("NONE SELECTED", "NIŠTA IZABRANO", "NESSUNO SCELTO", "NADA ELEGIDO", "NICHTS GEWÄHLT", "RIEN CHOISI", "未选择")
    val statistics = t("STATISTICS", "STATISTIKA", "STATISTICHE", "ESTADÍSTICAS", "STATISTIK", "STATISTIQUES", "统计")
    fun bloodN(n: Int) = "${coinName(CoinKind.BLOOD, n)}  $n"
    fun townN(n: Int) = "${coinName(CoinKind.TOWN, n)}  $n"
    fun goldN(n: Int) = "${coinName(CoinKind.GOLD, n)}  $n"
    fun coinName(kind: CoinKind, n: Int = 1): String {
        val one = n == 1
        return when (kind) {
            CoinKind.BLOOD -> if (one) "STAIN" else "STAINS"
            CoinKind.TOWN -> if (one) "WARD" else "WARDS"
            CoinKind.GOLD -> if (one) "JEST" else "JESTS"
        }
    }
    val unlockHint = t(
        "EACH SPECIAL COSTS 1. GOOD = WARD. EVIL = STAIN. WILD = JEST. JOKER IS FREE.",
        "SVAKA SPECIJALNA KOŠTA 1. DOBRE = WARD. ZLE = STAIN. WILD = JEST. DŽOKER JE BESPLATAN.",
        "OGNI SPECIALE COSTA 1. BUONI = WARD. CATTIVI = STAIN. WILD = JEST. JOKER È GRATIS.",
        "CADA ESPECIAL CUESTA 1. BIEN = WARD. MAL = STAIN. WILD = JEST. JOKER ES GRATIS.",
        "JEDE SPEZIALROLLE KOSTET 1. GUTE = WARD. BÖSE = STAIN. WILD = JEST. JOKER IST GRATIS.",
        "CHAQUE SPÉCIAL COÛTE 1. BONS = WARD. MÉCHANTS = STAIN. WILD = JEST. JOKER EST GRATUIT.",
        "每个特殊身份花 1。好人 = WARD。坏人 = STAIN。狂野 = JEST。小丑免费。",
    )
    fun watchAd(seen: Int, need: Int) =
        t("WATCH AD  $seen/$need", "GLEDAJ AD  $seen/$need", "GUARDA AD  $seen/$need", "VER ANUNCIO  $seen/$need", "WERBUNG  $seen/$need", "PUB  $seen/$need", "看广告  $seen/$need")
    val adCoinHint = t(
        "4 ADS = 1 COIN. AFTER THE LAST, PICK STAIN, WARD OR JEST.",
        "4 ADA = 1 NOVČIĆ. POSLE POSLEDNJEG BIRAŠ STAIN, WARD ILI JEST.",
        "4 AD = 1 MONETA. DOPO L'ULTIMO SCEGLI STAIN, WARD O JEST.",
        "4 ANUNCIOS = 1 MONEDA. AL FINAL ELIGES STAIN, WARD O JEST.",
        "4 ADS = 1 MÜNZE. DANACH STAIN, WARD ODER JEST.",
        "4 PUBS = 1 PIÈCE. ENSUITE CHOISIS STAIN, WARD OU JEST.",
        "4 条广告 = 1 枚币。看完后选 STAIN、WARD 或 JEST。",
    )
    val pickAdCoin = t("PICK YOUR COIN", "IZABERI NOVČIĆ", "SCEGLI LA MONETA", "ELIGE TU MONEDA", "WÄHL DEINE MÜNZE", "CHOISIS TA PIÈCE", "选择币种")
    fun takeCoin(coin: String) = t("TAKE  1 $coin", "UZMI  1 $coin", "PRENDI  1 $coin", "COGE  1 $coin", "NIMM  1 $coin", "PRENDS  1 $coin", "领取  1 $coin")
    val noAdsNow = t("NO ADS RIGHT NOW.", "NEMA ADOVA TRENUTNO.", "NIENTE AD ORA.", "NO HAY ANUNCIOS AHORA.", "GERADE KEINE WERBUNG.", "PAS DE PUB POUR L'INSTANT.", "暂时没有广告。")
    val tooFast = t(
        "NO CHANCE YOU PLAYED A GAME THAT QUICK.",
        "NEMA ŠANSE DA STE OVOLIKO BRZO ZAVRŠILI.",
        "IMPOSSIBILE AVER FINITO COSÌ IN FRETTA.",
        "IMPOSIBLE HABER TERMINADO TAN RÁPIDO.",
        "UNMÖGLICH SO SCHNELL FERTIG ZU SEIN.",
        "IMPOSSIBLE D'AVOIR FINI AUSSI VITE.",
        "不可能打得这么快。",
    )
    fun matchCoin(n: Int, coin: String) = "+$n $coin"
    val freeRole = t("FREE", "BESPLATNO", "GRATIS", "GRATIS", "GRATIS", "GRATUIT", "免费")
    val unlockedLabel = t("UNLOCKED", "OTKLJUČANO", "SBLOCCATO", "DESBLOQUEADO", "FREIGESCHALTET", "DÉBLOQUÉ", "已解锁")
    val locked = t("LOCKED", "ZAKLJUČANO", "BLOCCATO", "BLOQUEADO", "GESPERRT", "VERROUILLÉ", "未解锁")
    val buyFirst = t("BUY FIRST", "PRVO KUPI", "COMPRA PRIMA", "COMPRA ANTES", "ERST KAUFEN", "ACHÈTE D'ABORD", "先购买")
    fun unlockFor(cost: Int, coin: String) = t("UNLOCK  $cost $coin", "OTKLJUČAJ  $cost $coin", "SBLOCCA  $cost $coin", "DESBLOQUEAR  $cost $coin", "FREISCHALTEN  $cost $coin", "DÉBLOQUER  $cost $coin", "解锁  $cost $coin")
    fun needCoins(cost: Int, coin: String) = t("NEED $cost $coin", "TREBA $cost $coin", "SERVE $cost $coin", "FALTAN $cost $coin", "BRAUCHST $cost $coin", "IL FAUT $cost $coin", "还差 $cost $coin")
    fun spawnChance(pct: Int) = t("CHANCE $pct%", "ŠANSA $pct%", "CHANCE $pct%", "PROB. $pct%", "CHANCE $pct%", "CHANCE $pct%", "概率 $pct%")
    val spawnHintEvil = t(
        "MISS → NORMAL MAFIA.",
        "PROMASHAJ → OBIČNA MAFIJA.",
        "MANCATO → MAFIA NORMALE.",
        "FALLO → MAFIA NORMAL.",
        "FEHL → NORMALE MAFIA.",
        "RATÉ → MAFIA NORMALE.",
        "未中 → 普通黑手党。",
    )
    val spawnHintGood = t(
        "MISS → CIVILIAN.",
        "PROMASHAJ → CIVIL.",
        "MANCATO → CIVILE.",
        "FALLO → CIVIL.",
        "FEHL → ZIVILIST.",
        "RATÉ → CIVIL.",
        "未中 → 平民。",
    )
    val spawnHintWild = t(
        "MISS → CIVILIAN.",
        "PROMASHAJ → CIVIL.",
        "MANCATO → CIVILE.",
        "FALLO → CIVIL.",
        "FEHL → ZIVILIST.",
        "RATÉ → CIVIL.",
        "未中 → 平民。",
    )
    val guest = t("GUEST", "GOST", "OSPITE", "INVITADO", "GAST", "INVITÉ", "游客")
    val profile = t("PROFILE", "PROFIL", "PROFILO", "PERFIL", "PROFIL", "PROFIL", "资料")
    val pickAvatar = t(
        "PICK A FACE. MORE ART LATER.",
        "IZABERI LICE. SLIKE KASNIJE.",
        "SCEGLI UNA FACCIA. ALTRE IMMAGINI DOPO.",
        "ELIGE UNA CARA. MÁS ARTE LUEGO.",
        "WÄHL EIN GESICHT. MEHR BILDER SPÄTER.",
        "CHOISIS UN VISAGE. PLUS D'ART PLUS TARD.",
        "选一张脸。以后再加图。",
    )
    val namePlaceholder = t("YOUR NAME", "TVOJE IME", "IL TUO NOME", "TU NOMBRE", "DEIN NAME", "TON NOM", "你的名字")
    val signInPlay = t("SIGN IN WITH PLAY", "PRIJAVI SE PREKO PLAY", "ACCEDI CON PLAY", "ENTRAR CON PLAY", "MIT PLAY ANMELDEN", "CONNEXION PLAY", "用 Play 登录")
    val signedIn = t("SIGNED IN  (STUB)", "PRIJAVLJEN  (STUB)", "CONNESSO  (STUB)", "CONECTADO  (STUB)", "ANGEMELDET  (STUB)", "CONNECTÉ  (STUB)", "已登录（占位）")
    val savePlay = t("SAVE TO PLAY", "SAČUVAJ NA PLAY", "SALVA SU PLAY", "GUARDAR EN PLAY", "AUF PLAY SPEICHERN", "SAUVER SUR PLAY", "保存到 Play")
    val loadPlay = t("LOAD FROM PLAY", "UČITAJ SA PLAY", "CARICA DA PLAY", "CARGAR DESDE PLAY", "VON PLAY LADEN", "CHARGER DEPUIS PLAY", "从 Play 读取")
    val playStubHint = t(
        "PLAY GAMES CLOUD COMES LATER. SAVE / LOAD IS A LOCAL STUB FOR NOW.",
        "PLAY GAMES CLOUD DOLAZI KASNIJE. SAVE / LOAD JE ZA SADA LOKALNI STUB.",
        "IL CLOUD PLAY ARRIVA DOPO. SALVA / CARICA È UNO STUB LOCALE.",
        "LA NUBE DE PLAY LLEGA LUEGO. GUARDAR / CARGAR ES UN STUB LOCAL.",
        "PLAY-CLOUD KOMMT SPÄTER. SPEICHERN / LADEN IST LOKALER STUB.",
        "LE CLOUD PLAY VIENT PLUS TARD. SAUVER / CHARGER EST UN STUB LOCAL.",
        "Play 云存档以后再做。现在保存/读取只是本地占位。",
    )
    val playNeedSignIn = t("SIGN IN FIRST.", "PRVO SE PRIJAVI.", "ACCEDI PRIMA.", "ENTRA PRIMERO.", "ERST ANMELDEN.", "CONNECTE-TOI D'ABORD.", "请先登录。")
    val playSaved = t("SAVED.", "SAČUVANO.", "SALVATO.", "GUARDADO.", "GESPEICHERT.", "SAUVÉ.", "已保存。")
    val playLoaded = t("LOADED.", "UČITANO.", "CARICATO.", "CARGADO.", "GELADEN.", "CHARGÉ.", "已读取。")
    val playEmpty = t("NOTHING SAVED YET.", "NIŠTA JOŠ NIJE SAČUVANO.", "ANCORA NIENTE SALVATO.", "AÚN NO HAY NADA GUARDADO.", "NOCH NICHTS GESPEICHERT.", "RIEN SAUVÉ POUR L'INSTANT.", "还没有存档。")
    val statsSoon = t(
        "WINRATE, ROLE HISTORY, EXPORT. COMING SOON.",
        "WINRATE, ISTORIJA ULOGA, EXPORT. USKORO.",
        "WINRATE, STORIA RUOLI, EXPORT. PROSSIMAMENTE.",
        "WINRATE, HISTORIAL, EXPORTAR. PRÓXIMAMENTE.",
        "WINRATE, ROLLEN-HISTORIE, EXPORT. DEMNÄCHST.",
        "WINRATE, HISTORIQUE, EXPORT. BIENTÔT.",
        "胜率、身份记录、导出。即将推出。",
    )
    val yourTwin = t("YOUR TWIN", "TVOJ BLIZANAC", "IL TUO GEMELLO", "TU GEMELO", "DEIN ZWILLING", "TON JUMEAU", "你的双胞胎")

    fun seatN(n: Int) = "${seat} $n"
    fun nightN(n: Int) = "${night} $n"
    fun dayN(n: Int) = "${day} $n"
    fun recommendedFor(n: Int) = "$recommended $n $players"
    fun minLabel(m: Int) = if (m == 0) off else "$m MIN"
    fun secLabel(n: Int) = "$n SEC"
    fun roleTitle(role: Role?) = when (role) {
        Role.MAFIA -> t("MAFIA", "MAFIJA", "MAFIA", "MAFIA", "MAFIA", "MAFIA", "黑手党")
        Role.DON -> t("DON", "ŠEF", "DON", "DON", "DON", "DON", "教父")
        Role.LAWYER -> t("LAWYER", "ADVOKAT", "AVVOCATO", "ABOGADO", "ANWALT", "AVOCAT", "律师")
        Role.HEALER -> t("HEALER", "LEKAR", "GUARITORE", "SANADOR", "HEILER", "GUÉRISSEUR", "医生")
        Role.COP -> t("COP", "POLICAJAC", "SBIRRO", "POLI", "BULL", "FLIC", "警察")
        Role.CIVILIAN -> t("CIVILIAN", "CIVIL", "CIVILE", "CIVIL", "ZIVILIST", "CIVIL", "平民")
        Role.HUNTER -> t("HUNTER", "LOVAC", "CACCIATORE", "CAZADOR", "JÄGER", "CHASSEUR", "猎人")
        Role.SEER -> t("SEER", "VIDOVNJAK", "VEGGENTE", "VIDENTE", "SEHER", "VOYANT", "预言家")
        Role.BODYGUARD -> t("BODYGUARD", "TELOHRANITELJ", "GUARDIA", "GUARDAESPALDAS", "LEIBWÄCHTER", "GARDE DU CORPS", "保镖")
        Role.JOKER -> t("JOKER", "DŽOKER", "JOKER", "JOKER", "JOKER", "JOKER", "小丑")
        Role.KILLER -> t("KILLER", "UBICA", "KILLER", "ASESINO", "KILLER", "TUEUR", "杀手")
        Role.LUNATIC -> t("LUNATIC", "LUDAK", "PAZZO", "LOCO", "VERRÜCKTER", "FOU", "疯子")
        Role.DRUNK -> t("DRUNK", "PIJANI", "UBRIACO", "BORRACHO", "BETRUNKENER", "IVROGNE", "醉汉")
        Role.TWIN_CIVIL -> t("CIVIL TWIN", "CIVIL BLIZANAC", "GEMELLO CIVILE", "GEMELO CIVIL", "ZIVIL-ZWILLING", "JUMEAU CIVIL", "平民双胞胎")
        Role.TWIN_MAFIA -> t("MAFIA TWIN", "MAFIJA BLIZANAC", "GEMELLO MAFIA", "GEMELO MAFIA", "MAFIA-ZWILLING", "JUMEAU MAFIA", "黑手党双胞胎")
        Role.WHORE -> t("WHORE", "KURVA", "PUTTANA", "PUTA", "HURE", "PUTAIN", "妓女")
        Role.MAYOR -> t("MAYOR", "GRADONAČELNIK", "SINDACO", "ALCALDE", "BÜRGERMEISTER", "MAIRE", "市长")
        Role.NECROMANCER -> t("NECROMANCER", "NEKROMANT", "NEGROMANTE", "NIGROMANTE", "NEKROMANT", "NÉCROMANCIEN", "死灵法师")
        Role.VIGILANTE -> t("VIGILANTE", "OSVETNIK", "VIGILANTE", "VIGILANTE", "VIGILANT", "VIGILANT", "治安员")
        Role.FRAMER -> t("FRAMER", "NAMETAČ", "FALSARIO", "MONTADOR", "FALSCHER", "MANIPULATEUR", "栽赃者")
        Role.TRAITOR -> t("TRAITOR", "IZDAJNIK", "TRADITORE", "TRAIDOR", "VERRÄTER", "TRAÎTRE", "叛徒")
        Role.POISONER -> t("POISONER", "TROVAČ", "AVVELENATORE", "ENVENENADOR", "GIFTMISCHER", "EMPOISONNEUR", "下毒者")
        Role.CURSED -> t("CURSED", "PROKLETI", "MALEDETTO", "MALDITO", "VERFLUCHTER", "MAUDIT", "被诅咒者")
        Role.SURVIVOR -> t("SURVIVOR", "PREŽIVELI", "SOPRAVVISSUTO", "SUPERVIVIENTE", "ÜBERLEBENDER", "SURVIVANT", "幸存者")
        Role.AMNESIAC -> t("AMNESIAC", "AMNEZIČAR", "AMNESIACO", "AMNÉSICO", "AMNESTIKER", "AMNÉSIQUE", "失忆者")
        null -> unknownRole
    }

    fun roleBlurb(role: Role) = when (role) {
        Role.MAFIA -> t(
            "You are the knife in the dark. Each night the crew votes who dies. You see your people. The table does not. You may even vote yourself, if the bit is that good.",
            "Ti si nož u mraku. Svake noći ekipa glasa ko umire. Vidiš svoje. Sto ne vidi. Možeš i sebe da glasaš, ako je bit toliko dobar.",
            "Sei il coltello nel buio. Ogni notte la crew vota chi muore. Vedi i tuoi. Il tavolo no. Puoi votare anche te, se la bit è così bella.",
            "Eres el cuchillo en la oscuridad. Cada noche la banda vota quién muere. Ves a los tuyos. La mesa no. Hasta puedes votarte, si el bit lo pide.",
            "Du bist das Messer im Dunkeln. Jede Nacht stimmt die Crew ab, wer stirbt. Du siehst deine Leute. Der Tisch nicht. Du darfst sogar dich wählen, wenn der Bit so gut ist.",
            "Tu es le couteau dans le noir. Chaque nuit l'équipe vote qui meurt. Tu vois les tiens. La table non. Tu peux même voter pour toi, si le bit le vaut.",
            "你是暗处的刀。每晚同伙投票杀谁。你看得见自己人，牌桌看不见。如果表演需要，你甚至可以投自己。",
        )
        Role.DON -> t(
            "You run the crew. Same night vote as the rest, but if the kill ties, your word breaks it. Status talks. Everybody else shuts up.",
            "Ti vodiš ekipu. Isti noćni glas kao ostali, ali na nerešenom tvoja reč seče. Status priča. Ostali ćute.",
            "Comandi la crew. Stesso voto notturno, ma al pareggio parla la tua parola. Lo status parla. Gli altri zitti.",
            "Mandas la banda. Mismo voto nocturno, pero el empate lo rompes tú. El rango habla. El resto calla.",
            "Du führst die Crew. Gleiche Nachtstimme, aber bei Gleichstand bricht dein Wort. Status redet. Der Rest hält die Klappe.",
            "Tu diriges l'équipe. Même vote de nuit, mais en cas d'égalité ta voix tranche. Le statut parle. Les autres se taisent.",
            "你管这帮人。夜票和其他人一样，平票时你说了算。地位开口，别人闭嘴。",
        )
        Role.LAWYER -> t(
            "Mafia with a briefcase. You still vote to kill. Once per game you can make an exile bounce off a mafioso. Spend it like you mean it.",
            "Mafija sa aktovkom. I dalje glasaš koga ubiti. Jednom u igri možeš da odbiješ izbacivanje mafijaša. Potroši to kao da misliš.",
            "Mafia con la valigetta. Voti ancora per uccidere. Una volta per partita fai rimbalzare un esilio da un mafioso. Spendilo sul serio.",
            "Mafia con maletín. Sigues votando a quién matar. Una vez por partida puedes rebotar el exilio de un mafioso. Úsalo en serio.",
            "Mafia mit Aktentasche. Du stimmst weiter für den Mord. Einmal pro Spiel prallt ein Rauswurf an einem Mafioso ab. Setz es ein, als meintest du es.",
            "Mafia à mallette. Tu votes encore pour tuer. Une fois par partie tu fais rebondir un exil sur un mafieux. Dépense-le pour de vrai.",
            "提公文包的黑手党。你照样投票杀人。整局一次，可以挡掉对黑手党的放逐。别浪费。",
        )
        Role.HEALER -> t(
            "One hand on the dying. Each night you pick someone to keep breathing. You can save yourself. Just don't heal the same body two nights in a row unless the table lets you.",
            "Jedna ruka na umirućem. Svake noći biraš koga ostavljaš u životu. Možeš i sebe. Samo nemoj istog dva puta zaredom, osim ako sto to dozvoli.",
            "Una mano sui morenti. Ogni notte scegli chi resta in vita. Puoi salvarti. Non curare lo stesso corpo due notti di fila, a meno che il tavolo lo permetta.",
            "Una mano sobre el que se muere. Cada noche eliges quién sigue respirando. Puedes salvarte. No cures el mismo cuerpo dos noches seguidas, salvo que la mesa lo permita.",
            "Eine Hand auf den Sterbenden. Jede Nacht wählst du, wer weiteratmet. Du darfst dich selbst retten. Nicht dieselbe Person zwei Nächte hintereinander, außer der Tisch erlaubt es.",
            "Une main sur les mourants. Chaque nuit tu choisis qui continue de respirer. Tu peux te sauver. Pas le même corps deux nuits de suite, sauf si la table l'autorise.",
            "一只手按在将死的人身上。每晚选一个让他活。可以救自己。除非设置允许，别连续两夜治同一个人。",
        )
        Role.COP -> t(
            "You knock. They don't know. You learn GOOD or BAD, not the costume. You cannot inspect yourself. Write it down in your head and lie with a straight face.",
            "Kucaš. Oni ne znaju. Vidiš DOBAR ili LOŠ, ne kostim. Sebe ne možeš. Zapamti i laži ravnog lica.",
            "Bussi. Loro non sanno. Vedi BUONO o CATTIVO, non il costume. Non puoi ispezionare te stesso. Segnatelo in testa e menti a faccia ferma.",
            "Llamas. Ellos no saben. Ves BUENO o MALO, no el disfraz. No puedes investigarte. Guárdalo en la cabeza y miente con cara seria.",
            "Du klopfst. Sie wissen es nicht. Du siehst GUT oder BÖSE, nicht das Kostüm. Dich selbst prüfst du nicht. Merk es dir und lüg mit geradem Gesicht.",
            "Tu frappes. Ils ne savent pas. Tu vois BON ou MAUVAIS, pas le costume. Tu ne peux pas t'inspecter. Grave-le et mens sans cligner.",
            "你去敲。他们不知道。你只看到好或坏，不是具体身份。不能查自己。记在脑子里，面不改色地撒谎。",
        )
        Role.CIVILIAN -> t(
            "No gun. No badge. No excuse. You tap the dummy so the wolves cannot smell who has teeth. Then you talk. Talking is the job.",
            "Nemaš pištolj. Nemaš značku. Nemaš izgovor. Tapneš lažnu akciju da vukovi ne nanjuše ko ima zube. Onda pričaš. Pričanje je posao.",
            "Niente pistola. Niente distintivo. Niente scuse. Tocchi l'azione finta così i lupi non sentono chi ha i denti. Poi parli. Parlare è il lavoro.",
            "Sin pistola. Sin placa. Sin excusa. Tocas la acción falsa para que los lobos no huelan quién tiene dientes. Luego hablas. Hablar es el trabajo.",
            "Keine Waffe. Kein Abzeichen. Keine Ausrede. Du tippst die Scheinaktion, damit die Wölfe nicht riechen, wer Zähne hat. Dann redest du. Reden ist der Job.",
            "Pas d'arme. Pas de badge. Pas d'excuse. Tu tapes la fausse action pour que les loups ne sentent pas qui a des dents. Puis tu parles. Parler, c'est le boulot.",
            "没枪。没警徽。没借口。点伪装动作，别让狼闻出谁有牙。然后说话。说话就是你的工作。",
        )
        Role.HUNTER -> t(
            "If they take you, you take someone. Night kill or day exile, you still get one last name. Make it count. Make it ugly.",
            "Ako te uzmu, ti uzmeš nekoga. Noćno ubistvo ili dnevno izbacivanje, i dalje biraš poslednje ime. Neka vredi. Neka boli.",
            "Se ti prendono, tu prendi qualcuno. Morte di notte o esilio di giorno, hai ancora un ultimo nome. Fallo contare. Fallo brutto.",
            "Si te llevan, te llevas a alguien. Muerte nocturna o exilio diurno, aún eliges un último nombre. Que valga. Que duela.",
            "Wenn sie dich holen, holst du jemanden. Nachtmord oder Tagesrauswurf, du bekommst noch einen letzten Namen. Mach ihn teuer. Mach ihn hässlich.",
            "S'ils te prennent, tu en prends un. Meurtre de nuit ou exil de jour, tu as encore un dernier nom. Fais-le compter. Fais-le sale.",
            "他们带走你，你也带走一个。夜杀或日放逐，你还能点最后一个名字。点得值，点得狠。",
        )
        Role.SEER -> t(
            "Not good. Not bad. The exact role. Stronger than the cop, louder if you live long enough to say it.",
            "Ne dobar. Ne loš. Tačna uloga. Jači od pandura, glasniji ako poživiš dovoljno da to kažeš.",
            "Non buono. Non cattivo. Il ruolo esatto. Più forte dello sbirro, più rumoroso se vivi abbastanza da dirlo.",
            "Ni bueno. Ni malo. El rol exacto. Más fuerte que el poli, más ruidoso si vives lo bastante para decirlo.",
            "Nicht gut. Nicht böse. Die genaue Rolle. Stärker als der Bulle, lauter wenn du lange genug lebst, um es zu sagen.",
            "Pas bon. Pas mauvais. Le rôle exact. Plus fort que le flic, plus bruyant si tu vis assez longtemps pour le dire.",
            "不是好或坏。是准确身份。比警察强。活得够久说出来，就更吵。",
        )
        Role.BODYGUARD -> t(
            "Stand in front of someone. If the hit lands on them tonight, it lands on you instead. You die. They don't. That's the whole poem.",
            "Stani ispred nekoga. Ako večeras udarac padne na njih, pada na tebe. Ti umireš. Oni ne. To je cela pesma.",
            "Stai davanti a qualcuno. Se stanotte il colpo li prende, prende te. Tu muori. Loro no. Tutta la poesia è questa.",
            "Ponte delante de alguien. Si el golpe cae en ellos esta noche, cae en ti. Tú mueres. Ellos no. Ese es todo el poema.",
            "Stell dich vor jemanden. Trifft der Schlag sie heute Nacht, trifft er dich. Du stirbst. Sie nicht. Das ist das ganze Gedicht.",
            "Place-toi devant quelqu'un. Si le coup les touche cette nuit, il te touche. Tu meurs. Pas eux. Tout le poème est là.",
            "挡在某个人前面。今晚刀砍到他们，就砍到你。你死，他们活。整首诗就这句。",
        )
        Role.JOKER -> t(
            "You win only if the table throws you out in daylight. A night death is just a corpse. Play loud. Get voted. Then bow.",
            "Pobeđuješ samo ako te sto baci van danju. Noćna smrt je samo leš. Budi glasan. Neka te izbace. Onda se nakloni.",
            "Vinci solo se il tavolo ti butta fuori di giorno. Una morte notturna è solo un cadavere. Fai rumore. Fatti votare. Poi inchinati.",
            "Ganas solo si la mesa te echa de día. Una muerte nocturna es un cadáver. Haz ruido. Que te voten. Luego saluda.",
            "Du gewinnst nur, wenn der Tisch dich am Tag rauswirft. Ein Nacht-Tod ist nur eine Leiche. Sei laut. Lass dich rauswählen. Dann verbeug dich.",
            "Tu gagnes seulement si la table te jette le jour. Une mort de nuit n'est qu'un cadavre. Fais du bruit. Fais-toi voter. Puis salue.",
            "只有白天被放逐才算赢。夜里死了只是一具尸体。吵起来。让人投你。然后鞠躬。",
        )
        Role.KILLER -> t(
            "Not mafia. Not town. You strike on even nights. You win if you are the last one breathing. Everyone else is meat with opinions.",
            "Nisi mafija. Nisi grad. Udarš na parnim noćima. Pobeđuješ ako ostaneš poslednji živ. Svi ostali su meso sa mišljenjem.",
            "Non sei mafia. Non sei città. Colpisci nelle notti pari. Vinci se resti l'ultimo a respirare. Tutti gli altri sono carne con opinioni.",
            "No eres mafia. No eres pueblo. Golpeas en noches pares. Ganas si eres el último respirando. El resto es carne con opiniones.",
            "Nicht Mafia. Nicht Stadt. Du schlägst in geraden Nächten. Du gewinnst, wenn du als Letzter atmest. Alle anderen sind Fleisch mit Meinung.",
            "Pas mafia. Pas village. Tu frappes les nuits paires. Tu gagnes si tu es le dernier à respirer. Les autres sont de la viande avec des avis.",
            "不是黑手党。不是好人。双数夜动手。最后一个活着就赢。别人都是会说话的肉。",
        )
        Role.LUNATIC -> t(
            "You see CIVILIAN. You are not. You count as evil. Smile. Die confused. You helped the crew and never knew the joke.",
            "Vidiš CIVIL. Nisi. Brojiš se kao zli. Osmehni se. Umri zbunjen. Pomogao si ekipi i nikad nisi shvatio vic.",
            "Vedi CIVILE. Non lo sei. Conti come cattivo. Sorridi. Muori confuso. Hai aiutato la crew e non hai mai capito la barzelletta.",
            "Ves CIVIL. No lo eres. Cuentas como mal. Sonríe. Muere confundido. Ayudaste a la banda y nunca entendiste el chiste.",
            "Du siehst ZIVILIST. Bist du nicht. Du zählst als böse. Lächle. Stirb verwirrt. Du halfst der Crew und kanntest den Witz nie.",
            "Tu vois CIVIL. Tu ne l'es pas. Tu comptes comme méchant. Souris. Meurs perdu. Tu as aidé l'équipe sans jamais piger la blague.",
            "你看到的是平民。你不是。你算坏人。笑。糊涂地死。你帮了同伙，却从没听懂这个笑话。",
        )
        Role.DRUNK -> t(
            "Nights 1 and 2 you are a question mark with a dummy tap. Night 3 the room stops spinning and you remember who you are. Try not to confess before that.",
            "Noći 1 i 2 si znak pitanja sa lažnim tapom. Noć 3 soba prestane da se vrti i setiš se ko si. Pokušaj da se ne pohvališ pre toga.",
            "Notti 1 e 2 sei un punto interrogativo con un tap finto. Notte 3 la stanza smette di girare e ti ricordi chi sei. Non confessare prima.",
            "Noches 1 y 2 eres un interrogante con un toque falso. Noche 3 la sala deja de girar y recuerdas quién eres. No te delates antes.",
            "Nacht 1 und 2 bist du ein Fragezeichen mit Schein-Tipp. Nacht 3 steht der Raum still und du weißt, wer du bist. Nicht vorher auspacken.",
            "Nuits 1 et 2 tu es un point d'interrogation avec un tap factice. Nuit 3 la pièce arrête de tourner et tu te souviens qui tu es. Ne craque pas avant.",
            "第 1、2 夜你是个问号加伪装点击。第 3 夜房间不转了，你想起自己是谁。别提前招。",
        )
        Role.TWIN_CIVIL -> t(
            "Two good bodies, one fuse. If one drops, the other drops. Sit close. If they cut one of you, they cut both.",
            "Dva dobra tela, jedan fitilj. Ako jedan padne, pada i drugi. Sedite blizu. Ako preseku jednog, presekli su obojicu.",
            "Due corpi buoni, una miccia. Se uno cade, cade l'altro. State vicini. Se tagliano uno, tagliano entrambi.",
            "Dos cuerpos buenos, una mecha. Si uno cae, cae el otro. Sentaos juntos. Si cortan a uno, os cortan a los dos.",
            "Zwei gute Körper, eine Zündschnur. Fällt einer, fällt der andere. Sitzt nah. Schneiden sie einen, schneiden sie beide.",
            "Deux corps bons, une mèche. Si l'un tombe, l'autre tombe. Restez proches. S'ils coupent l'un, ils coupent les deux.",
            "两具好人身子，一根引线。一个倒，另一个也倒。坐近点。砍一个等于砍两个。",
        )
        Role.TWIN_MAFIA -> t(
            "Two mafiosi, one heartbeat. You vote with the crew. You see each other. If one dies, the other follows. Hide that bond or the table eats you twice.",
            "Dva mafijaša, jedan puls. Glasate sa ekipom. Vidite se. Ako jedan umre, drugi ide za njim. Sakrij tu vezu ili vas sto pojede dvaput.",
            "Due mafiosi, un battito. Votate con la crew. Vi vedete. Se uno muore, l'altro lo segue. Nascondete il legame o il tavolo vi mangia due volte.",
            "Dos mafiosos, un pulso. Votáis con la banda. Os veis. Si uno muere, el otro le sigue. Esconded el vínculo o la mesa os come dos veces.",
            "Zwei Mafiosi, ein Herzschlag. Ihr stimmt mit der Crew. Ihr seht euch. Stirbt einer, folgt der andere. Versteckt das Band oder der Tisch frisst euch zweimal.",
            "Deux mafieux, un pouls. Vous votez avec l'équipe. Vous vous voyez. Si l'un meurt, l'autre suit. Cachez le lien ou la table vous mange deux fois.",
            "两个黑手党，同一心跳。和同伙一起投票。你们看得见彼此。一个死，另一个跟着死。藏好这层关系，否则牌桌吃你们两次。",
        )
        Role.WHORE -> t(
            "One bed. One night. Whoever you sleep with loses their power until morning. Not the same body two nights in a row. In Roles set GOOD, EVIL, JUST A PEST, PLAY ALONE, or they pick at game start. Pest never wins — they only block. Alone wins like other Wild: last two standing.",
            "Jedan krevet. Jedna noć. Ko s kim spava, gubi moć do jutra. Ne ista osoba dve noći zaredom. U Roles staviš DOBRA, ZLA, SAMO SMETA, IGRA SAMA, ili biraju na početku. Smeta nikad ne pobedi — samo blokira. Sama pobedi kao ostali Wild: ostane 1v1.",
            "Un letto. Una notte. Chi dorme con te perde il potere fino al mattino. Non lo stesso corpo due notti di fila. In Ruoli: BUONA, CATTIVA, SOLO DISTURBA, GIOCA DA SOLA, o sceglie all'inizio. Disturba non vince mai — blocca e basta. Da sola vince come gli altri Wild: restano in due.",
            "Una cama. Una noche. Quien duerma contigo pierde el poder hasta el alba. No el mismo cuerpo dos noches seguidas. En Roles: BUENA, MALA, SOLO MOLESTA, JUEGA SOLA, o elige al empezar. Molestar nunca gana: solo bloquea. Sola gana como el resto Wild: quedan dos.",
            "Ein Bett. Eine Nacht. Wer mit dir schläft, verliert bis zum Morgen seine Kraft. Nicht denselben Körper zwei Nächte hintereinander. Unter Rollen: GUT, BÖSE, NUR STÖRERIN, SPIELT ALLEIN, oder Wahl am Start. Störerin gewinnt nie — sie blockt nur. Allein gewinnt wie andere Wild: die letzten zwei.",
            "Un lit. Une nuit. Qui couche avec toi perd son pouvoir jusqu'au matin. Pas le même corps deux nuits de suite. Dans Rôles : BIEN, MAL, JUSTE GÊNE, JOUE SEULE, ou choix en début. Gêne ne gagne jamais — elle bloque. Seule gagne comme les autres Wild : il n'en reste que deux.",
            "一张床。一夜。和你睡的人天亮前没有技能。不能连续两夜同一人。在身份里设好人、坏人、只捣乱、独自获胜，或开局自选。捣乱永不赢，只封技能。独自获胜和其他狂野一样：留下两人就赢。",
        )
        Role.MAYOR -> t(
            "Hide as long as you like. The night you step forward, the table knows. After that, your phone vote counts twice. Live vote: they should count your hand twice too.",
            "Krij se dok hoćeš. Noć kad izađeš, sto zna. Posle toga tvoj telefonski glas vredi duplo. Uživo: i ruka se broji dva puta.",
            "Nasconditi finché vuoi. La notte in cui esci, il tavolo sa. Dopo, il voto telefonico vale doppio. Dal vivo: anche la mano conta due.",
            "Escóndete cuanto quieras. La noche que sales, la mesa lo sabe. Luego tu voto por teléfono vale doble. En vivo: la mano también cuenta dos.",
            "Versteck dich so lange du willst. In der Nacht, in der du vortreten, weiß der Tisch. Danach zählt die Handy-Stimme doppelt. Live: auch die Hand zählt zweimal.",
            "Cache-toi tant que tu veux. La nuit où tu sors, la table sait. Après, ton vote téléphone compte double. En direct : ta main aussi.",
            "想藏多久藏多久。你站出来的那夜，牌桌就知道了。之后手机票算两票。现场投票：举手也算两票。",
        )
        Role.NECROMANCER -> t(
            "The dead still talk if you put your ear on the dirt. Pick a corpse. You learn the exact role. Living bodies stay shut.",
            "Mrtvi još pričaju ako staviš uvo na zemlju. Izaberi leš. Vidiš tačnu ulogu. Živi ćute.",
            "I morti parlano se metti l'orecchio nella terra. Scegli un cadavere. Vedi il ruolo esatto. I vivi stanno zitti.",
            "Los muertos hablan si pegas el oído a la tierra. Elige un cadáver. Ves el rol exacto. Los vivos callan.",
            "Die Toten reden, wenn du das Ohr in die Erde legst. Nimm eine Leiche. Du siehst die genaue Rolle. Die Lebenden halten dicht.",
            "Les morts parlent si tu colles l'oreille à la terre. Prends un cadavre. Tu vois le rôle exact. Les vivants se taisent.",
            "把耳朵贴到土上，死人还会说。选一具尸体。看到准确身份。活人闭嘴。",
        )
        Role.VIGILANTE -> t(
            "One bullet in the coat. One name. Same night they drop, unless a healer is already standing over them. You can keep the shot in your pocket. Spend it once. After that you are just another loud mouth at the table.",
            "Jedan metak u kaputu. Jedno ime. Iste noći padaju, osim ako lekar već stoji nad njima. Možeš da držiš hitac u džepu. Potroši ga jednom. Posle toga si samo još jedna glasna usta za stolom.",
            "Un proiettile nel cappotto. Un nome. Cadono la stessa notte, salvo se un guaritore è già su di loro. Puoi tenere il colpo in tasca. Spendilo una volta. Poi sei solo un'altra bocca rumorosa al tavolo.",
            "Una bala en el abrigo. Un nombre. Caen esa misma noche, salvo que un sanador ya esté sobre ellos. Puedes guardar el disparo. Úsalo una vez. Luego eres otra boca ruidosa en la mesa.",
            "Eine Kugel im Mantel. Ein Name. Sie fallen in derselben Nacht, außer ein Heiler steht schon über ihnen. Du kannst den Schuss in der Tasche behalten. Einmal ausgeben. Danach bist du nur noch ein lautes Maul am Tisch.",
            "Une balle dans le manteau. Un nom. Ils tombent la même nuit, sauf si un guérisseur est déjà sur eux. Tu peux garder le tir en poche. Dépense-le une fois. Après tu n'es plus qu'une bouche de plus à la table.",
            "大衣里一颗子弹。一个名字。当晚倒下，除非医生已经罩着他们。可以把这一枪留在口袋里。只用一次。之后你只是桌上又一张嘴。",
        )
        Role.FRAMER -> t(
            "You sit with the knives. After the kill vote you paint one living face. Tonight the cop reads them BAD even if they pray.",
            "Sediš sa noževima. Posle glasa za ubistvo ofarbaš jedno živo lice. Večeras ih pandur vidi kao LOŠE čak i ako se mole.",
            "Siedi con i coltelli. Dopo il voto kill dipingi una faccia viva. Stasera lo sbirro li legge CATTIVI anche se pregano.",
            "Te sientas con los cuchillos. Tras el voto de muerte pintas una cara viva. Esta noche el poli los lee MAL aunque recen.",
            "Du sitzt bei den Messern. Nach der Mordstimme streichst du ein lebendes Gesicht. Heute Nacht liest der Bulle sie BÖSE, auch wenn sie beten.",
            "Tu sièges avec les couteaux. Après le vote mort tu peins un visage vivant. Ce soir le flic les lit MAUVAIS même s'ils prient.",
            "你和刀坐一起。杀票之后给一张活脸涂黑。今晚警察读他们是坏人，哪怕他们在祈祷。",
        )
        Role.TRAITOR -> t(
            "You count as evil. You do not see the crew. The cop reads you GOOD. Smile like town. Bleed like mafia.",
            "Brojiš se kao zao. Ne vidiš ekipu. Pandur te vidi DOBRO. Osmehni se kao grad. Krvari kao mafija.",
            "Conti come cattivo. Non vedi la crew. Lo sbirro ti legge BUONO. Sorridi da città. Sanguina da mafia.",
            "Cuentas como mal. No ves a la banda. El poli te lee BUENO. Sonríe como pueblo. Sangra como mafia.",
            "Du zählst als böse. Du siehst die Crew nicht. Der Bulle liest dich GUT. Lächle wie die Stadt. Blute wie die Mafia.",
            "Tu comptes comme méchant. Tu ne vois pas l'équipe. Le flic te lit BON. Souris village. Saigne mafia.",
            "你算坏人。看不见同伙。警察读你是好人。像好人一样笑，像黑手党一样流血。",
        )
        Role.POISONER -> t(
            "Evil, not wild. You do not sit with the crew. Pick a name tonight. They drop the next night unless a healer wipes it. Slow knife. Same win as mafia.",
            "Zao, ne wild. Ne sediš sa ekipom. Večeras ime. Padaju sledeće noći osim ako lekar obriše. Spori nož. Ista pobeda kao mafija.",
            "Cattivo, non wild. Non siedi con la crew. Stasera un nome. Cadono la notte dopo se il guaritore non lo cancella. Coltello lento. Stessa vittoria della mafia.",
            "Mal, no wild. No te sientas con la banda. Esta noche un nombre. Caen la noche siguiente si el sanador no lo borra. Cuchillo lento. Misma victoria que la mafia.",
            "Böse, nicht Wild. Du sitzt nicht bei der Crew. Heute Nacht ein Name. Sie fallen nächste Nacht, außer ein Heiler wischt es. Langsames Messer. Gleicher Sieg wie Mafia.",
            "Méchant, pas wild. Tu ne sièges pas avec l'équipe. Un nom ce soir. Ils tombent la nuit suivante sauf si un guérisseur l'efface. Couteau lent. Même victoire que la mafia.",
            "坏人，不是狂野。不和同伙坐一起。今晚点一个名。下一夜倒下，除非医生抹掉。慢刀。和黑手党同一边赢。",
        )
        Role.CURSED -> t(
            "Wild until the knives find you. If mafia hits you and no bodyguard eats it, you do not die. You wake as mafia. Quiet. No corpse. New family.",
            "Wild dok te noževi ne nađu. Ako mafija udari i telohranitelj ne primi, ne umireš. Budiš se kao mafija. Tiho. Nema leša. Nova porodica.",
            "Wild finché i coltelli ti trovano. Se la mafia ti colpisce e nessuna guardia lo mangia, non muori. Ti svegli mafia. Silenzio. Niente cadavere. Nuova famiglia.",
            "Wild hasta que te encuentren los cuchillos. Si la mafia te pega y ningún guardaespaldas lo come, no mueres. Despiertas mafia. Silencio. Sin cadáver. Nueva familia.",
            "Wild, bis die Messer dich finden. Trifft dich die Mafia und kein Leibwächter frisst es, stirbst du nicht. Du wachst als Mafia auf. Still. Keine Leiche. Neue Familie.",
            "Wild jusqu'à ce que les couteaux te trouvent. Si la mafia te frappe et qu'aucun garde ne l'avale, tu ne meurs pas. Tu te réveilles mafia. Silence. Pas de cadavre. Nouvelle famille.",
            "狂野，直到刀找到你。黑手党砍你而保镖没挡，你不死。醒来就是黑手党。安静。没有尸体。新的一家。",
        )
        Role.SURVIVOR -> t(
            "You do not pick a side. Dummy tap. If you are still breathing when someone else wins, you pocket extra JEST. You do not block their win. Just don't die.",
            "Ne biraš stranu. Lažni tap. Ako još dišeš kad neko drugi pobedi, uzimaš extra JEST. Ne blokiraš njihovu pobedu. Samo nemoj da umreš.",
            "Non scegli un lato. Tap finto. Se respiri ancora quando vince qualcun altro, intaschi JEST extra. Non blocchi la loro vittoria. Non morire e basta.",
            "No eliges bando. Toque falso. Si sigues respirando cuando gana otro, te llevas JEST extra. No bloqueas su victoria. Solo no mueras.",
            "Du wählst keine Seite. Schein-Tipp. Atmest du noch, wenn jemand anderes gewinnt, kassierst du extra JEST. Du blockst ihren Sieg nicht. Stirb einfach nicht.",
            "Tu ne choisis pas de camp. Faux tap. Si tu respires encore quand un autre gagne, tu poches du JEST en plus. Tu ne bloques pas leur victoire. Ne meurs pas.",
            "不选边。伪装点击。别人赢的时候你还活着，就多拿一份 JEST。不挡别人的赢。别死就行。",
        )
        Role.AMNESIAC -> t(
            "Empty head. One chance: steal a dead role and wear it. No twins. No whore. No second amnesiac. After that you are whatever you grabbed.",
            "Prazna glava. Jedna šansa: ukradi mrtvu ulogu i obuci je. Ne blizanci. Ne kurva. Ne drugi amnezičar. Posle toga si to što si uzeo.",
            "Testa vuota. Una chance: ruba un ruolo morto e indossalo. Niente gemelli. Niente puttana. Niente secondo amnesiaco. Poi sei quello che hai preso.",
            "Cabeza vacía. Una chance: roba un rol muerto y póntelo. Ni gemelos. Ni puta. Ni otro amnésico. Luego eres lo que agarraste.",
            "Leerer Kopf. Eine Chance: stiehl eine tote Rolle und zieh sie an. Keine Zwillinge. Keine Hure. Kein zweiter Amnestiker. Danach bist du, was du gegriffen hast.",
            "Tête vide. Une chance : vole un rôle mort et mets-le. Pas de jumeaux. Pas de putain. Pas d'autre amnésique. Après tu es ce que tu as pris.",
            "空脑袋。一次机会：偷一具死人的身份穿上。不要双胞胎。不要妓女。不要第二个失忆者。之后你就是拿走的那个。",
        )
    }

    fun roleMoreInfo(role: Role) = when (role) {
        Role.MAFIA -> t(
            "You are on the evil team. Each night you see the other mafia players and vote for one living person to kill. The person with the most mafia votes dies that night, unless a healer saves them. You win when living evil is equal to or greater than living good.",
            "Na strani si zlih. Svake noći vidiš ostalu mafiju i glasate koga da ubijete. Osoba sa najviše glasova mafije umire te noći, osim ako je lekar spasi. Pobeđujete kad je živih zlih jednako ili više od živih dobrih.",
            "Sei nella squadra malvagia. Ogni notte vedi gli altri mafiosi e votate chi uccidere. Chi ha più voti mafia muore quella notte, salvo se un guaritore lo salva. Vincete quando i cattivi vivi sono almeno quanti i buoni.",
            "Estás en el equipo malvado. Cada noche ves a la otra mafia y votáis a quién matar. Quien tenga más votos mafia muere esa noche, salvo que un sanador lo salve. Ganáis cuando el mal vivo es igual o mayor que el bien.",
            "Du bist im bösen Team. Jede Nacht siehst du die anderen Mafiosi und stimmt ab, wen ihr tötet. Wer die meisten Mafia-Stimmen hat, stirbt in dieser Nacht, außer ein Heiler rettet ihn. Ihr gewinnt, wenn lebende Böse mindestens so viele sind wie lebende Gute.",
            "Tu es dans l'équipe du mal. Chaque nuit tu vois les autres mafieux et vous votez qui tuer. La personne avec le plus de votes mafia meurt cette nuit, sauf si un guérisseur la sauve. Vous gagnez quand le mal vivant est supérieur ou égal au bien.",
            "你属于坏人阵营。每晚能看见其他黑手党，并投票杀一个活人。得票最多的人当夜死亡，除非医生救下。活着的坏人不少于好人时获胜。",
        )
        Role.DON -> t(
            "You are mafia, and you lead the night kill. You vote like the other mafia. If two targets are tied, your vote breaks the tie and that person dies.",
            "Ti si mafija i vodiš noćno ubistvo. Glasaš kao ostala mafija. Ako su dve mete izjednačene, tvoj glas rešava nerešeno i ta osoba umire.",
            "Sei mafia e guidi l'uccisione notturna. Voti come gli altri mafiosi. Se due bersagli sono pari, il tuo voto spezza il pareggio e quella persona muore.",
            "Eres mafia y diriges el asesinato nocturno. Votas como el resto. Si dos objetivos empatan, tu voto rompe el empate y esa persona muere.",
            "Du bist Mafia und führst den Nachtmord. Du stimmst wie die anderen. Bei Gleichstand bricht deine Stimme den Gleichstand und diese Person stirbt.",
            "Tu es mafia et tu diriges le meurtre de nuit. Tu votes comme les autres. En cas d'égalité, ta voix tranche et cette personne meurt.",
            "你是黑手党，并主导夜杀。投票方式和其他黑手党一样。如果两人平票，你的票打破平局，那个人死亡。",
        )
        Role.LAWYER -> t(
            "You are mafia. Each night you vote on the kill with the crew. Once per game, after that vote, you may shield one mafia player. If that player is voted out during the day, the exile fails and they stay alive.",
            "Ti si mafija. Svake noći glasaš za ubistvo sa ekipom. Jednom u igri, posle tog glasa, možeš da zaštitiš jednog mafijaša. Ako tog dana glasaju da on ispadne, izbacivanje ne uspe i on ostaje živ.",
            "Sei mafia. Ogni notte voti l'uccisione con la crew. Una volta per partita, dopo quel voto, puoi proteggere un mafioso. Se di giorno votano di esiliarlo, l'esilio fallisce e resta in vita.",
            "Eres mafia. Cada noche votas el asesinato con la banda. Una vez por partida, tras ese voto, puedes proteger a un mafioso. Si de día votan expulsarlo, el exilio falla y sigue vivo.",
            "Du bist Mafia. Jede Nacht stimmst du mit der Crew über den Mord ab. Einmal pro Spiel kannst du danach einen Mafioso schützen. Wird er am Tag rausgewählt, scheitert der Rauswurf und er bleibt am Leben.",
            "Tu es mafia. Chaque nuit tu votes le meurtre avec l'équipe. Une fois par partie, après ce vote, tu peux protéger un mafieux. S'il est voté dehors le jour, l'exil échoue et il reste vivant.",
            "你是黑手党。每晚和同伙一起投票杀人。整局一次，投票后可以保护一名黑手党。如果白天投票放逐他，放逐失败，他继续活着。",
        )
        Role.HEALER -> t(
            "You are on the good team. Each night you choose one living player to protect, including yourself. If that player would die that night, they live. You cannot protect the same player two nights in a row, unless the host turns that setting on.",
            "Na strani si dobrih. Svake noći biraš jednog živog igrača kog štitiš, uključujući sebe. Ako bi ta osoba umrla te noći, ostaje živa. Ne smeš da štitiš istu osobu dve noći zaredom, osim ako je to uključeno u podešavanjima.",
            "Sei nella squadra buona. Ogni notte scegli un giocatore vivo da proteggere, anche te. Se quella persona morirebbe quella notte, resta in vita. Non puoi proteggere la stessa persona due notti di fila, salvo se l'impostazione lo permette.",
            "Estás en el equipo bueno. Cada noche eliges a un jugador vivo para proteger, incluido tú. Si esa persona iba a morir esa noche, vive. No puedes proteger a la misma persona dos noches seguidas, salvo que el ajuste lo permita.",
            "Du bist im guten Team. Jede Nacht wählst du einen lebenden Spieler zum Schutz, auch dich. Würde diese Person in dieser Nacht sterben, bleibt sie am Leben. Dieselbe Person darfst du nicht zwei Nächte hintereinander schützen, außer die Einstellung erlaubt es.",
            "Tu es dans l'équipe du bien. Chaque nuit tu choisis un joueur vivant à protéger, toi compris. Si cette personne devait mourir cette nuit, elle vit. Tu ne peux pas protéger la même personne deux nuits de suite, sauf si le réglage l'autorise.",
            "你属于好人阵营。每晚选一个活人保护，包括自己。如果那个人当夜本该死，会活下来。不能连续两夜保护同一人，除非设置允许。",
        )
        Role.COP -> t(
            "You are on the good team. Each night you inspect one other living player. You see GOOD or BAD, not their exact role. You cannot inspect yourself. Traitor looks GOOD. A player framed that night looks BAD.",
            "Na strani si dobrih. Svake noći proveravaš jednog drugog živog igrača. Vidiš DOBAR ili LOŠ, ne tačnu ulogu. Sebe ne možeš da proveriš. Izdajnik izgleda DOBRO. Igrač kog je nametač obeležio te noći izgleda LOŠE.",
            "Sei nella squadra buona. Ogni notte controlli un altro giocatore vivo. Vedi BUONO o CATTIVO, non il ruolo esatto. Non puoi controllare te stesso. Il traditore appare BUONO. Un giocatore montato quella notte appare CATTIVO.",
            "Estás en el equipo bueno. Cada noche investigas a otro jugador vivo. Ves BUENO o MALO, no el rol exacto. No puedes investigarte. El traidor se ve BUENO. Un jugador montado esa noche se ve MALO.",
            "Du bist im guten Team. Jede Nacht prüfst du einen anderen lebenden Spieler. Du siehst GUT oder BÖSE, nicht die genaue Rolle. Dich selbst kannst du nicht prüfen. Der Verräter wirkt GUT. Ein in dieser Nacht geframter Spieler wirkt BÖSE.",
            "Tu es dans l'équipe du bien. Chaque nuit tu inspectes un autre joueur vivant. Tu vois BON ou MAUVAIS, pas le rôle exact. Tu ne peux pas t'inspecter. Le traître paraît BON. Un joueur piégé cette nuit paraît MAUVAIS.",
            "你属于好人阵营。每晚调查另一名活人。只看到好或坏，不是准确身份。不能查自己。叛徒显示为好人。当晚被栽赃的人显示为坏人。",
        )
        Role.CIVILIAN -> t(
            "You are on the good team. You have no night power. Each night you do a fake action so nobody can tell who has a real power. During the day, talk and vote to find the evil players. You win when no evil players remain.",
            "Na strani si dobrih. Nemaš noćnu moć. Svake noći radiš lažnu akciju da se ne vidi ko ima pravu moć. Danju pričaj i glasaj da nađete zle igrače. Pobeđujete kad ne ostane nijedan zli igrač.",
            "Sei nella squadra buona. Non hai potere notturno. Ogni notte fai un'azione finta così non si capisce chi ha un vero potere. Di giorno parla e vota per trovare i cattivi. Vincete quando non restano cattivi.",
            "Estás en el equipo bueno. No tienes poder nocturno. Cada noche haces una acción falsa para que no se vea quién tiene poder de verdad. De día habla y vota para hallar a los malos. Ganáis cuando no queda ningún malvado.",
            "Du bist im guten Team. Du hast keine Nachtmacht. Jede Nacht machst du eine Scheinaktion, damit niemand sieht, wer echte Macht hat. Am Tag redet und stimmt ab, um die Bösen zu finden. Ihr gewinnt, wenn keine Bösen mehr da sind.",
            "Tu es dans l'équipe du bien. Tu n'as pas de pouvoir de nuit. Chaque nuit tu fais une fausse action pour qu'on ne voie pas qui a un vrai pouvoir. Le jour, parle et vote pour trouver les méchants. Vous gagnez quand il n'y a plus de méchants.",
            "你属于好人阵营。没有夜间技能。每晚做伪装动作，让人看不出谁有真技能。白天讨论并投票找出坏人。没有坏人时好人获胜。",
        )
        Role.HUNTER -> t(
            "You are on the good team. You have no night action while you are alive. If you die at night or are voted out during the day, you immediately choose one living player. That player dies too.",
            "Na strani si dobrih. Dok si živ nemaš noćnu akciju. Ako umreš noću ili te izbace danju, odmah biraš jednog živog igrača. Ta osoba umire i ona.",
            "Sei nella squadra buona. Da vivo non hai azione notturna. Se muori di notte o vieni esiliato di giorno, scegli subito un giocatore vivo. Anche quella persona muore.",
            "Estás en el equipo bueno. En vida no tienes acción nocturna. Si mueres de noche o te expulsan de día, eliges al momento a un jugador vivo. Esa persona también muere.",
            "Du bist im guten Team. Solange du lebst, hast du keine Nachtaktion. Stirbst du nachts oder wirst am Tag rausgewählt, wählst du sofort einen lebenden Spieler. Diese Person stirbt ebenfalls.",
            "Tu es dans l'équipe du bien. Tant que tu vis, tu n'as pas d'action de nuit. Si tu meurs la nuit ou es exilé le jour, tu choisis tout de suite un joueur vivant. Cette personne meurt aussi.",
            "你属于好人阵营。活着时没有夜间行动。如果你夜里死亡或白天被放逐，立刻选一个活人。那个人也会死。",
        )
        Role.SEER -> t(
            "You are on the good team. Each night you inspect one other living player. You see their exact role, not only good or bad. You cannot inspect yourself.",
            "Na strani si dobrih. Svake noći proveravaš jednog drugog živog igrača. Vidiš tačnu ulogu, ne samo dobar ili loš. Sebe ne možeš da proveriš.",
            "Sei nella squadra buona. Ogni notte controlli un altro giocatore vivo. Vedi il ruolo esatto, non solo buono o cattivo. Non puoi controllare te stesso.",
            "Estás en el equipo bueno. Cada noche investigas a otro jugador vivo. Ves el rol exacto, no solo bueno o malo. No puedes investigarte.",
            "Du bist im guten Team. Jede Nacht prüfst du einen anderen lebenden Spieler. Du siehst die genaue Rolle, nicht nur gut oder böse. Dich selbst kannst du nicht prüfen.",
            "Tu es dans l'équipe du bien. Chaque nuit tu inspectes un autre joueur vivant. Tu vois le rôle exact, pas seulement bon ou mauvais. Tu ne peux pas t'inspecter.",
            "你属于好人阵营。每晚调查另一名活人。看到准确身份，不只是好或坏。不能查自己。",
        )
        Role.BODYGUARD -> t(
            "You are on the good team. Each night you protect one other living player, not yourself. If the mafia tries to kill that player, you die instead and they live. You cannot protect the same player two nights in a row, unless that setting is on.",
            "Na strani si dobrih. Svake noći štitiš jednog drugog živog igrača, ne sebe. Ako mafija pokuša da ubije tu osobu, ti umireš umesto nje, a ona ostaje živa. Ne smeš da štitiš istu osobu dve noći zaredom, osim ako je to uključeno.",
            "Sei nella squadra buona. Ogni notte proteggi un altro giocatore vivo, non te. Se la mafia prova a ucciderlo, muori tu al suo posto e lui vive. Non puoi proteggere la stessa persona due notti di fila, salvo se l'impostazione lo permette.",
            "Estás en el equipo bueno. Cada noche proteges a otro jugador vivo, no a ti. Si la mafia intenta matarlo, mueres tú y él vive. No puedes proteger a la misma persona dos noches seguidas, salvo que el ajuste lo permita.",
            "Du bist im guten Team. Jede Nacht schützt du einen anderen lebenden Spieler, nicht dich. Versucht die Mafia, ihn zu töten, stirbst du stattdessen und er lebt. Dieselbe Person darfst du nicht zwei Nächte hintereinander schützen, außer die Einstellung erlaubt es.",
            "Tu es dans l'équipe du bien. Chaque nuit tu protèges un autre joueur vivant, pas toi. Si la mafia tente de le tuer, tu meurs à sa place et il vit. Tu ne peux pas protéger la même personne deux nuits de suite, sauf si le réglage l'autorise.",
            "你属于好人阵营。每晚保护另一名活人，不能保护自己。如果黑手党要杀那个人，你替他死，他活着。不能连续两夜保护同一人，除非设置允许。",
        )
        Role.JOKER -> t(
            "You are wild. You have no night power. You win only if the table votes you out during the day. If you die at night, you lose. You do not win with good or with evil.",
            "Ti si wild. Nemaš noćnu moć. Pobeđuješ samo ako te danju izbace glasovima. Ako umreš noću, gubiš. Ne pobeđuješ ni sa dobrima ni sa zlima.",
            "Sei wild. Non hai potere notturno. Vinci solo se ti esiliano di giorno. Se muori di notte, perdi. Non vinci con i buoni né con i cattivi.",
            "Eres wild. No tienes poder nocturno. Ganas solo si te expulsan de día. Si mueres de noche, pierdes. No ganas con el bien ni con el mal.",
            "Du bist Wild. Du hast keine Nachtmacht. Du gewinnst nur, wenn dich der Tisch am Tag rauswählt. Stirbst du nachts, verlierst du. Du gewinnst weder mit den Guten noch mit den Bösen.",
            "Tu es wild. Tu n'as pas de pouvoir de nuit. Tu gagnes seulement si on t'exile le jour. Si tu meurs la nuit, tu perds. Tu ne gagnes ni avec le bien ni avec le mal.",
            "你是狂野身份。没有夜间技能。只有白天被投票放逐才算赢。夜里死亡则输。不跟好人或坏人一起赢。",
        )
        Role.KILLER -> t(
            "You are wild. You are not mafia and not good. On even nights (2, 4, 6...) you choose one living player to kill. You win only if you are the last player alive.",
            "Ti si wild. Nisi mafija i nisi dobar. Na parnim noćima (2, 4, 6...) biraš jednog živog igrača kog ubijaš. Pobeđuješ samo ako ostaneš poslednji živ.",
            "Sei wild. Non sei mafia e non sei buono. Nelle notti pari (2, 4, 6...) scegli un giocatore vivo da uccidere. Vinci solo se resti l'ultimo in vita.",
            "Eres wild. No eres mafia ni bueno. En noches pares (2, 4, 6...) eliges a un jugador vivo para matar. Ganas solo si eres el último vivo.",
            "Du bist Wild. Du bist nicht Mafia und nicht gut. In geraden Nächten (2, 4, 6...) wählst du einen lebenden Spieler zum Töten. Du gewinnst nur, wenn du als Letzter lebst.",
            "Tu es wild. Tu n'es ni mafia ni bon. Les nuits paires (2, 4, 6...) tu choisis un joueur vivant à tuer. Tu gagnes seulement si tu es le dernier vivant.",
            "你是狂野身份。不是黑手党，也不是好人。双数夜（2、4、6…）选一个活人杀死。只有你成为最后一个活人时获胜。",
        )
        Role.LUNATIC -> t(
            "You are on the evil team, but the game shows you CIVILIAN. You do not see the mafia and they do not see you. You have no night power. You still count as evil for winning.",
            "Na strani si zlih, ali igra tebi pokazuje CIVIL. Ne vidiš mafiju i oni ne vide tebe. Nemaš noćnu moć. I dalje se brojiš kao zao za pobedu.",
            "Sei nella squadra malvagia, ma il gioco ti mostra CIVILE. Non vedi la mafia e loro non vedono te. Non hai potere notturno. Conti comunque come cattivo per la vittoria.",
            "Estás en el equipo malvado, pero el juego te muestra CIVIL. No ves a la mafia y ellos no te ven. No tienes poder nocturno. Sigues contando como mal para ganar.",
            "Du bist im bösen Team, aber das Spiel zeigt dir ZIVILIST. Du siehst die Mafia nicht und sie sehen dich nicht. Du hast keine Nachtmacht. Für den Sieg zählst du trotzdem als böse.",
            "Tu es dans l'équipe du mal, mais le jeu t'affiche CIVIL. Tu ne vois pas la mafia et elle ne te voit pas. Tu n'as pas de pouvoir de nuit. Tu comptes quand même comme méchant pour la victoire.",
            "你属于坏人阵营，但游戏显示你是平民。你看不见黑手党，他们也看不见你。没有夜间技能。胜利计算时你仍算坏人。",
        )
        Role.DRUNK -> t(
            "You are wild. On night 1 and night 2 you do not see your role. You only do a fake action. From night 3 you see that you are the Drunk. You have no other power.",
            "Ti si wild. Prve i druge noći ne vidiš svoju ulogu. Radiš samo lažnu akciju. Od treće noći vidiš da si Pijani. Nemaš drugu moć.",
            "Sei wild. Nelle notti 1 e 2 non vedi il tuo ruolo. Fai solo un'azione finta. Dalla notte 3 vedi che sei l'Ubriaco. Non hai altri poteri.",
            "Eres wild. En las noches 1 y 2 no ves tu rol. Solo haces una acción falsa. Desde la noche 3 ves que eres el Borracho. No tienes más poder.",
            "Du bist Wild. In Nacht 1 und 2 siehst du deine Rolle nicht. Du machst nur eine Scheinaktion. Ab Nacht 3 siehst du, dass du der Betrunkene bist. Du hast keine weitere Macht.",
            "Tu es wild. Nuits 1 et 2 tu ne vois pas ton rôle. Tu fais seulement une fausse action. Dès la nuit 3 tu vois que tu es l'Ivrogne. Tu n'as pas d'autre pouvoir.",
            "你是狂野身份。第 1、2 夜看不到自己的身份，只能做伪装动作。从第 3 夜起看到自己是醉汉。没有其他技能。",
        )
        Role.TWIN_CIVIL -> t(
            "You are on the good team. Civil Twins come in pairs. You see your twin, not other pairs. If your twin dies, you die at the same time. You have no other night power.",
            "Na strani si dobrih. Civil Blizanci idu u parovima. Vidiš svog blizanca, ne druge parove. Ako tvoj blizanac umre, umireš u istom trenutku. Nemaš drugu noćnu moć.",
            "Sei nella squadra buona. I Gemelli Civili vanno a coppie. Vedi il tuo gemello, non le altre coppie. Se muore il tuo gemello, muori nello stesso momento. Non hai altro potere notturno.",
            "Estás en el equipo bueno. Los Gemelos Civiles van por parejas. Ves a tu gemelo, no a otras parejas. Si muere tu gemelo, mueres al mismo tiempo. No tienes más poder nocturno.",
            "Du bist im guten Team. Zivil-Zwillinge kommen paarweise. Du siehst deinen Zwilling, nicht andere Paare. Stirbt dein Zwilling, stirbst du im selben Moment. Du hast keine weitere Nachtmacht.",
            "Tu es dans l'équipe du bien. Les Jumeaux civils vont par paires. Tu vois ton jumeau, pas les autres paires. Si ton jumeau meurt, tu meurs au même moment. Tu n'as pas d'autre pouvoir de nuit.",
            "你属于好人阵营。平民双胞胎成对出现。你只看见自己的双胞胎，看不见其他对。你的双胞胎死，你同时死。没有其他夜间技能。",
        )
        Role.TWIN_MAFIA -> t(
            "You are mafia. Mafia Twins come in pairs. You see your twin and vote on the night kill with the crew. If your twin dies, you die at the same time. Other twin pairs are separate.",
            "Ti si mafija. Mafija Blizanci idu u parovima. Vidiš svog blizanca i glasaš za noćno ubistvo sa ekipom. Ako tvoj blizanac umre, umireš u istom trenutku. Drugi parovi su odvojeni.",
            "Sei mafia. I Gemelli Mafia vanno a coppie. Vedi il tuo gemello e voti l'uccisione notturna con la crew. Se muore il tuo gemello, muori nello stesso momento. Le altre coppie sono separate.",
            "Eres mafia. Los Gemelos Mafia van por parejas. Ves a tu gemelo y votas el asesinato nocturno con la banda. Si muere tu gemelo, mueres al mismo tiempo. Las otras parejas son aparte.",
            "Du bist Mafia. Mafia-Zwillinge kommen paarweise. Du siehst deinen Zwilling und stimmst mit der Crew über den Nachtmord ab. Stirbt dein Zwilling, stirbst du im selben Moment. Andere Paare sind getrennt.",
            "Tu es mafia. Les Jumeaux mafia vont par paires. Tu vois ton jumeau et tu votes le meurtre de nuit avec l'équipe. Si ton jumeau meurt, tu meurs au même moment. Les autres paires sont séparées.",
            "你是黑手党。黑手党双胞胎成对出现。你看见自己的双胞胎，并和同伙一起投夜杀。你的双胞胎死，你同时死。其他对是分开的。",
        )
        Role.WHORE -> t(
            "Each night you choose one other living player. That player cannot use their night power until morning. You cannot choose the same player two nights in a row. In Roles you set how they win: GOOD (wins with good), EVIL (wins with evil), JUST A PEST (never wins, only blocks), PLAY ALONE (wins when two or fewer players are left), or they choose at the start of the game.",
            "Svake noći biraš jednog drugog živog igrača. Ta osoba ne može da koristi noćnu moć do jutra. Ne smeš istu osobu dve noći zaredom. U Ulogama se postavlja kako pobeđuje: DOBRA (sa dobrima), ZLA (sa zlima), SAMO SMETA (nikad ne pobeđuje, samo blokira), IGRA SAMA (pobedi kad ostanu dvoje ili manje), ili bira na početku igre.",
            "Ogni notte scegli un altro giocatore vivo. Quella persona non può usare il potere notturno fino al mattino. Non puoi scegliere la stessa persona due notti di fila. In Ruoli imposti come vince: BUONA (con i buoni), CATTIVA (con i cattivi), SOLO DISTURBA (non vince mai, blocca soltanto), GIOCA DA SOLA (vince quando restano due o meno), oppure sceglie all'inizio.",
            "Cada noche eliges a otro jugador vivo. Esa persona no puede usar su poder nocturno hasta el alba. No puedes elegir a la misma persona dos noches seguidas. En Roles se elige cómo gana: BUENA (con el bien), MALA (con el mal), SOLO MOLESTA (nunca gana, solo bloquea), JUEGA SOLA (gana si quedan dos o menos), o elige al empezar.",
            "Jede Nacht wählst du einen anderen lebenden Spieler. Diese Person kann bis zum Morgen ihre Nachtmacht nicht nutzen. Dieselbe Person darfst du nicht zwei Nächte hintereinander wählen. Unter Rollen stellst du den Sieg ein: GUT (mit den Guten), BÖSE (mit den Bösen), NUR STÖRERIN (gewinnt nie, blockt nur), SPIELT ALLEIN (gewinnt, wenn zwei oder weniger übrig sind), oder Wahl am Start.",
            "Chaque nuit tu choisis un autre joueur vivant. Cette personne ne peut pas utiliser son pouvoir de nuit jusqu'au matin. Tu ne peux pas choisir la même personne deux nuits de suite. Dans Rôles tu règles la victoire : BIEN (avec les bons), MAL (avec les méchants), JUSTE GÊNE (ne gagne jamais, bloque seulement), JOUE SEULE (gagne s'il reste deux joueurs ou moins), ou choix en début de partie.",
            "每晚选另一名活人。直到早晨，那个人不能使用夜间技能。不能连续两夜选同一人。在身份里设置如何获胜：好人（和好人一起赢）、坏人（和坏人一起赢）、只捣乱（永不获胜，只封技能）、独自获胜（剩下两人及以下时赢），或开局自选。",
        )
        Role.MAYOR -> t(
            "You are on the good team. Each night you may reveal yourself, or stay hidden. After you reveal, everyone knows you are the Mayor. Then your vote in a phone day vote counts as two votes. In a live vote, the table should also count your hand as two votes.",
            "Na strani si dobrih. Svake noći možeš da se otkriješ, ili da ostaneš skriven. Kad se otkriješ, svi znaju da si Gradonačelnik. Posle toga tvoj glas na telefonskom dnevnom glasanju vredi kao dva glasa. Na glasanju uživo sto treba da broji tvoju ruku kao dva glasa.",
            "Sei nella squadra buona. Ogni notte puoi rivelarti, o restare nascosto. Dopo la rivelazione tutti sanno che sei il Sindaco. Da allora il tuo voto nel voto telefonico diurno conta come due voti. Nel voto dal vivo il tavolo deve contare la tua mano come due voti.",
            "Estás en el equipo bueno. Cada noche puedes revelarte, o seguir oculto. Al revelarte, todos saben que eres el Alcalde. Luego tu voto en la votación diurna por teléfono cuenta como dos votos. En voto en vivo, la mesa debe contar tu mano como dos votos.",
            "Du bist im guten Team. Jede Nacht kannst du dich zeigen oder versteckt bleiben. Nach der Enthüllung wissen alle, dass du der Bürgermeister bist. Danach zählt deine Stimme bei der Handy-Tagesabstimmung als zwei Stimmen. Bei Live-Abstimmung soll der Tisch deine Hand auch als zwei Stimmen zählen.",
            "Tu es dans l'équipe du bien. Chaque nuit tu peux te révéler, ou rester caché. Après révélation, tout le monde sait que tu es le Maire. Ensuite ton vote au vote téléphone du jour compte pour deux. En vote en direct, la table doit aussi compter ta main pour deux.",
            "你属于好人阵营。每晚可以选择公开自己，或继续隐藏。公开后所有人知道你是市长。之后手机白天投票时，你的票算两票。现场投票时，牌桌也应把你的举手算两票。",
        )
        Role.NECROMANCER -> t(
            "You are on the good team. Each night, if someone is already dead, you choose one dead player. You see that player's exact role. You cannot inspect living players. If nobody is dead yet, you do a fake action.",
            "Na strani si dobrih. Svake noći, ako je neko već mrtav, biraš jednog mrtvog igrača. Vidiš tačnu ulogu te osobe. Žive igrače ne možeš da proveravaš. Ako još niko nije umro, radiš lažnu akciju.",
            "Sei nella squadra buona. Ogni notte, se qualcuno è già morto, scegli un giocatore morto. Vedi il ruolo esatto di quella persona. Non puoi controllare i vivi. Se nessuno è ancora morto, fai un'azione finta.",
            "Estás en el equipo bueno. Cada noche, si alguien ya está muerto, eliges a un jugador muerto. Ves el rol exacto de esa persona. No puedes investigar a los vivos. Si aún nadie ha muerto, haces una acción falsa.",
            "Du bist im guten Team. Jede Nacht, wenn schon jemand tot ist, wählst du einen toten Spieler. Du siehst die genaue Rolle dieser Person. Lebende kannst du nicht prüfen. Ist noch niemand tot, machst du eine Scheinaktion.",
            "Tu es dans l'équipe du bien. Chaque nuit, si quelqu'un est déjà mort, tu choisis un joueur mort. Tu vois le rôle exact de cette personne. Tu ne peux pas inspecter les vivants. Si personne n'est encore mort, tu fais une fausse action.",
            "你属于好人阵营。每晚如果已有人死亡，选一名死者。你会看到那个人的准确身份。不能调查活人。如果还没有人死，做伪装动作。",
        )
        Role.VIGILANTE -> t(
            "You are on the good team. Once per game, at night, you may shoot one other living player. That player dies the same night, unless a healer protects them. You may skip and keep the shot for a later night. After you shoot, you have no more shots.",
            "Na strani si dobrih. Jednom u igri, noću, možeš da upucaš jednog drugog živog igrača. Ta osoba umire iste noći, osim ako je lekar zaštiti. Možeš da preskočiš i sačuvaš hitac za kasniju noć. Kad upucaš, nemaš više hitaca.",
            "Sei nella squadra buona. Una volta per partita, di notte, puoi sparare a un altro giocatore vivo. Quella persona muore la stessa notte, salvo se un guaritore la protegge. Puoi saltare e tenere il colpo per una notte dopo. Dopo aver sparato, non hai più colpi.",
            "Estás en el equipo bueno. Una vez por partida, de noche, puedes disparar a otro jugador vivo. Esa persona muere esa misma noche, salvo que un sanador la proteja. Puedes saltar y guardar el disparo para otra noche. Cuando disparas, no te quedan más disparos.",
            "Du bist im guten Team. Einmal pro Spiel darfst du nachts auf einen anderen lebenden Spieler schießen. Diese Person stirbt in derselben Nacht, außer ein Heiler schützt sie. Du kannst überspringen und den Schuss für eine spätere Nacht behalten. Nach dem Schuss hast du keine Schüsse mehr.",
            "Tu es dans l'équipe du bien. Une fois par partie, la nuit, tu peux tirer sur un autre joueur vivant. Cette personne meurt la même nuit, sauf si un guérisseur la protège. Tu peux passer et garder le tir pour une nuit plus tard. Après avoir tiré, tu n'as plus de tirs.",
            "你属于好人阵营。整局一次，夜里可以开枪打另一名活人。那个人当夜死亡，除非医生保护了他们。可以跳过，把这一枪留到以后的夜晚。开过枪后不能再开。",
        )
        Role.FRAMER -> t(
            "You are mafia. Each night you vote on the kill with the crew. After that vote you may choose one living player. If the cop inspects that player the same night, the cop sees BAD, even if the player is good. You may skip the frame.",
            "Ti si mafija. Svake noći glasaš za ubistvo sa ekipom. Posle tog glasa možeš da izabereš jednog živog igrača. Ako policajac te noći proveri tu osobu, vidi LOŠ, čak i ako je ta osoba dobra. Možeš da preskočiš nametanje.",
            "Sei mafia. Ogni notte voti l'uccisione con la crew. Dopo quel voto puoi scegliere un giocatore vivo. Se lo sbirro controlla quella persona la stessa notte, vede CATTIVO, anche se è buona. Puoi saltare la montatura.",
            "Eres mafia. Cada noche votas el asesinato con la banda. Tras ese voto puedes elegir a un jugador vivo. Si el poli lo investiga esa misma noche, ve MALO, aunque sea bueno. Puedes saltar el montaje.",
            "Du bist Mafia. Jede Nacht stimmst du mit der Crew über den Mord ab. Danach darfst du einen lebenden Spieler wählen. Prüft der Bulle diese Person in derselben Nacht, sieht er BÖSE, auch wenn sie gut ist. Du kannst das Framen überspringen.",
            "Tu es mafia. Chaque nuit tu votes le meurtre avec l'équipe. Après ce vote tu peux choisir un joueur vivant. Si le flic l'inspecte la même nuit, il voit MAUVAIS, même si la personne est bonne. Tu peux passer le piège.",
            "你是黑手党。每晚和同伙一起投票杀人。之后可以选一名活人。如果警察当晚调查那个人，会看到坏人，即使那个人是好人。可以跳过栽赃。",
        )
        Role.TRAITOR -> t(
            "You are on the evil team. You do not see the mafia, and they do not see you. You have no night power. If the cop inspects you, they see GOOD. You still count as evil for winning.",
            "Na strani si zlih. Ne vidiš mafiju, i oni ne vide tebe. Nemaš noćnu moć. Ako te policajac proveri, vidi DOBAR. I dalje se brojiš kao zao za pobedu.",
            "Sei nella squadra malvagia. Non vedi la mafia e loro non vedono te. Non hai potere notturno. Se lo sbirro ti controlla, vede BUONO. Conti comunque come cattivo per la vittoria.",
            "Estás en el equipo malvado. No ves a la mafia y ellos no te ven. No tienes poder nocturno. Si el poli te investiga, ve BUENO. Sigues contando como mal para ganar.",
            "Du bist im bösen Team. Du siehst die Mafia nicht und sie sehen dich nicht. Du hast keine Nachtmacht. Prüft dich der Bulle, sieht er GUT. Für den Sieg zählst du trotzdem als böse.",
            "Tu es dans l'équipe du mal. Tu ne vois pas la mafia et elle ne te voit pas. Tu n'as pas de pouvoir de nuit. Si le flic t'inspecte, il voit BON. Tu comptes quand même comme méchant pour la victoire.",
            "你属于坏人阵营。看不见黑手党，他们也看不见你。没有夜间技能。警察调查你会看到好人。胜利计算时你仍算坏人。",
        )
        Role.POISONER -> t(
            "You are on the evil team, but you are not in the mafia group. You do not see the mafia. Each night you may choose one living player to poison, or skip. That player dies the next night, unless a healer protects them on the night they would die. You win with evil.",
            "Na strani si zlih, ali nisi u grupi mafije. Ne vidiš mafiju. Svake noći možeš da izabereš jednog živog igrača kog truješ, ili da preskočiš. Ta osoba umire sledeće noći, osim ako je lekar zaštiti noći kad treba da umre. Pobeđuješ sa zlima.",
            "Sei nella squadra malvagia, ma non sei nel gruppo mafia. Non vedi la mafia. Ogni notte puoi scegliere un giocatore vivo da avvelenare, o saltare. Quella persona muore la notte dopo, salvo se un guaritore la protegge la notte in cui dovrebbe morire. Vinci con i cattivi.",
            "Estás en el equipo malvado, pero no estás en el grupo mafia. No ves a la mafia. Cada noche puedes elegir a un jugador vivo para envenenar, o saltar. Esa persona muere la noche siguiente, salvo que un sanador la proteja la noche en que debía morir. Ganas con el mal.",
            "Du bist im bösen Team, aber nicht in der Mafia-Gruppe. Du siehst die Mafia nicht. Jede Nacht darfst du einen lebenden Spieler vergiften oder überspringen. Diese Person stirbt in der nächsten Nacht, außer ein Heiler schützt sie in der Nacht, in der sie sterben würde. Du gewinnst mit den Bösen.",
            "Tu es dans l'équipe du mal, mais tu n'es pas dans le groupe mafia. Tu ne vois pas la mafia. Chaque nuit tu peux choisir un joueur vivant à empoisonner, ou passer. Cette personne meurt la nuit suivante, sauf si un guérisseur la protège la nuit où elle devrait mourir. Tu gagnes avec le mal.",
            "你属于坏人阵营，但不在黑手党小组里。看不见黑手党。每晚可以选一名活人下毒，或跳过。那个人在下一夜死亡，除非医生在他本该死的那夜保护了他。你和坏人一起获胜。",
        )
        Role.CURSED -> t(
            "You start as wild and have no night power. If the mafia tries to kill you and no bodyguard takes the hit, you do not die. You become mafia instead. Nobody is announced dead from that hit. From the next night you play as mafia.",
            "Počinješ kao wild i nemaš noćnu moć. Ako mafija pokuša da te ubije i nijedan telohranitelj ne primi udarac, ne umireš. Umesto toga postaješ mafija. Niko se ne objavljuje mrtav od tog udarca. Od sledeće noći igraš kao mafija.",
            "Inizi come wild e non hai potere notturno. Se la mafia prova a ucciderti e nessuna guardia prende il colpo, non muori. Diventi mafia. Nessuno viene annunciato morto per quel colpo. Dalla notte dopo giochi come mafia.",
            "Empiezas como wild y no tienes poder nocturno. Si la mafia intenta matarte y ningún guardaespaldas recibe el golpe, no mueres. Pasas a ser mafia. Nadie se anuncia muerto por ese golpe. Desde la noche siguiente juegas como mafia.",
            "Du startest als Wild und hast keine Nachtmacht. Versucht die Mafia, dich zu töten, und kein Leibwächter nimmt den Schlag, stirbst du nicht. Du wirst stattdessen Mafia. Niemand wird durch diesen Schlag als tot verkündet. Ab der nächsten Nacht spielst du als Mafia.",
            "Tu commences wild et tu n'as pas de pouvoir de nuit. Si la mafia tente de te tuer et qu'aucun garde ne prend le coup, tu ne meurs pas. Tu deviens mafia. Personne n'est annoncé mort pour ce coup. Dès la nuit suivante tu joues comme mafia.",
            "开局是狂野身份，没有夜间技能。如果黑手党要杀你且没有保镖挡刀，你不会死，而是变成黑手党。这次袭击不会公布死者。从下一夜起你按黑手党行动。",
        )
        Role.SURVIVOR -> t(
            "You are wild. You have no night power and you do not choose a side. If you are still alive when the game ends, you get one extra JEST. You do not stop anyone else from winning.",
            "Ti si wild. Nemaš noćnu moć i ne biraš stranu. Ako si još živ kad se igra završi, dobijaš jedan extra JEST. Ne sprečavaš nikog drugog da pobedi.",
            "Sei wild. Non hai potere notturno e non scegli una squadra. Se sei ancora vivo a fine partita, ricevi un JEST extra. Non impedisci agli altri di vincere.",
            "Eres wild. No tienes poder nocturno y no eliges bando. Si sigues vivo al terminar la partida, recibes un JEST extra. No impides que gane nadie más.",
            "Du bist Wild. Du hast keine Nachtmacht und wählst keine Seite. Lebst du noch, wenn das Spiel endet, bekommst du ein extra JEST. Du verhinderst den Sieg anderer nicht.",
            "Tu es wild. Tu n'as pas de pouvoir de nuit et tu ne choisis pas de camp. Si tu es encore vivant à la fin, tu reçois un JEST extra. Tu n'empêches personne d'autre de gagner.",
            "你是狂野身份。没有夜间技能，也不选边。游戏结束时如果你还活着，额外获得 1 个 JEST。不阻止其他人获胜。",
        )
        Role.AMNESIAC -> t(
            "You are wild. Once per game, at night, you may choose one dead player and take that player's role. After that you play as the new role. You cannot take Amnesiac, Whore, or Twin. If nobody usable is dead yet, you skip.",
            "Ti si wild. Jednom u igri, noću, možeš da izabereš jednog mrtvog igrača i uzmeš tu ulogu. Posle toga igraš kao nova uloga. Ne možeš da uzmeš Amnezičara, Kurvu ili Blizanca. Ako još nema pogodnog mrtvog, preskačeš.",
            "Sei wild. Una volta per partita, di notte, puoi scegliere un giocatore morto e prendere quel ruolo. Poi giochi con il nuovo ruolo. Non puoi prendere Amnesiaco, Puttana o Gemello. Se nessuno adatto è ancora morto, salti.",
            "Eres wild. Una vez por partida, de noche, puedes elegir a un jugador muerto y tomar ese rol. Luego juegas con el rol nuevo. No puedes tomar Amnésico, Puta o Gemelo. Si aún no hay ningún muerto válido, saltas.",
            "Du bist Wild. Einmal pro Spiel darfst du nachts einen toten Spieler wählen und dessen Rolle übernehmen. Danach spielst du als neue Rolle. Amnestiker, Hure und Zwilling kannst du nicht nehmen. Ist noch niemand Passendes tot, überspringst du.",
            "Tu es wild. Une fois par partie, la nuit, tu peux choisir un joueur mort et prendre ce rôle. Ensuite tu joues le nouveau rôle. Tu ne peux pas prendre Amnésique, Putain ou Jumeau. Si personne de valable n'est encore mort, tu passes.",
            "你是狂野身份。整局一次，夜里可以选一名死者并拿走那个人的身份。之后按新身份行动。不能拿失忆者、妓女或双胞胎。如果还没有可拿的死者，就跳过。",
        )
    }

    fun winnerTitle(w: Winner) = when (w) {
        Winner.MAFIA -> mafiaWins
        Winner.GOOD -> goodWins
        Winner.JOKER -> jokerWins
        Winner.KILLER -> killerWins
        Winner.WHORE -> whoreWins
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
        "Un telefono ospita la partita. Sedete nell'ordine dei nomi. Ogni notte il telefono gira. Sblocca solo al tuo turno. Dopo la notte: riepilogo, discussione, voto diurno. Dal vivo = il tavolo decide, l'host tocca chi esce. Telefono = si passa di nuovo.",
        "Un teléfono lleva la partida. Sentaos en el orden de los nombres. Cada noche el teléfono da la vuelta. Desbloquea solo en tu turno. Tras la noche: resumen, debate, voto diurno. En vivo = la mesa decide, el anfitrión toca quién sale. Teléfono = se pasa otra vez.",
        "Ein Handy führt das Spiel. Sitzt in der Reihenfolge der Namen. Jede Nacht wandert das Handy. Nur in deinem Zug entsperren. Nach der Nacht: Zusammenfassung, Diskussion, Tagesabstimmung. Live = der Tisch entscheidet, der Host tippt wer raus ist. Handy = wieder herumgeben.",
        "Un téléphone mène la partie. Asseyez-vous dans l'ordre des noms. Chaque nuit le téléphone tourne. Déverrouille seulement à ton tour. Après la nuit : résumé, débat, vote du jour. En direct = la table décide, l'hôte tape qui sort. Téléphone = on repasse.",
        "一部手机主持对局。按输入名字的顺序坐。每晚传手机。只在自己回合解锁。夜晚之后：结算、讨论、白天投票。现场投票 = 牌桌决定，房主点谁出局。手机投票 = 再传一圈。",
    )
    val nightBody = t(
        "Everyone living gets the phone. Power roles act in secret. Others do a dummy tap so nobody can tell who has a power. Then the night resolves: mafia votes, healer can save, bodyguard can take the hit, killer may strike on even nights.",
        "Svaki živi dobije telefon. Moćne uloge rade tajno. Ostali tapnu lažnu akciju da se ne vidi ko ima moć. Noć se rešava: mafija glasa, lekar može da spasi, telohranitelj može da primi udarac, ubica udara na parnim noćima.",
        "Ogni vivo prende il telefono. I ruoli con potere agiscono in segreto. Gli altri fanno un tap finto così non si capisce chi ha il potere. Poi la notte si risolve: la mafia vota, il guaritore può salvare, la guardia può prendere il colpo, il killer colpisce nelle notti pari.",
        "Cada vivo recibe el teléfono. Los roles con poder actúan en secreto. Los demás tocan una acción falsa para que no se vea quién tiene poder. Luego se resuelve la noche: la mafia vota, el sanador puede salvar, el guardaespaldas puede recibir el golpe, el asesino pega en noches pares.",
        "Jeder Lebende bekommt das Handy. Machtrollen handeln geheim. Die anderen tippen eine Scheinaktion, damit niemand sieht, wer Macht hat. Dann löst sich die Nacht: Mafia stimmt, Heiler kann retten, Leibwächter kann den Schlag nehmen, Killer schlägt in geraden Nächten.",
        "Chaque vivant a le téléphone. Les rôles à pouvoir agissent en secret. Les autres tapent une fausse action pour qu'on ne voie pas qui a un pouvoir. Puis la nuit se résout : la mafia vote, le guérisseur peut sauver, le garde peut prendre le coup, le tueur frappe les nuits paires.",
        "每个活着的人都拿到手机。有技能的身份偷偷行动。其他人点伪装，让人看不出谁有技能。然后结算夜晚：黑手党投票，医生可救人，保镖可挡刀，杀手在双数夜动手。",
    )
    val dayBody = t(
        "Talk freely. Then vote someone out, or nobody. A tie on the phone vote saves everyone. The lawyer can block one mafia exile. The hunter, if they just died, still picks one last target.",
        "Pričajte slobodno. Onda izbacite nekoga, ili nikoga. Nerešeno na telefonu = niko ne ispada. Advokat može jednom da blokira izbacivanje mafije. Lovac, ako je upravo umro, bira još jednu metu.",
        "Parlate pure. Poi esiliate qualcuno, o nessuno. Pareggio sul telefono = nessuno esce. L'avvocato può bloccare un esilio mafioso. Il cacciatore, se è appena morto, sceglie ancora un bersaglio.",
        "Hablad libremente. Luego expulsad a alguien, o a nadie. Empate en el teléfono = nadie sale. El abogado puede bloquear un exilio mafioso. El cazador, si acaba de morir, elige un último objetivo.",
        "Redet frei. Dann werft jemanden raus, oder niemanden. Gleichstand am Handy = niemand fliegt. Der Anwalt kann einen Mafia-Rauswurf blocken. Der Jäger, falls er gerade starb, wählt noch ein Ziel.",
        "Parlez librement. Puis exilez quelqu'un, ou personne. Égalité au téléphone = personne ne sort. L'avocat peut bloquer un exil mafieux. Le chasseur, s'il vient de mourir, choisit encore une cible.",
        "随便聊。然后放逐一个人，或不放逐。手机投票平票则无人出局。律师可挡一次黑手党放逐。猎人刚死也能再点一个目标。",
    )
    val winBody = t(
        "Mafia wins if living evil is greater or equal to living good. Neutrals do not count in that race. Good wins when no evil remain and the killer is gone. Joker wins only if day-exiled. Killer wins if they are the last player alive. A solo whore wins at 1v1. A pest whore never wins. A living survivor does not block the win — they just pocket extra JEST.",
        "Mafija pobeđuje ako je živih zlih više ili jednako živim dobrima. Neutralni se ne broje. Dobri pobeđuju kad nema zlih i kad nema ubice. Džoker pobeđuje samo ako ispadne danju. Ubica pobeđuje ako ostane poslednji. Kurva sama pobedi na 1v1. Smeta nikad ne pobedi. Živi preživeli ne blokira pobedu — samo uzme extra JEST.",
        "La mafia vince se i cattivi vivi sono maggiori o uguali ai buoni. I neutrali non contano. I buoni vincono quando non restano cattivi e il killer è fuori. Lo Joker vince solo se esiliato di giorno. Il killer vince se resta l'ultimo. La puttana sola vince all'1v1. Chi solo disturba non vince mai. Un survivor vivo non blocca la vittoria — intasca solo JEST extra.",
        "La mafia gana si el mal vivo es mayor o igual que el bien. Los neutrales no cuentan. El bien gana cuando no queda mal y el asesino no está. El joker gana solo si lo exilian de día. El asesino gana si es el último vivo. La puta sola gana en 1v1. Quien solo molesta nunca gana. Un superviviente vivo no bloquea la victoria: solo se lleva JEST extra.",
        "Mafia gewinnt, wenn lebende Böse größer oder gleich lebenden Guten sind. Neutrale zählen nicht. Die Guten gewinnen, wenn kein Böses mehr da ist und der Killer weg ist. Der Joker gewinnt nur bei Tagesrauswurf. Der Killer gewinnt als letzter Lebender. Eine allein spielende Hure gewinnt im 1v1. Eine bloße Störerin gewinnt nie. Ein lebender Survivor blockt den Sieg nicht — er kassiert nur extra JEST.",
        "La mafia gagne si le mal vivant est supérieur ou égal au bien. Les neutres ne comptent pas. Les bons gagnent quand il n'y a plus de mal et plus de tueur. Le joker gagne seulement s'il est exilé le jour. Le tueur gagne s'il est le dernier vivant. Une putain solo gagne en 1v1. Une gêneuse ne gagne jamais. Un survivant vivant ne bloque pas la victoire — il empoche juste du JEST extra.",
        "活着的坏人不少于好人时，黑手党赢。中立不计入。没有坏人且杀手已死，好人赢。小丑只有白天被放逐才赢。杀手最后一个活着就赢。独自妓女 1v1 获胜。只捣乱的妓女永不赢。活着的幸存者不挡别人赢，只多拿一份 JEST。",
    )
    val about1 = t(
        "One phone hosts a live mafia game so nobody has to sit out as narrator.",
        "Jedan telefon vodi uživu mafija igru, niko ne sedi kao voditelj.",
        "Un telefono conduce una partita di mafia dal vivo, nessuno resta fuori come narratore.",
        "Un teléfono lleva una partida de mafia en vivo, nadie se queda de narrador.",
        "Ein Handy führt ein Live-Mafia-Spiel, niemand muss als Erzähler draußen sitzen.",
        "Un téléphone mène une partie de mafia en live, personne ne reste narrateur.",
        "一部手机主持现场黑手党局，不用有人当旁白坐冷板凳。",
    )
    val about2 = t(
        "Pass the phone around the table. Each player unlocks their role, takes a secret night action, then locks the screen before handing it on.",
        "Telefon ide oko stola. Svako otključa ulogu, uradi noćnu akciju, pa zaključa ekran pre predaje.",
        "Il telefono gira attorno al tavolo. Ognuno sblocca il ruolo, fa l'azione notturna, poi blocca lo schermo prima di passarlo.",
        "El teléfono da la vuelta a la mesa. Cada uno desbloquea su rol, hace la acción nocturna y bloquea la pantalla antes de pasarlo.",
        "Das Handy wandert um den Tisch. Jeder entsperrt seine Rolle, macht die Nachtaktion, sperrt den Bildschirm und gibt weiter.",
        "Le téléphone tourne autour de la table. Chacun déverrouille son rôle, fait l'action de nuit, puis verrouille l'écran avant de passer.",
        "手机围着桌子传。每人解锁身份、做秘密夜行动，再锁屏传给下一位。",
    )
    val about3 = t(
        "Mafia hunt at night. Town tries to vote them out by day. Advanced roles change the math. Read Rules before the first messy game.",
        "Mafija lovi noću. Grad ih izbacuje danju. Napredne uloge menjaju računicu. Pročitaj Pravila pre prve haotične partije.",
        "La mafia caccia di notte. La città li vota fuori di giorno. I ruoli avanzati cambiano i conti. Leggi le Regole prima del primo caos.",
        "La mafia caza de noche. El pueblo los echa de día. Los roles avanzados cambian las cuentas. Lee las Reglas antes del primer caos.",
        "Mafia jagt nachts. Die Stadt wirft sie tags raus. Spezialrollen ändern die Rechnung. Lies die Regeln vor dem ersten Chaos.",
        "La mafia chasse la nuit. Le village les vote le jour. Les rôles avancés changent le calcul. Lis les Règles avant le premier bordel.",
        "黑手党夜里猎杀。好人白天投票放逐。进阶身份改写胜负。开打前先看规则。",
    )
}
