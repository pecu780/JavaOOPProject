 rendszer 3 fő osztályból áll:
1. Task (Feladat osztály) – Az adatmodell
   - Adattagok (mezők):
     - String title (A feladat neve)
       - boolean isDone (Teljesítve van-e már)
   - Függvények:
     - Konstruktor, Getters/Setters, toString() (a kiíratáshoz).
2. TaskManager (Üzleti logika osztály) – A logika
   - Adattagok:
     - List<Task> tasks (A feladatok listája)
       - String FILE_NAME = "tasks.txt" (A mentési fájl neve)
   - Függvények:
     - addTask(String title) (Új feladat hozzáadása)
       - listTasks() (Feladatok kiíratása)
       - markTaskDone(int index) (Készre jelölés)
       - saveToFile() / loadFromFile() (Fájlkezelés)
3. Main (Főprogram osztály) – A felület
   - Függvények:
     - main(String[] args) (Konzolos do-while menü ciklus a felhasználói interakcióhoz).
# JavaOOPProject
