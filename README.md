# DiscordBot
Radu Deleanu - 2B1 - DiscordBot

Pentru acest proiect, am utilizat JDA impreuna cu alte librarii pentru a crea un bot de Discord: vaasBot

Librarii auxiliare folosite:
- Lavaplayer: comenzi ce tin de muzica
- HikariCP: database pool, folosit pt conectarea la SQLite
- xerial/SQLite-jdbc: implementare API JDBC
- duncte123/botCommons: diferite functii peste JDA (embedded messages etc.)

MainBot este clasa principala, in ea fiind creata instanta JDA-ului. Aceasta instanta creeaza la randul ei o instanta a clasei Listener,
in care se vor prelua si prelucra mesajele trimise pe canalele text de pe serverele de discord.

Pentru a executa comenzi, am creat clasa CommandManager, in care avem lista cu toate comenzile disponibile si metoda care separa argumentele de comanda si de prefix.
Comenzile in sine sunt create dupa interfata ICommand, care contine metoda handle (care primeste contextul comenzii: argumentele si evenimentul declansator in sine),
metoda getName si metoda getHelp.

Comenzile sunt rulate pe discord folosind un prefix intr-un canal de text. Acest prefix poate fi modificat cu ajutorul clasei SetPrefix. Pentru a tine minte ce prefix
este folosit pe fiecare server in parte, am creat o baza de date SQLite, accesata prin HikariCP si implementata prin SQLite-jdbc.

Unele comenzi folosesc API-uri produse de catre duncte123, ce produc JSON-uri usor de folosit. Ca exemplu, in comanda joke, se foloseste un API care extrage automat
o gluma la intamplare si o afiseaza in canalul text in care s-a apelat comanda.

Fiecare comanda este facuta in asa fel incat orice input gresit sa fie tratat. Astfel, comanda kick nu va functiona daca bot-ul nu are permisiuni de management al server-ului
sau daca acea persoana nu poate fi data afara.

Comenzile ce tin de muzica sunt implementate cu ajutorul bibliotecii LavaPlayer. Clasele folosite sunt:
- Track Scheduler: organizarea pieselor ce vor fi "cantate"
- AudioPlayerSendHandler: legatura dintre LavaPlayer si JDA cand se trimite audio intre bot si discord
- GuildMusicManager: "manageriaza" player-ul muzical dintr-un server particular
- PlayerManager: se ocupa de tot ce inseamna raspunderea mesajelor, selectarea server ului corect, apelarea track scheduler-ului corect etc.

Exemplu functionalitati muzicale - LavaPLayer
![image](https://user-images.githubusercontent.com/59650692/119503635-dce7ce80-bd73-11eb-80b7-ef94fdbb42ed.png)
