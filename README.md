# Implementare utilizatori:
Am creat un fișier numit Users.csv, în care vor fi salvați sub forma user,password.
Pentru a verifica de fiecare dată autentificarea am creat 2 funcții: una de autentificare și una de login.

# Implementare întrebare:
Pentru întrebări am creat o clasă numită Question în care avem 3 constructori ce vor fi folosiți în funcție de caz: unul care doar instanțiază obiectul, fără modificarea atributelor, unul primește noile atribute și unul care instanțiază obiectul folosindu-se de o linie din fițierul Questions.csv în care sunt salvate întrebările.
Fiecare întrebarea va avea în plus un ID și un tablou de răspunsuri. Am creat o clasă Answer ce va fi folosită în acest scop; fiecare răspuns va avea la rîndul său un ID.
Funcția toString este cea care transformă informațiile unei întrebări într-o linie din fișierul Questions.csv.
Am creat funcții de calculare a punctajului unei întrebări dacă este răspunsă corect sau greșit, fără a rotunji.

# Implementare chestionare:
Analog întrebărilor, clasa Quiz va avea 3 constructori ce pot fi utilizați și funcția toString. Fișierul care le este asociat chestionarelor este numit Quizzes.csv.
Pentru task-ul de afișare a chestionarelor după un ID dat am creat o funcție în cadrul clasei ce afișează fiecare quiz în parte.
Punctajul finalde la o întrebare, fără rotunjire, este calculat prin funcțiile pozitivePoints și negativePoints ce vor fi apoi folosite în main pentru a calcula scorul final, rotunjit pe un quiz.

# Implemetarea soluțiilor
S-a realizat de asemenea cu ajutorul unui fișier Solutions.csv în care au fost stocate informațiile și o clasă auxiliară Solution.

# Bonus:
Cazuri limită ce puteau fi verificate:
1. -create-question: existența tipului unei întrebări
2. posibilitatea unui quiz de a avea mai mult de 10 intrebari
3. -delete-quizz: nu se verifică dacă user-ul conectat este cel care a creat chestionarul
4. un utilizator putea să scrie comanda într-o ordine greșită; corectitudinea comenzii nu este verificată în totalitate
5. You already submitted this quizz - neverificata

Refactorizări pentru comenzi și răspunsuri:
1. modul de scriere al răspunsurilor este destul de complex; nu consider necesare apostroafele din jurul fiecărui cuvânt
2. la întoarcerea tuturor întrebărilor din sistem, răspunsul putea fi scris pe mai multe linii pentru lizibilitate
3. la afișarea tuturor chestionarelor din sistem, răspunsul putea fi scris pe mai multe linii pentru lizibilitate
4. login putea să fie realizat o singură dată



