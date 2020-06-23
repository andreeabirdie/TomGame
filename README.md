# TomGame
Proiectați și implementați o aplicație client-server pentru următoarea problemă.
Un joc cu 3 jucători numit TOM. Trei utilizatori autentificați pot juca acest joc. Pentru o literă primită de la server,
fiecare jucător trebuie să introducă numele unei țări, a unui oraș și unei mări care încep cu litera respectivă.
Jucătorul/Jucătorii care obține/obțin cele mai multe puncte după 3 runde, câștigă jocul. Fiecare jucător poate să facă
următoarele:
1. Login. După autentificarea cu succes se deschide o nouă fereastră în care este afișat un buton “Start joc”.
Doar după ce trei jucatori se autentifică în aplicație, butonul respectiv poate fi apăsat de oricare dintre
jucători. Când se apasă butonul de pornire a jocului, serverul va alege aleator o literă din alfabet și o va
trimite celor 3 jucatori.
2. Completează TOM. După primirea unei litere de la server, fiecare jucător introduce numele unei țări, a unui
oraș si a unei mări care încep cu litera respectivă și le trimite la server. Dupa ce toți jucătorii au trimis
informațiile la server, serverul verifică răspunsurile primite astfel:
• dacă jucătorul nu a completat nimic la un nume sau răspunsul nu începe cu litera ceruta sau
răspunsul nu este corect, jucătorul va primi 0 puncte pe răspunsul respectiv;
• dacă un răspuns începe cu litera cerută, este corect, dar se repetă (încă un jucător a dat același
răspuns), jucătorul va primi 3 puncte pe răspunsul respectiv;
• dacă răspunsul începe cu litera cerută, este corect și nu se repetă, jucătorul va primi 10 puncte pe
răspunsul respectiv;
Dupa verificarea răspunsurilor, serverul trimite tuturor jucătorilor punctajul total obținut de fiecare jucător la
runda respectivă și litera pentru runda următoare. Aceste informații vor apărea automat pe interfața grafică a
fiecărui jucator.
Acest pas se repeta de încă 2 ori. La finalul celor 3 runde, serverul va trimite tuturor jucătorilor clasamentul
final și toți jucătorii vor vedea clasamentul pe interfața grafică.
3. Un serviciu REST care permite vizualizarea clasamentului final pentru anumit joc.
4. Un serviciu REST care permite vizualizarea răspunsurilor date de un anumit jucator la un anumit joc și
punctajul obținut de acesta la fiecare rundă.
Observații:
1. Pentru verificarea răspunsurilor corecte, puteți păstra în baza de date nume de țări, orașe și mări.
2. Se acceptă ca serverul să genereze litere aleatoare dintr-un set mai mic (cel puțin 5 litere).
Cerințe:
• Aplicația poate fi dezvoltată în orice limbaj de programare (Java, C#, etc).
• Datele vor fi preluate/salvate dintr-o bază de date relațională.
• Pentru o entitate (exceptând jucator) se va folosi un instrument ORM pentru stocarea datelor.
• Pentru testarea serviciilor REST se va folosi o extensie a unui browser web/aplicație (Postman,
REST client, etc).
